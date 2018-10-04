package info.bcdev.librarysdkew.interfaces.callback;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

public interface CBSendingEther {
    void backSendEthereum(EthSendTransaction result);
}
