package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.RegiChoice_;
import com.trams.azit.Teacher_Main_;
import com.trams.azit.common.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.orhanobut.logger.Logger;
import com.trams.azit.activity.MentoMainActivity;
import com.trams.azit.gcm.RegistrationIntentService;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.BackPressCloseHandler;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EActivity(R.layout.activity_login)
public class LoginActivity extends ConnActivity {

    @ViewById
    LinearLayout main_ll;
    @ViewById
    EditText et_id, et_pw;
    @ViewById
    CheckBox cb_login;
    @ViewById
    TextView login_btn, intro_btn, regi_btn, btn_find_password;

    SharedPreferences myPrefs;

    JSONObject jo = null;
    String str_result, str_role, str_secret, str_user_id, str_email;
    int category = 0;  // 로그인 후 결과값 1 = 학생, 2 = 멘토, 3 = 선생님
    private BackPressCloseHandler backPressCloseHandler;

    private static final String TAG = LoginActivity.class.getName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {
        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        String login = myPrefs.getString("login", "");
        String role = myPrefs.getString("role", "");
        if (login.equals("true")) {
            category = Integer.parseInt(role);
            Log.e("role", role);
            setLoginStatusBtn();
        }
        setEditTextButton();
        backPressCloseHandler = new BackPressCloseHandler(this);

        registerListener();

//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                SharedPreferences sharedPreferences =
//                        PreferenceManager.getDefaultSharedPreferences(context);
//                boolean sentToken = sharedPreferences
//                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
////                if (sentToken) {
////                    mInformationTextView.setText(getString(R.string.gcm_send_message));
////                } else {
////                    mInformationTextView.setText(getString(R.string.token_error_message));
////                }
//            }
//        };

    }

