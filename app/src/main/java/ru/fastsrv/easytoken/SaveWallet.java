package ru.fastsrv.easytoken;

import android.os.AsyncTask;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class SaveWallet extends AsyncTask<Void,Void,Void>{

    private String mPasswordwallet;

    private File mKeystoredir;

    private Credentials mCredentials;

    public SaveWallet(File keydir){
        mKeystoredir = keydir;
        mCredentials = Global.getCredentials();
        mPasswordwallet = config.passwordwallet();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String FileWallet = null;
        try {
            FileWallet = WalletUtils.generateWalletFile(mPasswordwallet,mCredentials.getEcKeyPair(), mKeystoredir,false);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("BIP44 FILE Wallet: "+ FileWallet);

        return null;
    }
}
