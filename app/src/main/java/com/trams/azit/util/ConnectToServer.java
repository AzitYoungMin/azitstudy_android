package com.trams.azit.util;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2015-09-04.
 */
public class ConnectToServer {

    private String url = "";
    private JSONObject JsonOb;
    private Context mContext;
    private OnMessageCallBack onMessageCallBack;

    public ConnectToServer(Context context, String urls, JSONObject parameters) {
        this.url = urls;
        this.mContext = context;
        this.JsonOb = parameters;
    }

    public interface OnMessageCallBack {
        void returnMessage(String message);
    }

    public void setOnMessage(OnMessageCallBack callback) {
        onMessageCallBack = callback;
    }

    public void Responce() {
        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = new StringEntity(JsonOb.toString(), "UTF-8");

        client.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here

                onMessageCallBack.returnMessage(response.toString());
                Log.e("test", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                onMessageCallBack.returnMessage("");
                Log.e("test", "fail");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                onMessageCallBack.returnMessage(res.toString());
                Log.e("test", "fail");
            }
        });


    }
}
