package ru.fastsrv.easytoken;

import org.web3j.crypto.Credentials;

import java.io.File;

public class Global {

    private static File sKeystoredir;

    private static Credentials sCredentials;

    public static void setKeystoreDir(File keystoreDir){
        sKeystoredir = keystoreDir;
    }
    public static File getKeystoreDir(){
        return sKeystoredir;
    }

    public static void setCredentials(Credentials credentials){
        sCredentials = credentials;
    }
    public static Credentials getCredentials(){
        return sCredentials;
    }

}
