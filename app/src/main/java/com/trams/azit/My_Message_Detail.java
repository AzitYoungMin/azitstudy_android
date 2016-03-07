package com.trams.azit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manh on 2016-01-26.
 */
public class My_Message_Detail extends ConnActivity {

    SharedPreferences myPrefs;
    String secret, user_id;
    int message_id;
    ImageView back_btn;
    JSONObject jsonObject;
    TextView from_who, title, date, body;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message_detail);

        myPrefs = getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        message_id = getIntent().getIntExtra("message_id", 0);

        back_btn = (ImageView) findViewById(R.id.back_btn);
        from_who = (TextView) findViewById(R.id.from_who);
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        body = (TextView) findViewById(R.id.body);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getMyMessage();
    }

    private void getMyMessage() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("message_id", message_id+"");

            requestJson(Url_define.Common_Get_message + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    Log.d("statusCode", statusCode + "");
                    Log.d("onSuccess res", res.toString());

                    //서버의 데이터 수신이 성공하면
                    try {
                        if (res.getString("result").equals("success")) {
                            from_who.setText(res.getString("name"));
                            title.setText(res.getString("title"));
                            date.setText(res.getString("created_at"));
                            body.setText(res.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {

                    Log.d("statusCode", statusCode + "");
                    Log.d("throwable", String.valueOf(t.getMessage()) == null ? String.valueOf(t.getMessage()) : "");
                    Log.d("onFailure res", res.toString());
                    Log.d("jsonObject : ", jsonObject.toString());
                    Toast.makeText(getApplicationContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
