package ru.fastsrv.easytoken.Gen;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.LinuxSecureRandom;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.fastsrv.easytoken.Global;
import ru.fastsrv.easytoken.MainActivity;
import ru.fastsrv.easytoken.OnTaskCompleted;
import ru.fastsrv.easytoken.config;
import ru.fastsrv.easytoken.ethereum.crypto.MnemonicUtils;

public class BIP44 {

    private String mPasswordwallet;

    private File mKeystoredir;

    private Credentials mCredentials;

    private OnTaskCompleted onTaskCompleted;

    public BIP44(MainActivity onTaskCompleted){
        this.onTaskCompleted = onTaskCompleted;
        mPasswordwallet = config.passwordwallet();
    }

    public Map<String, String> Get(File keydir){

        mKeystoredir = keydir;

        byte[] initialEntropy = new byte[16];
        secureRandom().nextBytes(initialEntropy);

        String seedCode = MnemonicUtils.generateMnemonic(initialEntropy);

        DeterministicSeed seed = null;
        try {

            /**
             * Use BitcoinJ
             */
            seed = new DeterministicSeed(seedCode, null, mPasswordwallet, 1409478661L);

            DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
            List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
            DeterministicKey key = chain.getKeyByPath(keyPath, true);
            BigInteger privKey = key.getPrivKey();

            /**
             * Use Web3J
             */
            mCredentials = Credentials.create(privKey.toString(16));

            Global.setCredentials(mCredentials);

            System.out.println("seedCode: " + seedCode);
            System.out.println("Generate BitcoinJ address: " +mCredentials.getAddress());
            System.out.println("PrivateKey: " +mCredentials.getEcKeyPair().getPrivateKey());
            System.out.println("PublicKey: " +mCredentials.getEcKeyPair().getPublicKey());

            Map<String, String> result = new HashMap<>();
            result.put("seedcode",seedCode);
            result.put("address",mCredentials.getAddress());

            onTaskCompleted.onTaskCompleted(result);
            return result;
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Random
     */
    private static final SecureRandom SECURE_RANDOM;

    static {
        if (isAndroidRuntime()) {
            new LinuxSecureRandom();
        }
        SECURE_RANDOM = new SecureRandom();
    }

    public static SecureRandom secureRandom() {
        return SECURE_RANDOM;
    }

    private static int isAndroid = -1;
    static boolean isAndroidRuntime() {
        if (isAndroid == -1) {
            final String runtime = System.getProperty("java.runtime.name");
            isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
        }
        return isAndroid == 1;
    }

}
