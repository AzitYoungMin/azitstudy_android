package com.trams.azit.activity.register.student;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.StudentMainActivity;
import com.trams.azit.adapter.StudentJoin3Adapter;
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
public class JoinStudent3Activity extends ConnActivity {
    private Button btLiberalArt, btNaturalScience;
    private ViewPager viewPager;

    private String emailValue, passwordValue,nameValue, nicknameValue, phoneValue, parent_nameValue, parent_phoneValue, edu_inst_idValue, genderValue;
    private int yearValue, departmentValue, math_typeValue,foreign_languageValue = 0;
    private boolean mp_educationValue = false;
    private String  optional_subjectsValue = "", target_departmentValue="";
    private JSONObject jsonObject;
    
    SharedPreferences myPrefs;
    RegisterReceiver receiver;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join3);

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            emailValue = extra.getString(Constants.EMAIL_KEY);
            passwordValue = extra.getString(Constants.PASSWORD_KEY);

            nameValue = extra.getString(Constants.NAME_KEY);
            nicknameValue = extra.getString(Constants.NICKNAME_KEY);
            phoneValue = extra.getString(Constants.PHONE_KEY);
            parent_nameValue = extra.getString(Constants.PARENT_NAME_KEY);
            parent_phoneValue = extra.getString(Constants.PARENT_PHONE_KEY);

            edu_inst_idValue = extra.getString(Constants.EDU_INST_ID_KEY);
            genderValue = extra.getString(Constants.GENDER_KEY);
            yearValue = extra.getInt(Constants.YEAR_KEY);

            jsonObject = new JSONObject();

        }
        initUI();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_REGISTER_LIBERAL_STUDENT);
        filter.addAction(Constants.ACTION_REGISTER_NATURAL_STUDENT);
        receiver = new RegisterReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initUI(){
        btLiberalArt = (Button)findViewById(R.id.btLiberalArt);
        btNaturalScience = (Button)findViewById(R.id.btNaturalScience);

        btLiberalArt.setBackgroundColor(getResources().getColor(R.color.white));
        btLiberalArt.setTextColor(getResources().getColor(R.color.azit_green_bg));
        btNaturalScience.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
        btNaturalScience.setTextColor(getResources().getColor(R.color.white));

        viewPager = (ViewPager) findViewById(R.id.pagerStudentJoin3);
        final StudentJoin3Adapter adapter = new StudentJoin3Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    public void gotoLiberalArt(View view){
        setUILiberalArt();
        viewPager.setCurrentItem(0);
    }

    public void gotoNaturalScience(View view){
        setUINaturalScience();
        viewPager.setCurrentItem(1);
    }

    private void setUILiberalArt(){
        btLiberalArt.setBackgroundColor(getResources().getColor(R.color.white));
        btLiberalArt.setTextColor(getResources().getColor(R.color.azit_green_bg));
        btNaturalScience.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
        btNaturalScience.setTextColor(getResources().getColor(R.color.white));
    }

    private void setUINaturalScience(){
        btLiberalArt.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
        btLiberalArt.setTextColor(getResources().getColor(R.color.white));
        btNaturalScience.setBackgroundColor(getResources().getColor(R.color.white));
        btNaturalScience.setTextColor(getResources().getColor(R.color.azit_green_bg));
    }

    public void gotoBack(View view){
        finish();
    }

    private void gotoRegisterForStudent(){

        requestJson(Url_define.BASE + "/api/student/signup" + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", res.toString());
                try {
                    String str = (String) res.get("message");
                    if (str.equals("duplicated email")) {
                        Toast.makeText(JoinStudent3Activity.this, "중복된 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
                    } else if (!res.get("result").toString().equals("fail")) {
                        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.putString("secret", res.getString("secret"));
                        editor.putString("user_id", res.getString("user_id"));
                        editor.commit();
                        Intent intent = new Intent(JoinStudent3Activity.this, StudentMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("register", true);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                Log.d("statusCode", statusCode + "");
                Log.d("throwable", String.valueOf(t.getMessage()));
                Log.d("onFailure res", res.toString());
            }

        });
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position==0){
                setUILiberalArt();
            }else {
                setUINaturalScience();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class RegisterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extra = intent.getExtras();
            if (extra!= null){
                intent.putExtra(Constants.DEPARTMENT_KEY, 1);
                intent.putExtra(Constants.MATH_TYPE_KEY, math_typeValue);
                intent.putExtra(Constants.OPTIONAL_SUBJECTS_KEY, optional_subjectsValue);
                intent.putExtra(Constants.FOREIGN_LANGUAGE_KEY, foreign_languageValue);
                intent.putExtra(Constants.MP_EDUCATION_KEY, mp_educationValue);
                intent.putExtra(Constants.TARGET_DEPARTMENTS_KEY, target_departmentValue);

                departmentValue = extra.getInt(Constants.DEPARTMENT_KEY);
                math_typeValue = extra.getInt(Constants.MATH_TYPE_KEY);
                optional_subjectsValue = extra.getString(Constants.OPTIONAL_SUBJECTS_KEY);
                foreign_languageValue = extra.getInt(Constants.FOREIGN_LANGUAGE_KEY);
                mp_educationValue = extra.getBoolean(Constants.MP_EDUCATION_KEY);
                target_departmentValue = extra.getString(Constants.TARGET_DEPARTMENTS_KEY);

                try {
                    jsonObject.put("email", emailValue);
                    jsonObject.put("password", passwordValue);
                    jsonObject.put("name", nameValue);
                    jsonObject.put("nickname", nicknameValue);
                    jsonObject.put("phone", phoneValue);
                    jsonObject.put("edu_inst_id", edu_inst_idValue);
                    jsonObject.put("year", yearValue);
                    jsonObject.put("gender", genderValue);
                    jsonObject.put("parent_name", parent_nameValue);
                    jsonObject.put("parent_phone", parent_phoneValue);
                    jsonObject.put("department", departmentValue);
                    jsonObject.put("math_type", math_typeValue);
                    jsonObject.put("optional_subjects", optional_subjectsValue);
                    jsonObject.put("foreign_language", foreign_languageValue);
                    jsonObject.put("mp_education", mp_educationValue);
                    jsonObject.put("target_departments", target_departmentValue);

                    gotoRegisterForStudent();

                }catch (Exception e){

                }
            }
        }
    }
}
