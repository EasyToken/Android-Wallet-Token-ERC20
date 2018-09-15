package ru.fastsrv.easytoken.ethereum;

import android.os.AsyncTask;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import ru.fastsrv.easytoken.Global;
import ru.fastsrv.easytoken.config;

public class SendingEther  extends AsyncTask<Void, Integer, Map<String, String>> {

    private String mPasswordwallet = config.passwordwallet();
    private Web3j web3j = new InitWeb3j(config.addressethnode()).Get();

    private String sendtoaddress, sendethervalue;
    private BigInteger GasPrice, GasLimit;

    public SendingEther(Map<String,String> values){

        GasPrice = BigInteger.valueOf(Long.valueOf(values.get("gasprice")));
        GasLimit = BigInteger.valueOf(Long.valueOf(values.get("gaslimit")));
        sendtoaddress = values.get("sendtoaddress");
        sendethervalue = values.get("sendethervalue");

        System.out.println(GasPrice);
        System.out.println(GasLimit);
        System.out.println(sendtoaddress);
        System.out.println(sendethervalue);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {

        try {

            /**
             // Загружаем файл кошелька и получаем адрес
             // Upload the wallet file and get the address
             */
            Credentials credentials = Global.getCredentials();
            String address = credentials.getAddress();
            System.out.println("Eth Address: " + address);

            /**
             * Получаем счетчик транзакций
             * Get count transaction
             */
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            /**
             * Convert ammount ether to BigInteger
             */
            BigInteger value = Convert.toWei(sendethervalue, Convert.Unit.ETHER).toBigInteger();

            /**
             * Транзакция
             * Transaction
             */
            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, GasPrice, GasLimit, sendtoaddress, value);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = "0x"+ Hex.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue.toString()).sendAsync().get();

            /**
             * Get Transaction Error and Hash
             */
            System.out.println("Error: "+ ethSendTransaction.getError());
            System.out.println("Transaction: " + ethSendTransaction.getTransactionHash());

            /**
             * Получаем баланс Ethereum
             * Get balance Ethereum
             */
            EthGetBalance etherbalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            String ethbalanceafter = Convert.fromWei(String.valueOf(etherbalance.getBalance()), Convert.Unit.ETHER).toString();

            /**
             * Возвращаем из потока, Баланс и Хэш транзакции
             * Returned from thread, Ether Balance and transaction hash
             */

            Map<String,String> result = new HashMap<>();
            result.put("transactionhash", ethSendTransaction.getTransactionHash());
            result.put("balance",ethbalanceafter);

            return result;

        }catch (Exception ex) {ex.printStackTrace();}
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
