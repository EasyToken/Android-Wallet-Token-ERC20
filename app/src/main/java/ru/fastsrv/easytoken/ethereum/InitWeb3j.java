package ru.fastsrv.easytoken.ethereum;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class InitWeb3j {
    private Web3j web3j;

    /**
     * Construct a new Web3j instance.
     *
     * @param urlnode address Node - i.e. HTTP or IPC
     * @return new Web3j instance
     */

    public InitWeb3j(String urlnode){
        web3j = Web3jFactory.build(new HttpService(urlnode));
    }

    public Web3j Get(){
        return web3j;
    }
}
