package info.bcdev.librarysdkew.wallet.generate;

import android.os.AsyncTask;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.LinuxSecureRandom;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.bcdev.librarysdkew.interfaces.callback.CBBip44;
import info.bcdev.librarysdkew.wallet.generate.crypto.MnemonicUtils;

import static org.bitcoinj.core.Utils.isAndroidRuntime;

public class Bip44 extends AsyncTask<String, Void, Map<String, String>> {

    private static final SecureRandom SECURE_RANDOM;

    private Credentials mCredentials;

    private CBBip44 cbBip44;

    static {
        if (isAndroidRuntime()) {
            new LinuxSecureRandom();
        }
        SECURE_RANDOM = new SecureRandom();
    }

    public static SecureRandom secureRandom() {
        return SECURE_RANDOM;
    }

    public Map<String, String> Generation(String passwordWallet){

        byte[] initialEntropy = new byte[16];

        secureRandom().nextBytes(initialEntropy);

        String seedCode = MnemonicUtils.generateMnemonic(initialEntropy);

        DeterministicSeed seed = null;
        try {

            seed = new DeterministicSeed(seedCode, null, passwordWallet, 1409478661L);
            DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
            List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
            DeterministicKey key = chain.getKeyByPath(keyPath, true);
            BigInteger privKey = key.getPrivKey();

            mCredentials =  Credentials.create(privKey.toString(16));

            Map<String, String> result = new HashMap<>();

            result.put("seedcode",seedCode);
            result.put("address", mCredentials.getAddress());
            result.put("privatekey", mCredentials.getEcKeyPair().getPrivateKey().toString());
            result.put("publickey", mCredentials.getEcKeyPair().getPublicKey().toString());

            return result;

        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Map<String, String> doInBackground(String... values) {
        return Generation(values[0]);
    }

    protected void onPostExecute(Map<String, String> result){
        if (result != null) {
            cbBip44.backGeneration(result,mCredentials);
        }
    }

    public void registerCallBack(CBBip44 cbBip44){
        this.cbBip44 = cbBip44;
    }
}
