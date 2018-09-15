package ru.fastsrv.easytoken.Gen;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.LinuxSecureRandom;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.fastsrv.easytoken.config;
import ru.fastsrv.easytoken.ethereum.crypto.MnemonicUtils;

public class BIP44 {

    private String mPasswordwallet;

    private File mKeystoredir;

    public BIP44(File keystoredir){
        mKeystoredir = keystoredir;
        mPasswordwallet = config.passwordwallet();
    }

    public Map<String, String> Get(){

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
            Credentials credentials = Credentials.create(privKey.toString(16));

            System.out.println("seedCode: " + seedCode);
            System.out.println("Generate BitcoinJ address: " +credentials.getAddress());
            System.out.println("PrivateKey: " +credentials.getEcKeyPair().getPrivateKey());
            System.out.println("PublicKey: " +credentials.getEcKeyPair().getPublicKey());

            String FileWallet = WalletUtils.generateWalletFile(mPasswordwallet,credentials.getEcKeyPair(), mKeystoredir,false);

            Map<String, String> result = new HashMap<>();
            result.put("seedcode",seedCode);
            result.put("address",credentials.getAddress());
            result.put("privatekey",credentials.getEcKeyPair().getPrivateKey().toString());
            result.put("publickey",credentials.getEcKeyPair().getPublicKey().toString());
            System.out.println("BIP44 FILE Wallet: "+ FileWallet);

            return result;

        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
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
