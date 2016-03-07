package com.trams.azit.activity.register.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.common.Constants;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sonnv on 1/12/2016.
 */
public class JoinStudent1Activity extends ConnActivity {

    private TextView tvGuideEmail, tvGuidePassword;
    private EditText edEmail, edPassword, edPasswordConfirm;
    private Button btGotoNext;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join1);

        initUI();
    }

    public void initUI() {
        tvGuideEmail = (TextView) findViewById(R.id.tvGuideEmail);
        tvGuidePassword = (TextView) findViewById(R.id.tvGuidePassword);

        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edPasswordConfirm = (EditText) findViewById(R.id.edPasswordConfirm);
        btGotoNext = (Button) findViewById(R.id.btGotoNext);

        tvGuideEmail.setVisibility(View.GONE);
        tvGuidePassword.setVisibility(View.GONE);

    }

    public void gotoBack(View view) {
        finish();
    }

    public void gotoNext(View view) {
        if (validateData()) {
            confirmEmail();
        }
    }

    public void confirmEmail() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", edEmail.getText().toString());

            requestJson(Url_define.BASE + "/api/email/check" + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))) {
                            tvGuideEmail.setVisibility(View.VISIBLE);
                        } else {
                            tvGuideEmail.setVisibility(View.GONE);

                            Intent intent = new Intent(JoinStudent1Activity.this, JoinStudent2Activity.class);
                            intent.putExtra(Constants.EMAIL_KEY, edEmail.getText().toString().trim());
                            intent.putExtra(Constants.PASSWORD_KEY, edPassword.getText().toString().trim());
                            startActivity(intent);
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

    public boolean checkMatchPassword() {
        String password = edPassword.getText().toString().trim();
        String passwordConfirm = edPasswordConfirm.getText().toString().trim();
        if (password.equals("") || passwordConfirm.equals("")) {
            return false;
        }
        if (!password.equals(passwordConfirm)) {
            return false;
        }
        return true;
    }

    private boolean validateData() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String passwordConfirm = edPasswordConfirm.getText().toString().trim();

        if (email.equals("") || password.equals("") || passwordConfirm.equals("")) {
            Toast.makeText(getApplicationContext(), "입력되지 않은 데이터가 있습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            edEmail.setError("이메일 형식에 맞지않습니다.");
            return false;
        }

        if (!checkMatchPassword()) {
            tvGuidePassword.setVisibility(View.VISIBLE);
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8) {
            edPassword.setError("비밀번호는 8자 이상입니다.");
            return false;
        }

        tvGuidePassword.setVisibility(View.GONE);
        return true;
    }

    /**
     * validate email form
     *
     * @param target
     * @return
     */
    public boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else if (!target.contains("@")) {
            return false;
        } else {
            return true;
        }

    }

}
