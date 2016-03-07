package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.RegisterTeacherNextActivity_;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-02.
 */
@EActivity(R.layout.activity_register_teacher)
public class RegisterTeacherActivity extends ConnActivity {

//    @ViewById
//    CheckBox cb_school, cb_institude;

    @ViewById
    RadioGroup rd_teacher;

    @ViewById
    EditText edt_email, edt_password, edt_password_confirm;

    @ViewById
    TextView tv_msg_email, tv_msg_password;

    @ViewById
    Button btGotoNext;

    String classification;

    public static final String TEACHER_TYPE_EXTRA = "teacher_type";
    public static final String TEACHER_EMAIL_EXTRA = "teacher_email";
    public static final String TEACHER_PASS_EXTRA = "teacher_pass";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {
        tv_msg_email.setVisibility(View.GONE);
        tv_msg_password.setVisibility(View.GONE);
    }

    public boolean checkMatchPassword(){
        String password = edt_password.getText().toString().trim();
        String passwordConfirm = edt_password_confirm.getText().toString().trim();
        if (password.equals("") || passwordConfirm.equals("")){
            return false;
        }
        if (!password.equals(passwordConfirm)){
            return false;
        }
        return true;
    }

    public  boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else if (!target.contains("@")) {
            return false;
        } else {
            return true;
        }
    }

    protected void confirmEmail() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", edt_email.getText().toString());

            requestJson(Url_define.BASE + "/api/email/check" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))) {
                            tv_msg_email.setVisibility(View.VISIBLE);
                        } else {
                            tv_msg_email.setVisibility(View.GONE);
                            doRegister();
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

    private boolean validateData(){
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String passwordConfirm = edt_password_confirm.getText().toString().trim();

        if (email.equals("") || password.equals("") || passwordConfirm.equals("")){
            Toast.makeText(getApplicationContext(), "입력되지 않은 데이터가 있습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)){
            edt_email.setError("이메일 형식에 맞지않습니다.");
            return false;
        }

        if (!checkMatchPassword()){
            tv_msg_password.setVisibility(View.VISIBLE);
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8) {
            edt_password.setError("비밀번호는 8자 이상입니다.");
            return false;
        }

        tv_msg_password.setVisibility(View.GONE);
        return true;
    }

    @Click(R.id.btGotoNext)
    protected void setBtn_register() {
        if (validateData()){
            confirmEmail();
        }
    }

    private void doRegister() {
        try {

            if (rd_teacher.getCheckedRadioButtonId() == R.id.rd_school) {
                classification = "A";
            } else if (rd_teacher.getCheckedRadioButtonId() == R.id.rd_academy) {
                classification = "B";
            }

            Intent intent = new Intent(RegisterTeacherActivity.this, RegisterTeacherNextActivity_.class);

            intent.putExtra(TEACHER_TYPE_EXTRA, classification);
            intent.putExtra(TEACHER_EMAIL_EXTRA, edt_email.getText().toString());
            intent.putExtra(TEACHER_PASS_EXTRA, edt_password.getText().toString());

            startActivity(intent);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

}
