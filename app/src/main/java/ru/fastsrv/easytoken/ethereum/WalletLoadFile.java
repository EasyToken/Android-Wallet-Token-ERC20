package ru.fastsrv.easytoken.ethereum;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import ru.fastsrv.easytoken.Global;
import ru.fastsrv.easytoken.config;
import ru.fastsrv.easytoken.contract.TokenERC20;

public class WalletLoadFile extends AsyncTask<Map<String,String>, Integer, Map<String, String>> {

    private String mPasswordwallet;
    private Web3j web3j;

    public WalletLoadFile(){
        mPasswordwallet = config.passwordwallet();
        web3j = new InitWeb3j(config.addressethnode()).Get();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, String> doInBackground(Map<String,String>... params) {

        String smartcontract = params[0].get("smartcontract");
        BigInteger GasPrice = BigInteger.valueOf(Long.valueOf(params[0].get("gasprice")));
        BigInteger GasLimit = BigInteger.valueOf(Long.valueOf(params[0].get("gaslimit")));

        try {
            /**
             // Загружаем файл кошелька и получаем адрес
             // Upload the wallet file and get the address
             */
            Credentials credentials = Global.getCredentials();
            String address = credentials.getAddress();
            System.out.println("Eth Address: " + address);

            /**
             // Получаем Баланс
             // Get balance Ethereum
             */
            EthGetBalance etherbalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            String ethbalance = Convert.fromWei(String.valueOf(etherbalance.getBalance()), Convert.Unit.ETHER).toString();
            System.out.println("Eth Balance: " + ethbalance);

            /**
             // Загружаем Токен
             // Download Token
             */
            TokenERC20 token = TokenERC20.load(smartcontract, web3j, credentials, GasPrice, GasLimit);

            /**
             // Получаем название токена
             // Get the name of the token
             */
            String tokenname = token.name().send();
            System.out.println("Token Name: " + tokenname);

            /**
             // Получаем Символ Токена
             // Get Symbol marking token
             */
            String tokensymbol = token.symbol().send();
            System.out.println("Symbol Token: " + tokensymbol);

            /**
             // Получаем адрес Токена
             // Get The Address Token
             */
            String tokenaddress = token.getContractAddress();
            System.out.println("Address Token: " + tokenaddress);

            /**
             // Получаем общее количество выпускаемых токенов
             // Get the total amount of issued tokens
             */
            BigInteger totalSupply = token.totalSupply().send();
            System.out.println("Supply Token: "+totalSupply.toString());

            /**
             // Получаем количество токенов в кошельке
             // Receive the Balance of Tokens in the wallet
             */
            BigInteger tokenbalance = token.balanceOf(address).send();
            System.out.println("Balance Token: "+ tokenbalance.toString());

            Map<String,String> result = new HashMap<>();

            result.put("ethaddress",address);
            result.put("ethbalance", ethbalance);
            result.put("tokenbalance", tokenbalance.toString());
            result.put("tokenname", tokenname);
            result.put("tokensymbol", tokensymbol);
            result.put("tokenaddress",tokenaddress);
            result.put("tokensupply", totalSupply.toString());
            return result;
        } catch (Exception ex) {System.out.println("ERROR:" + ex);}

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Map<String, String> result) {
        super.onPostExecute(result);
    }
}
