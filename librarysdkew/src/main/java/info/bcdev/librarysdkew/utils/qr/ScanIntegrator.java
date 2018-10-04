package info.bcdev.librarysdkew.utils.qr;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanIntegrator {

    public static IntentIntegrator sIntentIntegrator;

    public ScanIntegrator(Activity activity){
        sIntentIntegrator = new IntentIntegrator(activity);
    }

    public void startScan(){
        sIntentIntegrator.setOrientationLocked(false);
        sIntentIntegrator.setBarcodeImageEnabled(true);
        sIntentIntegrator.initiateScan();
    }

}
