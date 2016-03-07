package com.trams.azit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.dialog.FinishActivity;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 04/01/2016.
 */
public class FindPasswordActivity extends ConnActivity implements View.OnClickListener {

    private static final String TAG = FindPasswordActivity.class.getName();
    private TextView tvMsgResponse;
    private Button btnFindPass;
    private EditText edtEmail;
    private ImageView imgBack;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);

        tvMsgResponse = (TextView) findViewById(R.id.tv_msg_response);
        edtEmail = (EditText) findViewById(R.id.tv_email_find_pass);

        btnFindPass = (Button) findViewById(R.id.btn_find_pass);
        btnFindPass.setOnClickListener(this);

        imgBack = (ImageView) findViewById(R.id.back_btn);
        imgBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_find_pass:
                if (edtEmail.getText().toString().contains("@") && edtEmail.getText().toString().length() > 0) {
                    tvMsgResponse.setText("유효한 이메일 주소입니다.");
                    tvMsgResponse.setTextColor(Color.GREEN);
                    doFindPass();

                } else {
                    tvMsgResponse.setText("유효하지 않은 이메일 형식입니다.");
                    tvMsgResponse.setTextColor(Color.RED);
                }
                break;

            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void doFindPass() {
        LogUtils.d(TAG, "doFindPass start");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", edtEmail.getText().toString());

            requestJson(Url_define.Find_Pass + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.getString("result").equals("success")) {
                            Toast.makeText(FindPasswordActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FindPasswordActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
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
