package info.bcdev.librarysdkew.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastMsg {

    public void Short(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void Long(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
