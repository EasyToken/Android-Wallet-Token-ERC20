package ru.fastsrv.easytoken.contract;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import ru.fastsrv.easytoken.Global;
import ru.fastsrv.easytoken.config;
import ru.fastsrv.easytoken.ethereum.InitWeb3j;


///////////////////////// Sending Tokens /////////////////////
public class SendingToken extends AsyncTask<Map<String,String>, Integer, Map<String, String>> {

    private String mPasswordwallet = config.passwordwallet();
    private Web3j web3j = new InitWeb3j(config.addressethnode()).Get();

    private String smartcontract, sendtoaddress, sendtokenvalue;
    private BigInteger GasPrice, GasLimit;

    public SendingToken(Map<String,String> values){

        smartcontract = values.get("smartcontract");
        GasPrice = BigInteger.valueOf(Long.valueOf(values.get("gasprice")));
        GasLimit = BigInteger.valueOf(Long.valueOf(values.get("gaslimit")));
        sendtoaddress = values.get("sendtoaddress");
        sendtokenvalue = values.get("sendtokenvalue");

        System.out.println(smartcontract);
        System.out.println(GasPrice);
        System.out.println(GasLimit);
        System.out.println(sendtoaddress);
        System.out.println(sendtokenvalue);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, String> doInBackground(Map<String,String>... param) {

        try {
            /**
             // Загружаем файл кошелька и получаем адрес
             // Upload the wallet file and get the address
             */
            Credentials credentials = Global.getCredentials();
            String address = credentials.getAddress();
            System.out.println("Eth Address: " + address);

            /**
             * Загружаем Токен
             * Load Token
             */
            TokenERC20 token = TokenERC20.load(smartcontract, web3j, credentials, GasPrice, GasLimit);

            String status = null;
            String balance = null;

            /**
             * Конвертируем сумму токенов в BigInteger и отправляем на указанные адрес
             * Convert the amount of tokens to BigInteger and send to the specified address
             */
            BigInteger sendvalue = BigInteger.valueOf(Long.parseLong(sendtokenvalue));
            status = token.transfer(sendtoaddress, sendvalue).send().getTransactionHash();

            /**
             * Обновляем баланс Токенов
             * Renew Token balance
             */
            BigInteger tokenbalance = token.balanceOf(address).send();
            System.out.println("Balance Token: "+ tokenbalance.toString());
            balance = tokenbalance.toString();

            /**
             * Возвращаем из потока, Статус транзакции и баланс Токенов
             * Returned from thread, transaction Status and Token balance
             */
            Map<String,String> result = new HashMap<>();
            result.put("balance",balance);
            result.put("status",status);

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

        if (result != null) {
            //tokenbalance.setText(result.get("balance").toString());
            //Toast toast = Toast.makeText(getApplicationContext(),result.get("status").toString(), Toast.LENGTH_LONG);
            //toast.show();
        } else {System.out.println();}
    }
}
/////////////////////// End Sending Tokens ///////////////////