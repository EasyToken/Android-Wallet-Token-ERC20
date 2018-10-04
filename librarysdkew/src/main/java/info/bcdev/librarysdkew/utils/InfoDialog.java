package info.bcdev.librarysdkew.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class InfoDialog {

    private Context mContext;

    private AlertDialog infodialog;

    public InfoDialog(Context context){
        mContext = context;
    }

    public void Get(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setTitle(title);
        infodialog = builder.create();
        infodialog.setCanceledOnTouchOutside(false);
        infodialog.show();
    }

    public void Dismiss(){
        infodialog.dismiss();
    }

}
