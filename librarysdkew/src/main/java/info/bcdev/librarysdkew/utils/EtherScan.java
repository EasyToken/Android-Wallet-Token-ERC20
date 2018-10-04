package info.bcdev.librarysdkew.utils;

import android.os.AsyncTask;

import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import info.bcdev.librarysdkew.interfaces.callback.CBEtherScan;

public class EtherScan {

    CBEtherScan cbEtherScan;
    private String mUrl = "https://api.etherscan.io/api";
    private String mApiKey;
    Map<String,String> actionparams;
    String action;

    public EtherScan(String apikey){
    mApiKey = apikey;
    }

    public void getBalance(String address){
        action = "balance";
        Map<String,String> params = new HashMap<>();
        params.put("module","account");
        params.put("action","balance");
        params.put("address",address);
        params.put("tag","latest");
        params.put("apikey",mApiKey);
        new RequestES().execute(params);
    }

    private class RequestES extends AsyncTask<Map<String, String>, Void, Object> {

        @Override
        protected Object doInBackground(Map<String,String>... params) {

            String urlrequest = mUrl+"?";

            for(Map.Entry<String,String> entry : params[0].entrySet()){
                urlrequest+=entry.getKey()+"="+entry.getValue()+"&";
            }
            System.out.println(urlrequest);
            try {
                URL urletherscan = new URL(urlrequest);
                HttpURLConnection etherscan = (HttpURLConnection) urletherscan.openConnection();
                etherscan.setDoOutput(true);
                etherscan.setDoInput(true);
                etherscan.setRequestMethod("GET");
                etherscan.setRequestProperty("Content-Type", "application/json");
                InputStreamReader inputStreamReader = new InputStreamReader(etherscan.getInputStream());
                JsonParser jsonParser = new JsonParser();

                Object object = jsonParser.parse(inputStreamReader);

                inputStreamReader.close();

                return object;
            } catch(Exception ex){System.out.println("EtherScan get Hash: " + ex);}
            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            if (object != null){
            cbEtherScan.backEtherScan(action, object);
            }
        }
    }

    public void registerCallBack(CBEtherScan cbEtherScan){
        this.cbEtherScan = cbEtherScan;
    }

}
