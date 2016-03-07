package com.trams.azit.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.trams.azit.util.MyProgressDialog;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 11/01/2016.
 */
public class NetworkHelper {
    public static final String TAG = ConnHttpResponseHandler.class.getName();


    public static class ConnHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            Log.d(TAG, "ConnActivity statusCode , statusCode : " + statusCode);
            Log.d(TAG, "ConnActivity onSuccess , response : " + response);
            Log.d(TAG, "ConnActivity onSuccess , headers : " + headers);


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

            Log.d(TAG, "ConnActivity statusCode : " + statusCode);
            Log.d(TAG, "ConnActivity onFailure1 : " + t.getMessage());
            Log.d(TAG, "ConnActivity onSuccess , headers : " + headers);


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {


            Log.d(TAG, "ConnActivity statusCode : " + statusCode);
            Log.d(TAG, "ConnActivity onFailure1 : " + t.getMessage());
            Log.d(TAG, "ConnActivity onSuccess , headers : " + headers);

            t.getStackTrace();

        }
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        pd = MyProgressDialog.show(this,"","",true,false,null);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (pd !=null){
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
//        }
//        super.onDestroy();
//
//    }

    protected void request(String subUrl, HashMap<String, String> hashParams, ConnHttpResponseHandler receiveHandler) {
        Log.d(TAG, "ConnActivity request subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity request hashParams : " + hashParams);


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        for (Map.Entry<String, String> entry : hashParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }

        client.post(subUrl, params, receiveHandler);

    }

    protected void requestMultipart(String subUrl, JSONObject jsonObj, RequestParams params, ConnHttpResponseHandler receiveHandler) {
        Log.d(TAG, "ConnActivity requestMultipart subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity requestMultipart jsonObj : " + jsonObj.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        params.put("jsonbody", jsonObj.toString());
        client.post(subUrl, params, receiveHandler);
    }

    public static void requestJson(Context context, String subUrl, JSONObject jsonObj, ConnHttpResponseHandler receiveHandler) {

        Log.d(TAG, "ConnActivity requestMultipart subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity requestMultipart jsonObj : " + jsonObj.toString());



        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        entity = new StringEntity(jsonObj.toString(), "UTF-8");

        client.setTimeout(15000);
        client.setResponseTimeout(15000);
        client.setConnectTimeout(15000);


        client.post(context, subUrl, entity, "application/json; charset=utf-8", receiveHandler);
    }

}
