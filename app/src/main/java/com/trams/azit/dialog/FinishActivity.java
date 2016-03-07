package com.trams.azit.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.StudentMainActivity;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manh on 2016-01-25.
 */
public class FinishActivity extends ConnActivity {

    ImageView btn_close;
    Button next_btn;
    EditText page_from, page_to;
    SharedPreferences myPrefs;
    int activity_id, activity_type;
    String start_time, end_time, duration, secret, user_id;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.dialog_timefinished);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        myPrefs = getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        activity_id = getIntent().getIntExtra("activity_id", 0);
        activity_type = getIntent().getIntExtra("activity_type", 0);
        start_time = getIntent().getStringExtra("start_time");
        end_time = getIntent().getStringExtra("end_time");
        duration = getIntent().getStringExtra("duration");

        btn_close = (ImageView) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        page_from = (EditText) findViewById(R.id.page_from);
        page_to = (EditText) findViewById(R.id.page_to);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_from.getText().toString().equals("") || page_to.getText().toString().equals("")) {
                    Toast.makeText(FinishActivity.this, "페이지를 설정해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("secret", secret);
                        jsonObject.put("user_id", user_id);
                        jsonObject.put("start_time", start_time);
                        jsonObject.put("end_time", end_time);
                        jsonObject.put("activity_id", activity_id);
                        jsonObject.put("duration", duration);
                        jsonObject.put("activity_type", activity_type);
                        jsonObject.put("start_page", Integer.parseInt(page_from.getText().toString()));
                        jsonObject.put("end_page", Integer.parseInt(page_to.getText().toString()));

                        Log.e("jsonObject", jsonObject.toString());

                        requestJson(Url_define.Student_Send_My_Time + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("statusCode : ", statusCode + "");
                                Log.d("response : ", response.toString());
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (response.getString("result").equals("success")) {
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                Log.d("statusCode : ", statusCode + "");
                                super.onFailure(statusCode, headers, res, t);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
