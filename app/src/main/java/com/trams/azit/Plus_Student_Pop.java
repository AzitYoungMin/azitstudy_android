package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.trams.azit.Teacher_Main_;
import com.orhanobut.logger.Logger;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-11-02.
 */
public class Plus_Student_Pop extends ConnActivity {

    String name, school, email, student_id;
    SharedPreferences myPrefs;
    String secret, user_id;
    TextView tv_name, tv_school, tv_email, confirm, cancel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.plus_student_pop);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

//        name = getIntent().getStringExtra("name");
//        school = getIntent().getStringExtra("school");
        email = getIntent().getStringExtra("email");

        LoadFromServer();
        setButton();

    }

    private void setButton() {
        confirm = (TextView) findViewById(R.id.confirm);
        cancel = (TextView) findViewById(R.id.cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
                Intent i = new Intent(Plus_Student_Pop.this, Teacher_Main_.class);
                startActivity(i);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_name = (TextView)findViewById(R.id.name);
        tv_school= (TextView)findViewById(R.id.grade);
        tv_email= (TextView)findViewById(R.id.email);

    }

    private void addStudent() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("student_id", student_id);

            Logger.e(secret);
            Logger.e(user_id);

            requestJson(Url_define.Teacher_add_student + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("name", "");
            jsonObject.put("edu_inst_id", "0");
            jsonObject.put("phone", email);

            Logger.e(secret);
            Logger.e(user_id);

            requestJson(Url_define.Teacher_Search_student + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {

                        tv_name.setText(response.getString("name"));
                        tv_school.setText(response.getString("school"));
                        tv_email.setText(response.getString("email"));
                        student_id = response.getString("user_id");

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
            e.getStackTrace();
        }
    }


}
