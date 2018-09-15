package ru.fastsrv.easytoken;

import android.os.AsyncTask;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class setCredentional extends AsyncTask<Void, Void, Void> {

    public Integer mResult = 0;
    private File mKeystore;
    private Credentials mCredentials;

    public setCredentional(File keystore){
        mKeystore = keystore;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        File[] listfiles = mKeystore.listFiles();
        try {

            mCredentials = WalletUtils.loadCredentials(config.passwordwallet(), listfiles[0]);
            Global.setCredentials(mCredentials);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mResult = 1;
    }

}
