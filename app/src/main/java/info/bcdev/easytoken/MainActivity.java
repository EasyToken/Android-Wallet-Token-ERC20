package info.bcdev.easytoken;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import info.bcdev.librarysdkew.GetCredentials;
import info.bcdev.librarysdkew.interfaces.callback.CBBip44;
import info.bcdev.librarysdkew.interfaces.callback.CBGetCredential;
import info.bcdev.librarysdkew.interfaces.callback.CBLoadSmartContract;
import info.bcdev.librarysdkew.interfaces.callback.CBSendingEther;
import info.bcdev.librarysdkew.interfaces.callback.CBSendingToken;
import info.bcdev.librarysdkew.smartcontract.LoadSmartContract;
import info.bcdev.librarysdkew.utils.InfoDialog;
import info.bcdev.librarysdkew.utils.ToastMsg;
import info.bcdev.librarysdkew.utils.qr.Generate;
import info.bcdev.librarysdkew.utils.qr.ScanIntegrator;
import info.bcdev.librarysdkew.wallet.Balance;
import info.bcdev.librarysdkew.wallet.SendingEther;
import info.bcdev.librarysdkew.wallet.SendingToken;
import info.bcdev.librarysdkew.wallet.generate.Bip44;
import info.bcdev.librarysdkew.web3j.Initiate;

/**
 *
 * @author Dmitry Markelov
 * Telegram group: https://t.me/joinchat/D62dXAwO6kkm8hjlJTR9VA
 *
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Если есть вопросы, отвечу в телеграме
 * If you have any questions, I will answer the telegram
 *
 *    Russian:
 *    Пример включает следующие функции:
 *       - Получаем адрес кошелька
 *       - Получаем баланс Eth
 *       - Получаем баланс Токена
 *       - Получаем название Токена
 *       - Получаем символ Токена
 *       - Получаем адрес Контракта Токена
 *       - Получаем общее количество выпущеных Токенов
 *
 *
 *   English:
 *   The example includes the following functions:
 *       - Get address wallet
 *       - Get balance Eth
 *       - Get balance Token
 *       - Get Name Token
 *       - Get Symbol Token
 *       - Get contract Token address
 *       - Get supply Token
 *
 */

public class MainActivity extends AppCompatActivity implements CBGetCredential, CBLoadSmartContract, CBBip44, CBSendingEther, CBSendingToken {

    private String mNodeUrl = config.addressethnode(2);

    private String mPasswordwallet = config.passwordwallet();

    private String mSmartcontract = config.addresssmartcontract(1);

    TextView ethaddress, ethbalance, tokenname, tokensymbol, tokensupply, tokenaddress, tokenbalance, tokensymbolbalance, seedcode;
    TextView tv_gas_limit, tv_gas_price, tv_fee;
    EditText sendtoaddress, sendtokenvalue, sendethervalue;

    ImageView qr_small, qr_big;

    final Context context = this;

    IntentIntegrator qrScan;

    private Web3j mWeb3j;

    private File keydir;

    private Credentials mCredentials;

    private InfoDialog mInfoDialog;

    private BigInteger mGasPrice;

    private BigInteger mGasLimit;

    private SendingEther sendingEther;

    private SendingToken sendingToken;

    private ToastMsg toastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoDialog = new InfoDialog(this);

        ethaddress = (TextView) findViewById(R.id.ethaddress); // Your Ether Address
        ethbalance = (TextView) findViewById(R.id.ethbalance); // Your Ether Balance

        tokenname = (TextView) findViewById(R.id.tokenname); // Token Name
        tokensymbol = (TextView) findViewById(R.id.tokensymbol); // Token Symbol
        tokensupply = (TextView) findViewById(R.id.tokensupply); // Token Supply
        tokenaddress = (TextView) findViewById(R.id.tokenaddress); // Token Address
        tokenbalance = (TextView) findViewById(R.id.tokenbalance); // Token Balance
        tokensymbolbalance = (TextView) findViewById(R.id.tokensymbolbalance);
        seedcode = (TextView) findViewById(R.id.seedcode);

