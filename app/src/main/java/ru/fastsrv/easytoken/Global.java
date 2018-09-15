package ru.fastsrv.easytoken;

import org.web3j.crypto.Credentials;

public class Global {

    private static Credentials sCredentials;

    public static void setCredentials(Credentials credentials){
        sCredentials = credentials;
    }
    public static Credentials getCredentials(){
        return sCredentials;
    }

}
