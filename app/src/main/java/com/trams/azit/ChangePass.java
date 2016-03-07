package com.trams.azit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * Created by Administrator on 2015-10-23.
 */
public class ChangePass extends ConnActivity {

    ImageView back;
    TextView done;
    EditText current_pass, new_pass, check_new_pass;
    SharedPreferences myPrefs;
    String secret, user_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        myPrefs = getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        setButton();
        setBackBtn();
        setDone();
    }

    private void setDone() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_pass.getText().toString().equals("")) {
                    Toast.makeText(ChangePass.this, "현재 비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (new_pass.getText().toString().equals("") || !new_pass.getText().toString().equals(check_new_pass.getText().toString())) {
                        Toast.makeText(ChangePass.this, "새로운 비밀번호를 다시 확인 해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("secret", secret);
                            jsonObject.put("user_id", user_id);
                            jsonObject.put("exist_password", current_pass.getText().toString());
                            jsonObject.put("new_password", new_pass.getText().toString());

//                            requestJson(Url_define.Student_Change_Pass + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                            requestJson(Url_define.CHANGE_PASS + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Log.d("statusCode : ", statusCode + "");
                                    Log.d("response : ", response.toString());
                                    super.onSuccess(statusCode, headers, response);

                                    try {
                                        if (response.getString("result").equals("success")) {
                                            Toast.makeText(ChangePass.this, "새로운 비밀번호로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ChangePass.this, "현재 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
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
                            e.getStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void setButton() {
        done = (TextView) findViewById(R.id.done);
        current_pass = (EditText) findViewById(R.id.et_current_pass);
        new_pass = (EditText) findViewById(R.id.et_new_pass);
        check_new_pass = (EditText) findViewById(R.id.et_check_new_pass);
    }

    private void setBackBtn() {
        back = (ImageView) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
