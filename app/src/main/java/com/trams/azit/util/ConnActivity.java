package com.trams.azit.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-10-30.
 */
public class ConnActivity extends FragmentActivity {

    private static final String TAG = ConnActivity.class.getName();

    public class ConnHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            Log.d(TAG, "ConnActivity statusCode , statusCode : " + statusCode);
            Log.d(TAG, "ConnActivity onSuccess , response : " + response);
            Log.d(TAG, "ConnActivity onSuccess , headers : " + headers);

            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            Log.d(TAG, "ConnActivity statusCode , statusCode : " + statusCode);
            Log.d(TAG, "ConnActivity onSuccess , response : " + response);
            Log.d(TAG, "ConnActivity onSuccess , headers : " + headers);

            if (pd != null) {
                pd.dismiss();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
            if (pd != null) {
                pd.dismiss();
            }

            Log.d(TAG, "ConnActivity statusCode : " + statusCode);
            Log.d(TAG, "ConnActivity onFailure1 : " + t.getMessage());
            Log.d(TAG, "ConnActivity onSuccess , headers : " + headers);

            t.getStackTrace();

        }
    }

    public static class ConnHttpResponseHandlers extends JsonHttpResponseHandler {
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

    private MyProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        pd = MyProgressDialog.show(this, "", "", true, false, null);

    }

    @Override
    protected void onDestroy() {
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
        super.onDestroy();

    }

    protected void request(String subUrl, HashMap<String, String> hashParams, ConnHttpResponseHandler receiveHandler) {
        Log.d(TAG, "ConnActivity request start");
        Log.d(TAG, "ConnActivity request , subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity request , hashParams : " + hashParams.toString());

        if (pd != null) {
            pd.show();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        for (Map.Entry<String, String> entry : hashParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }

        client.post(subUrl, params, receiveHandler);

    }

    protected void requestMultipart(String subUrl, JSONObject jsonObj, RequestParams params, ConnHttpResponseHandler receiveHandler) {
        Log.d(TAG, "ConnActivity requestMultipart start");
        Log.d(TAG, "ConnActivity requestMultipart , subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity requestMultipart , jsonObj : " + jsonObj.toString());
        Log.d(TAG, "ConnActivity requestMultipart , params : " + params.toString());

        if (pd != null) {
            pd.show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        params.put("jsonbody", jsonObj.toString());
        client.post(subUrl, params, receiveHandler);
    }

    protected void requestMultipart2(String subUrl, JSONObject jsonObj, RequestParams params, ConnHttpResponseHandler receiveHandler) {
        Log.d(TAG, "ConnActivity requestMultipart start");
        Log.d(TAG, "ConnActivity requestMultipart , subUrl : " + subUrl);
        Log.d(TAG, "ConnActivity requestMultipart , jsonObj : " + jsonObj.toString());
        Log.d(TAG, "ConnActivity requestMultipart , params : " + params.toString());

        if (pd != null) {
            pd.show();
        }

        StringEntity entity = null;
        entity = new StringEntity(jsonObj.toString(), "UTF-8");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(15000);
        client.setResponseTimeout(15000);
        client.setConnectTimeout(15000);



        params.put("jsonbody", entity);
        client.post(subUrl, params, receiveHandler);
    }

    protected void requestJson(String subUrl, JSONObject jsonObj, ConnHttpResponseHandler receiveHandler) {
        if (pd != null) {
            pd.show();
        }

        Log.d(TAG, "ConnActivity requestJson , url : " + subUrl);
        Log.d(TAG, "ConnActivity requestJson , jsonObj : " + jsonObj.toString());

        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        entity = new StringEntity(jsonObj.toString(), "UTF-8");

        client.setTimeout(15000);
        client.setResponseTimeout(15000);
        client.setConnectTimeout(15000);


        client.post(this, subUrl, entity, "application/json; charset=utf-8", receiveHandler);
    }

    protected static void requestTimeJson(Context context, String subUrl, JSONObject jsonObj, ConnHttpResponseHandlers receiveHandler) {

        Log.d(TAG, "ConnActivity requestJson , url : " + subUrl);
        Log.d(TAG, "ConnActivity requestJson , jsonObj : " + jsonObj.toString());

        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        entity = new StringEntity(jsonObj.toString(), "UTF-8");

        client.setTimeout(15000);
        client.setResponseTimeout(15000);
        client.setConnectTimeout(15000);


        client.post(context, subUrl, entity, "application/json; charset=utf-8", receiveHandler);
    }

    /**
     * 텍스트뷰가 null인지 아닌지 체크한다.
     *
     * @param text
     * @return boolean
     */
    public boolean textNullcheck(TextView text) {
        boolean flag = false;
        if (text.getText().equals(" ")) text.setText("");
        if (text.getText().toString().equals("") || text.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT);
            flag = true;
        }
        return flag;
    }

    public void startActivityNewTask(Class<?> Clazz) {
        Intent i = new Intent(this, Clazz);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void startActivityNewTask(Class<?> Clazz, Bundle b) {
        Intent i = new Intent(this, Clazz);
        i.putExtras(b);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