        sendtoaddress = (EditText) findViewById(R.id.sendtoaddress); // Address for sending ether or token

        sendtokenvalue = (EditText) findViewById(R.id.SendTokenValue); // Ammount token for sending
        sendethervalue = (EditText) findViewById(R.id.SendEthValue); // Ammount ether for sending

        qr_small = (ImageView)findViewById(R.id.qr_small);

        qrScan = new IntentIntegrator(this);

        tv_gas_limit = (TextView) findViewById(R.id.tv_gas_limit);
        tv_gas_price = (TextView) findViewById(R.id.tv_gas_price);
        tv_fee = (TextView) findViewById(R.id.tv_fee);

        final SeekBar sb_gas_limit = (SeekBar) findViewById(R.id.sb_gas_limit);
        sb_gas_limit.setOnSeekBarChangeListener(seekBarChangeListenerGL);
        final SeekBar sb_gas_price = (SeekBar) findViewById(R.id.sb_gas_price);
        sb_gas_price.setOnSeekBarChangeListener(seekBarChangeListenerGP);

        GetFee();

        getWeb3j();

        toastMsg = new ToastMsg();

        keydir = this.getExternalFilesDir("/keystore/");

        File[] listfiles = keydir.listFiles();
        if (listfiles.length == 0 ) {

            CreateWallet();

        } else {

            getCredentials(keydir);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SendEther:
                sendEther();
                break;
            case R.id.SendToken:
                sendToken();
                break;
            case R.id.qr_small:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.qr_view);
                qr_big = (ImageView) dialog.findViewById(R.id.qr_big);
                qr_big.setImageBitmap(new Generate().Get(getEthAddress(),600,600));
                dialog.show();
                break;
            case R.id.qrScan:
                new ScanIntegrator(this).startScan();
                break;
        }
    }

    /* Create Wallet */
    private void CreateWallet(){
        Bip44 bip44 = new Bip44();
        bip44.registerCallBack(this);
        bip44.execute(mPasswordwallet);
        mInfoDialog.Get("Wallet generation", "Please wait few seconds");
    }

    @Override
    public void backGeneration(Map<String, String> result, Credentials credentials) {
        mCredentials = credentials;
        setEthAddress(result.get("address"));
        setEthBalance(getEthBalance());
        setSeed(result.get(seedcode));
        new SaveWallet(keydir,mCredentials,mPasswordwallet).execute();
        mInfoDialog.Dismiss();
    }

    private void setSeed(String seed){
        seedcode.setText(seed);
    }
    /* End Create Wallet*/

    /* Get Web3j*/
    private void getWeb3j(){
        new Initiate(mNodeUrl);
        mWeb3j = Initiate.sWeb3jInstance;
    }

    /* Get Credentials */
    private void getCredentials(File keydir){
        File[] listfiles = keydir.listFiles();
        try {
            mInfoDialog.Get("Load Wallet","Please wait few seconds");
            GetCredentials getCredentials = new GetCredentials();
            getCredentials.registerCallBack(this);
            getCredentials.FromFile(listfiles[0].getAbsolutePath(),mPasswordwallet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void backLoadCredential(Credentials credentials) {
        mCredentials = credentials;
        mInfoDialog.Dismiss();
        LoadWallet();
    }
    /* End Get Credentials */

    private void LoadWallet(){
        setEthAddress(getEthAddress());
        setEthBalance(getEthBalance());
        GetTokenInfo();
    }

    /* Get Address Ethereum */
    private String getEthAddress(){
        return mCredentials.getAddress();
    }

    /* Set Address Ethereum */
    private void setEthAddress(String address){
        ethaddress.setText(address);
        qr_small.setImageBitmap(new Generate().Get(address,200,200));
    }

    private String getToAddress(){
        return sendtoaddress.getText().toString();
    }

    private void setToAddress(String toAddress){
        sendtoaddress.setText(toAddress);
    }

    /* Get Balance */
    private String getEthBalance(){
        try {
            return new Balance(mWeb3j,getEthAddress()).getInEther().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Get Send Ammount */
    private String getSendEtherAmmount(){
        return sendethervalue.getText().toString();
    }

    private String getSendTokenAmmount(){
        return sendtokenvalue.getText().toString();
    }

    /* Set Balance */
    private void setEthBalance(String ethBalance){
        ethbalance.setText(ethBalance);
    }

    public void GetFee(){
        setGasPrice(getGasPrice());
        setGasLimit(getGasLimit());

        BigDecimal fee = BigDecimal.valueOf(mGasPrice.doubleValue()*mGasLimit.doubleValue());
        BigDecimal feeresult = Convert.fromWei(fee.toString(),Convert.Unit.ETHER);
        tv_fee.setText(feeresult.toPlainString() + " ETH");
    }

    private String getGasPrice(){
        return tv_gas_price.getText().toString();
    }

    private void setGasPrice(String gasPrice){
        mGasPrice = Convert.toWei(gasPrice,Convert.Unit.GWEI).toBigInteger();
    }

    private String getGasLimit() {
        return tv_gas_limit.getText().toString();
    }

    private void setGasLimit(String gasLimit){
        mGasLimit = BigInteger.valueOf(Long.valueOf(gasLimit));
    }

    /*Get Token Info*/
    private void GetTokenInfo(){
        LoadSmartContract loadSmartContract = new LoadSmartContract(mWeb3j,mCredentials,mSmartcontract,mGasPrice,mGasLimit);
        loadSmartContract.registerCallBack(this);
        loadSmartContract.LoadToken();
    }

    /* Get Token*/
    @Override
    public void backLoadSmartContract(Map<String,String> result) {
            setTokenBalance(result.get("tokenbalance"));
            setTokenName(result.get("tokenname"));
            setTokenSymbol(result.get("tokensymbol"));
            setTokenAddress(result.get("tokenaddress"));
            setTokenSupply(result.get("totalsupply"));
    }

    private void setTokenBalance(String value){
        tokenbalance.setText(value);
    }

    private void setTokenName(String value){
        tokenname.setText(value);
    }

    private void setTokenSymbol(String value){
        tokensymbol.setText(value);
    }

    private void setTokenSupply(String value){
        tokensupply.setText(value);
    }

    private void setTokenAddress(String value){
        tokenaddress.setText(value);
    }
    /* End Get Token*/

    /* Sending */
    private void sendEther(){
        sendingEther = new SendingEther(mWeb3j,
                mCredentials,
                getGasPrice(),
                getGasLimit());
        sendingEther.registerCallBack(this);
        sendingEther.Send(getToAddress(),getSendEtherAmmount());
    }

    @Override
    public void backSendEthereum(EthSendTransaction result) {
        toastMsg.Long(this,result.getTransactionHash());
        LoadWallet();
    }

    private void sendToken(){
        sendingToken = new SendingToken(mWeb3j,
                mCredentials,
                getGasPrice(),
                getGasLimit());
        sendingToken.registerCallBackToken(this);
        sendingToken.Send(mSmartcontract,getToAddress(),getSendTokenAmmount());
    }

    @Override
    public void backSendToken(TransactionReceipt result) {
        toastMsg.Long(this,result.getTransactionHash());
        LoadWallet();
    }
    /* End Sending */

    /* QR Scan */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                toastMsg.Short(this, "Result Not Found");
            } else {
                setToAddress(result.getContents());
                toastMsg.Short(this, result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /* End Q Scan */

    /* SeekBar Listener */
    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerGL = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            GetGasLimit(String.valueOf(seekBar.getProgress()*1000+42000));
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    };
    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerGP = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            GetGasPrice(String.valueOf(seekBar.getProgress()+12));
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    public void GetGasLimit(String value) {
        tv_gas_limit.setText(value);
        GetFee();
    }
    public void GetGasPrice(String value) {
        tv_gas_price.setText(value);
        GetFee();
    }


    /* End SeekBar Listener */
}
