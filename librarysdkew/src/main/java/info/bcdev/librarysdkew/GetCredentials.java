package info.bcdev.librarysdkew;

import android.os.AsyncTask;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import info.bcdev.librarysdkew.interfaces.callback.CBGetCredential;

public class GetCredentials {

    private CBGetCredential cbGetCredential;

    public void FromFile(String walletFile,
                                String passwordWallet) throws IOException, CipherException {
        new LoadWalletFile().execute(walletFile, passwordWallet);
    }

    public Credentials FromPrivatKey(String privatKey){
        BigInteger privatkey = new BigInteger(privatKey);
        return Credentials.create(privatkey.toString(16));
    }

    public Credentials FromSeed(String seedCode,
                                String passwordWallet) throws UnreadableWalletException {
        DeterministicSeed seed = new DeterministicSeed(seedCode, null, passwordWallet, 1409478661L);
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();
        return Credentials.create(ECKeyPair.create(privKey));
    }

    private class LoadWalletFile extends AsyncTask<String,Void,Credentials>{

        @Override
        protected Credentials doInBackground(String... values) {

            File walletFile = new File(values[0]);

            try {
                return WalletUtils.loadCredentials(values[1], walletFile);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Credentials credentials) {
            super.onPostExecute(credentials);

            if (credentials != null){
                cbGetCredential.backLoadCredential(credentials);
            }

        }
    }

    public void registerCallBack(CBGetCredential cbGetCredential){
        this.cbGetCredential = cbGetCredential;
    }
}
