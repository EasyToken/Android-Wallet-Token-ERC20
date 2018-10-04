package info.bcdev.librarysdkew.utils.identicon;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Identicon {

    public Identicon(BlockiesIdenticon identicon, String value){
        identicon.setVisibility(INVISIBLE);
        identicon.setCornerRadius(30);
        identicon.setAddress(value);
        identicon.setVisibility(VISIBLE);
    }

}
