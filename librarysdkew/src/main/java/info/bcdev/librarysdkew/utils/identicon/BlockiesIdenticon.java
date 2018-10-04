package info.bcdev.librarysdkew.utils.identicon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import info.bcdev.librarysdkew.R;

/**
 * Custom view that is used to display a
 */

public class BlockiesIdenticon extends View {
    private final String TAG = this.getClass().getName();

    private final float DEFAULT_RADIUS = 10;

    private BlockiesData mData;
    private Paint mColor;
    private Paint mBackground;
    private Paint mSpot;
    private RectF mBlock = new RectF();
    private float cornerRadius = 20;

    private Paint mShadowPaint;
    private Paint mBrightPaint;
    private boolean hasShadow;

    public BlockiesIdenticon(Context context) {
        super(context);
        mData = new BlockiesData("", BlockiesData.DEFAULT_SIZE);
        init();
    }

    public BlockiesIdenticon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BlockiesIdenticon, 0, 0);
        String address = "";
        try{
            cornerRadius = a.getFloat(R.styleable.BlockiesIdenticon_radius, DEFAULT_RADIUS);
            address = a.getString(R.styleable.BlockiesIdenticon_address);
        }finally{
            a.recycle();
        }
        mData = new BlockiesData(address, BlockiesData.DEFAULT_SIZE);
        init();
    }

    private void init(){
        int[] colors = mData.getColors();
        mColor = new Paint();
        mColor.setStyle(Paint.Style.FILL);
        mColor.setColor(colors[0]);

        mBackground = new Paint();
        mBackground.setStyle(Paint.Style.FILL);
        mBackground.setAntiAlias(true);
        mBackground.setColor(colors[1]);

        mSpot = new Paint();
        mSpot.setStyle(Paint.Style.FILL);
        mSpot.setColor(colors[2]);

        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setDither(true);

        mBrightPaint = new Paint();
        mBrightPaint.setDither(true);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        // Account for padding
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float left = xpad;
        float right = (xpad + width);
        float top = ypad;
        float bottom = (ypad + height);
        mBlock.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path clipPath = new Path();
        float radius = 10.0f;
        float padding = radius / 2;
        int w = this.getWidth();
        int h = this.getHeight();
        clipPath.addRoundRect(new RectF(padding, padding, w - padding, h - padding), cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);

        canvas.drawRect(mBlock, mBackground);

        int[] data = mData.getImageData();
        double blockSize = w / Math.sqrt(data.length);
        for(int i = 0; i < data.length; i++){
            double x = (blockSize * i) % w;
            double y = Math.floor((blockSize * i) / w) * blockSize;
            RectF rect = new RectF((float)x, (float)y, (float)(x + blockSize),(float) (y + blockSize));
            if(data[i] == 2) {
                canvas.drawRect(rect, mSpot);
            }else if(data[i] == 1){
                canvas.drawRect(rect, mColor);
            }
        }

        if(hasShadow){
            int shadowColors[] = new int[]{Color.argb(200, 50, 50, 50), Color.argb(100, 0, 0, 0), Color.TRANSPARENT};
            float[] positions = new float[]{0.20f, 0.35f, 1f};
            LinearGradient shadowGradient = new LinearGradient((w/2), h, (w/2), (float) (h-blockSize), shadowColors, positions, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(shadowGradient);

            int brightColors[] = new int[]{Color.argb(100, 255, 255, 255), Color.TRANSPARENT};
            LinearGradient brightGradient = new LinearGradient((w/2), 0, (w/2), (float) blockSize, brightColors, null, Shader.TileMode.CLAMP);
            mBrightPaint.setShader(brightGradient);

            canvas.drawRect(mBlock, mShadowPaint);
            canvas.drawRect(mBlock, mBrightPaint);
        }
    }

    public void setAddress(String address){
        this.setAddress(address, BlockiesData.DEFAULT_SIZE);
        init();
    }

    public void setAddress(String address, int size){
        mData = new BlockiesData(address, size);
        init();
    }

    public void setCornerRadius(float radius){
        cornerRadius = radius;
        init();
    }

    public void setHasShadow(boolean hasShadow){
        this.hasShadow = hasShadow;
    }

    public boolean isHasShadow(){
        return this.hasShadow;
    }
}
