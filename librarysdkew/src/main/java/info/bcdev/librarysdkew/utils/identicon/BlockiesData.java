package info.bcdev.librarysdkew.utils.identicon;

import android.support.v4.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that encapsulates the preudo-random generation of block data and colors
 */

public class BlockiesData {
    private final String TAG = this.getClass().getName();
    public static final int DEFAULT_SIZE = 8;

    private int[] randSeed;
    private int imageData[];
    private String seed;

    private int color;
    private int bgColor;
    private int spotColor;


    public BlockiesData(String seed, int size){
        if(seed == null){
            seed = "";
        }
        this.seed = seed.toLowerCase();
        this.randSeed = new int[]{0,0,0,0};
        seedRand(this.seed);
        createIcon(size);
    }

    private int[] seedRand(String seed){
        for(int i = 0; i < seed.length(); i++){
            randSeed[i % 4] = ((randSeed[i % 4] << 5) - randSeed[i % 4]) + ((int) seed.charAt(i));
        }
        return randSeed;
    }

    private void createIcon(int size){
        color = createColor();
        bgColor = createColor();
        spotColor = createColor();
        imageData = createImageData(size);
    }

    private double rand(){
        int t = randSeed[0] ^ (randSeed[0] << 11);
        randSeed[0] = randSeed[1];
        randSeed[1] = randSeed[2];
        randSeed[2] = randSeed[3];
        randSeed[3] = (randSeed[3] ^(randSeed[3] >> 19) ^ t ^ (t >> 8));
        double num = (randSeed[3] >>> 0);
        double den = ((1 << 31) >>> 0);
        return Math.abs(num / den);
    }

    private int createColor(){
        float h = (float) Math.floor(rand() * 360);
        float s = (float) ((rand()*60)+40) / 100;
        float l = (float) ((rand()+rand()+rand()+rand())*25) / 100;
        return ColorUtils.HSLToColor(new float[]{h, s, l});
    }

    private int[] createImageData(int size){
        int width = size;
        int height = size;
        int dataWidth = (int) Math.ceil(width / 2);
        int mirrorWidth = width - dataWidth;
        ArrayList<Integer> data = new ArrayList<>();
        for(int y = 0; y < height; y++){
            ArrayList<Integer> row = new ArrayList<>();
            for(int x = 0; x < dataWidth; x++){
                double r = rand() * 2.3;
                double d = Math.floor(r);
                int add = (int) d;
                row.add(add);
            }

            List<Integer> r = new ArrayList<>();
            for(int i = 0; i < mirrorWidth; i++){
                r.add(row.get(i));
            }

            Collections.reverse(r);
            for(int i = 0; i < r.size(); i++){
                row.add(r.get(i));
            }
            for(int i = 0; i < row.size(); i++){
                data.add(row.get(i));
            }
        }
        return toIntArray(data);
    }

    private int[] toIntArray(List<Integer> list)  {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e.intValue();
        return ret;
    }

    /**
     * Retrieves the generated image data
     * @return Image data
     */
    public int[] getImageData(){
        return imageData;
    }

    /**
     * Gets the generated colors in the form of an array of integers.
     * These are:
     *
     *  [0] color
     *  [1] background color
     *  [2] spot color
     *
     * @return
     */
    public int[] getColors(){
        return new int[]{ color, bgColor, spotColor};
    }
}
