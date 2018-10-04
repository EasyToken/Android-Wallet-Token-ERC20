package info.bcdev.librarysdkew.wallet;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class Balance {

    private EthGetBalance mEtherbalance;

    public Balance(Web3j web3j, String address) throws ExecutionException, InterruptedException {
        mEtherbalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
    }

    public BigInteger getInWei() {
        return mEtherbalance.getBalance();
    }

    public BigDecimal getInEther() {
        return Convert.fromWei(String.valueOf(mEtherbalance.getBalance()), Convert.Unit.ETHER);
    }


}
