package info.bcdev.librarysdkew.smartcontract;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import info.bcdev.librarysdkew.interfaces.callback.CBLoadSmartContract;

public class LoadSmartContract {

    private CBLoadSmartContract cbLoadSmartContract;
    private Web3j mWeb3j;
    private Credentials mCredentials;
    private String mSmartContractAddress;
    private BigInteger mGasPrice;
    private BigInteger mGasLimit;

    public LoadSmartContract(Web3j web3j,
                             Credentials credentials,
                             String smartContractAddress,
                             BigInteger gasPrice,
                             BigInteger gasLimit){
        mWeb3j = web3j;
        mCredentials = credentials;
        mSmartContractAddress = smartContractAddress;
        mGasPrice = gasPrice;
        mGasLimit = gasLimit;
    }

    public void LoadToken(){
        new Token().execute();
    }

    private class Token extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            try {
                /**
                 // Загружаем файл кошелька и получаем адрес
                 // Upload the wallet file and get the address
                 */
                String address = mCredentials.getAddress();

                /**
                 // Получаем Баланс
                 // Get balance Ethereum
                 */
                EthGetBalance etherbalance = mWeb3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
                String ethbalance = Convert.fromWei(String.valueOf(etherbalance.getBalance()), Convert.Unit.ETHER).toString();
                System.out.println("Eth Balance: " + ethbalance);

                /**
                 // Загружаем Токен
                 // Download Token
                 */
                TokenERC20 token = TokenERC20.load(mSmartContractAddress, mWeb3j, mCredentials, mGasPrice, mGasLimit);

                String tokenname = token.name().send();

                String tokensymbol = token.symbol().send();

                String tokenaddress = token.getContractAddress();

                BigInteger totalsupply = token.totalSupply().send();

                BigInteger tokenbalance = token.balanceOf(address).send();

                Map<String,String> result = new HashMap<>();
                result.put("tokenname",tokenname);
                result.put("tokensymbol",tokensymbol);
                result.put("tokenaddress",tokenaddress);
                result.put("totalsupply",totalsupply.toString());
                result.put("tokenbalance",tokenbalance.toString());

                return result;
            } catch (Exception ex) {System.out.println("ERROR:" + ex);}

            return null;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            super.onPostExecute(result);
            if (result != null){
                cbLoadSmartContract.backLoadSmartContract(result);
            }
        }
    }

    public void registerCallBack(CBLoadSmartContract cbLoadSmartContract){
        this.cbLoadSmartContract = cbLoadSmartContract;
    }
}