    private void registerListener() {

        // forgot password
        String matcherForgotPassword = btn_find_password.getText().toString();
        SpannableString spannableForgotPassword = new SpannableString(btn_find_password.getText().toString());
        Pattern patternIdForgotPassword = Pattern.compile(matcherForgotPassword);
        Matcher matcherIdMail = patternIdForgotPassword.matcher(btn_find_password.getText().toString());

        while (matcherIdMail.find()) {
            spannableForgotPassword.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "start forgot password");
                    doFindPass();

                }
            }, matcherIdMail.start(), matcherIdMail.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableForgotPassword.setSpan(Typeface.DEFAULT, matcherIdMail.start(), matcherIdMail.end(), 0);
            spannableForgotPassword.setSpan(new ForegroundColorSpan(Color.WHITE), matcherIdMail.start(), matcherIdMail.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        btn_find_password.setText(spannableForgotPassword);
        btn_find_password.setMovementMethod(LinkMovementMethod.getInstance());

        // register user

        String matcher = "가입하기";
        SpannableString spannableRegis = new SpannableString(regi_btn.getText().toString());
        Pattern patternIdRegis = Pattern.compile(matcher);
        Matcher matcherRegister = patternIdRegis.matcher(regi_btn.getText().toString());

        while (matcherRegister.find()) {
            spannableRegis.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "start register");
                    setRegi_btn();
                }
            }, matcherRegister.start(), matcherRegister.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableRegis.setSpan(Typeface.DEFAULT, matcherRegister.start(), matcherRegister.end(), 0);
            spannableRegis.setSpan(new ForegroundColorSpan(Color.WHITE), matcherRegister.start(), matcherRegister.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        regi_btn.setText(spannableRegis);
        regi_btn.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void doFindPass() {
        Intent i = new Intent(LoginActivity.this, FindPasswordActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Click(R.id.main_ll)
    protected void setNain() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main_ll.getWindowToken(), 0);
    }

    private void setEditTextButton() {

        et_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_id.setCompoundDrawables(null, null, null, null);
                    et_id.setHint("");
                } else if (!hasFocus) {
                    if (et_id.getText().length() == 0) {
                        et_id.setHint("E-mail");
                        et_id.setCompoundDrawablesWithIntrinsicBounds(R.drawable.id_icon, 0, 0, 0);
                    }
                }
            }
        });

        et_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_pw.setCompoundDrawables(null, null, null, null);
                    et_pw.setHint("");
                } else if (!hasFocus) {
                    if (et_pw.getText().length() == 0) {
                        et_pw.setHint("비밀번호");
                        et_pw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, 0, 0);
                    }
                }
            }
        });
    }

    private void setLoginStatusBtn() {
        String ss = null;

        ss = myPrefs.getString("email", "");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", ss);
            Log.e("jsonObject", jsonObject.toString());
            requestJson("http://52.192.0.99:2000/typeChk/", jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }



                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());

                    super.onSuccess(statusCode, headers, response);

                    try {
                        Log.e("jsonObject", String.valueOf(response.getJSONObject(0)));

                        jo = response.getJSONObject(0);
                        if (jo.getInt("Type_t") == 1) {

                            if (category == 2 ) {
                                Intent i = new Intent(LoginActivity.this, StudentMainActivity.class);
                                startActivity(i);
                                finish();
                            }

                            else if (category == 4 && jo.getInt("Is_auth") == 1) {
                                Intent i = new Intent(LoginActivity.this, MentoMainActivity.class);
                                startActivity(i);
                                finish();
                            } else if (category == 4 && jo.getInt("Is_auth") == 0) {
                                Intent i = new Intent(LoginActivity.this, Authorization_.class);
                                startActivity(i);
                                finish();
                            }

                            else if (category == 3 && jo.getInt("Is_auth") == 1) {
                                Intent i = new Intent(LoginActivity.this, Teacher_Main_.class);
                                startActivity(i);
                                finish();
                            } else if (category == 3 && jo.getInt("Is_auth") == 0) {
                                Intent i = new Intent(LoginActivity.this, Authorization_.class);
                                startActivity(i);
                                finish();
                            }


                        } else {
                            if (jo.getInt("Type_id") == 2 && category == 2) {
                                Intent i = new Intent(LoginActivity.this, Help_Student.class);
                                startActivity(i);
                                finish();
                            } else if (jo.getInt("Type_id") == 4 && category == 4) {
                                Intent i = new Intent(LoginActivity.this, Help_Mentor.class);
                                startActivity(i);
                                finish();
                            } else if (jo.getInt("Type_id") == 3 && category == 3) {

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.login_btn)
    protected void setLogin_btn() {
        if (et_id.getText().toString().equals("") || et_pw.getText().toString().equals("")) {
            if (et_id.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show();
            } else if (et_pw.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", et_id.getText().toString());
                jsonObject.put("password", et_pw.getText().toString());


                if (PreferUtils.getToken(LoginActivity.this) != null && !PreferUtils.getToken(LoginActivity.this).equals(""))
                    jsonObject.put("token", PreferUtils.getToken(LoginActivity.this));

                requestJson(Url_define.Login + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("statusCode", "로그인 성공잼 ㅎㅎ");
                        Log.d("statusCode : ", statusCode + "");
                        Log.d("response : ", response.toString());
                        afterResponce(response);//파싱파싱
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Log.d("statusCode", "로그인 실패잼 후..");
                        super.onFailure(statusCode, headers, res, t);
                    }
                });
            } catch (JSONException e) {
                e.getStackTrace();
            }
        }
    }

    //    @Click(R.id.regi_btn)

    protected void setRegi_btn() {
        Intent i = new Intent(LoginActivity.this, RegiChoice_.class);
        startActivity(i);
    }

    private void afterResponce(JSONObject msg) {
        try {
            if (msg.equals("")) {
                throw new JSONException("");
            }

            str_result = msg.getString("result");
            str_role = msg.getString("type_id");
            str_secret = msg.getString("secret");
            str_user_id = msg.getString("user_id");
            str_email = et_id.getText().toString();

            Logger.e(str_result);
            Logger.e(str_role);
            Logger.e(str_secret);
            Logger.e(str_user_id);

        } catch (JSONException e) {
//            Toast.makeText(LoginActivity.this, "접속상태를 확인해주세요!", Toast.LENGTH_SHORT).show();
        }
        if (str_result.equals("success")) {

            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("role", str_role);
            editor.putString("secret", str_secret);
            editor.putString("user_id", str_user_id);
            editor.putString("email", str_email);
            Constants.IS_LOGIN = true;
            if (cb_login.isChecked()) {
                editor.putString("login", "true");
            } else {
                editor.putString("login", "false");
            }

            editor.commit();

            //getInstanceIdToken();

            category = Integer.parseInt(str_role);
            setLoginStatusBtn();
        } else {
            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            try {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}