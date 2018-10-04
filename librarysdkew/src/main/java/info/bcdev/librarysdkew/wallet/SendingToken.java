package info.bcdev.librarysdkew.wallet;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

import info.bcdev.librarysdkew.interfaces.callback.CBSendingEther;
import info.bcdev.librarysdkew.interfaces.callback.CBSendingToken;
import info.bcdev.librarysdkew.smartcontract.TokenERC20;

public class SendingToken {

    private Credentials mCredentials;
    private Web3j mWeb3j;
    private String fromAddress;
    private String mValueGasPrice;
    private String mValueGasLimit;

    private CBSendingEther cbSendingEther;
    private CBSendingToken cbSendingToken;

    public SendingToken(Web3j web3j, Credentials credentials, String valueGasPrice, String valueGasLimit){
        mWeb3j = web3j;
        mCredentials = credentials;
        fromAddress = credentials.getAddress();
        mValueGasPrice = valueGasPrice;
        mValueGasLimit = valueGasLimit;
    }

    private BigInteger getGasPrice(){
        return BigInteger.valueOf(Long.valueOf(mValueGasPrice));
    }

    private BigInteger getGasLimit(){
        return BigInteger.valueOf(Long.valueOf(mValueGasLimit));
    }

    public void Send(String smartContractAddress, String toAddress, String valueAmmount) {
        new SendToken().execute(smartContractAddress,toAddress,valueAmmount);
    }

    private class SendToken extends AsyncTask<String,Void,TransactionReceipt> {

        @Override
        protected TransactionReceipt doInBackground(String... value) {

            BigInteger ammount = BigInteger.valueOf(Long.parseLong(value[2]));

            System.out.println(getGasPrice());
            System.out.println(getGasLimit());

            TokenERC20 token = TokenERC20.load(value[0], mWeb3j, mCredentials, getGasPrice(), getGasLimit());
            try {
                TransactionReceipt result = token.transfer(value[1], ammount).send();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(TransactionReceipt result) {
            super.onPostExecute(result);
            cbSendingToken.backSendToken(result);
        }
    }

    public void registerCallBackToken(CBSendingToken cbSendingToken){
        this.cbSendingToken = cbSendingToken;
    }

}
