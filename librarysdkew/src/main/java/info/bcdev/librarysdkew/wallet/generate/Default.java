package info.bcdev.librarysdkew.wallet.generate;

import org.web3j.crypto.WalletUtils;

import java.io.File;

public class Default {

    public String Generation(File keystoreWallet, String passwordWallet){
        try {
            return WalletUtils.generateNewWalletFile(passwordWallet, keystoreWallet, false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}
