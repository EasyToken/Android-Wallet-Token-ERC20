package info.bcdev.librarysdkew.wallet;

import android.os.AsyncTask;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import info.bcdev.librarysdkew.interfaces.callback.CBSendingEther;

public class SendingEther {

    private Credentials mCredentials;
    private Web3j mWeb3j;
    private String fromAddress;
    private String mValueGasPrice;
    private String mValueGasLimit;

    private CBSendingEther cbSendingEther;

    public SendingEther(Web3j web3j, Credentials credentials, String valueGasPrice, String valueGasLimit){
        mWeb3j = web3j;
        mCredentials = credentials;
        fromAddress = credentials.getAddress();
        mValueGasPrice = valueGasPrice;
        mValueGasLimit = valueGasLimit;
    }

    private BigInteger getNonce() throws ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount = mWeb3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetTransactionCount.getTransactionCount();
    }

    private BigInteger getGasPrice(){
        return BigInteger.valueOf(Long.valueOf(mValueGasPrice));
    }

    private BigInteger getGasLimit(){
        return BigInteger.valueOf(Long.valueOf(mValueGasLimit));
    }

    public void Send(String toAddress, String valueAmmount) {
        new SendEthereum().execute(toAddress, valueAmmount);
    }

    private class SendEthereum extends AsyncTask<String,Void,EthSendTransaction> {

        @Override
        protected EthSendTransaction doInBackground(String... values) {
            BigInteger ammount = Convert.toWei(values[1], Convert.Unit.ETHER).toBigInteger();
            try {

                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(getNonce(), getGasPrice(), getGasLimit(), values[0], ammount);

                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, mCredentials);
                String hexValue = "0x"+ Hex.toHexString(signedMessage);

                return mWeb3j.ethSendRawTransaction(hexValue.toString()).sendAsync().get();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(EthSendTransaction result) {
            super.onPostExecute(result);
            cbSendingEther.backSendEthereum(result);
        }
    }

    public void registerCallBack(CBSendingEther cbSendingEther){
        this.cbSendingEther = cbSendingEther;
    }

}
