package ru.fastsrv.easytoken.Gen;

import org.web3j.crypto.WalletUtils;

import java.io.File;

import ru.fastsrv.easytoken.config;

@Deprecated
public class Default {

    private String mPasswordwallet;
    private File mKeystoredir;

    public Default(File keystoredir){
        mKeystoredir = keystoredir;
        mPasswordwallet = config.passwordwallet();
    }

    @Deprecated
    public void Get(){
        try {
            String fileName = WalletUtils.generateNewWalletFile(mPasswordwallet, mKeystoredir, false);

            System.out.println("FileName: " + fileName);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
