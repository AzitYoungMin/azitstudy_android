package com.trams.azit.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-11-03.
 */
public class ConnFragment extends Fragment{

    private static final String TAG = ConnFragment.class.getName();

    public class ConnHttpResponseHandler extends JsonHttpResponseHandler {
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



    protected void request(String subUrl, HashMap<String, String> hashParams, ConnHttpResponseHandler receiveHandler) {
        Log.d(TAG, "ConnActivity subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity  hashParams : " + hashParams.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        for (Map.Entry<String, String> entry : hashParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }

        client.post(subUrl, params, receiveHandler);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    protected void requestJson(String subUrl, JSONObject jsonObj, ConnHttpResponseHandler receiveHandler) {

        Log.d(TAG, "ConnActivity subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity jsonObj : " + jsonObj.toString());



        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        entity = new StringEntity(jsonObj.toString(), "UTF-8");

        client.setTimeout(15000);
        client.setResponseTimeout(15000);
        client.setConnectTimeout(15000);
        client.post(getContext(), subUrl, entity, "application/json", receiveHandler);
    }
}
