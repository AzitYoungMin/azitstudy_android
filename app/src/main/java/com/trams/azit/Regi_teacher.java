package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.LoginActivity_;
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

import java.util.Hashtable;

/**
 * Created by Administrator on 2015-09-02.
 */
@EActivity(R.layout.activity_regi_teacher)
public class Regi_teacher extends ConnActivity {

    @ViewById
    CheckBox cb_school, cb_institude, cb_enroll, cb_grad;
    @ViewById
    EditText name, year, month, day, email, password, conf_password, phone, school;
    @ViewById
    TextView man, women;
    @ViewById
    AutoCompleteTextView belong;

    String genderFlag, classification;
    private JSONObject jsonObject;
    private String is_graduated;
    Hashtable<String, String> hashTable;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {

        String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        String[] arrayOfSchoolid = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolid[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        belong.setAdapter(adapter);

        belong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e(belong.getText().toString(), hashTable.get(belong.getText().toString()));
            }
        });

        classification = "A";
        jsonObject = new JSONObject();
    }

    @Click(R.id.cb_school)
    protected void setCbSchool() {
        if (cb_school.isChecked()) {
            classification = "A";
        } else {
            classification = "B";
        }
    }

    @Click(R.id.cb_grad)
    protected void isGraduated() {
        if (cb_grad.isChecked())
            is_graduated = "true";
        else
            is_graduated = "false";
    }
    @Click(R.id.cb_enroll)
    protected void isCbenroll(){
        if (cb_enroll.isChecked())
            is_graduated = "false";
        else
            is_graduated = "true";
    }

    @Click(R.id.cb_institude)
    protected void setcbInstitude() {
        if (cb_institude.isChecked()) {
            classification = "B";
        } else {
            classification = "A";
        }
    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

    @Click(R.id.man)
    protected void setMan() {
        man.setBackgroundResource(R.drawable.man_push);
        women.setBackgroundResource(R.drawable.woman);
        genderFlag = "M";
        Log.d("gender", genderFlag);
    }

    @Click(R.id.women)
    protected void setWomen() {
        man.setBackgroundResource(R.drawable.man);
        women.setBackgroundResource(R.drawable.woman_push);
        genderFlag = "F";
        Log.d("gender", genderFlag);
    }

    @Click(R.id.btnComplete)
    protected void setEnter() {
        if (!(password.getText().toString().equals(conf_password.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "비밀번호와 확인칸이 일치하지않습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        }else{
            Log.d("birthday", year.getText().toString() + "." + month.getText().toString() + "." + day.getText().toString());
            try {
                jsonObject.put("password",password.getText().toString());
                jsonObject.put("gender", genderFlag);
                jsonObject.put("name", name.getText().toString());
                jsonObject.put("email", email.getText().toString());
                jsonObject.put("phone", phone.getText().toString());
                jsonObject.put("edu_inst_id", hashTable.get(belong.getText().toString()));
                jsonObject.put("birthday", year.getText().toString() + "." + month.getText().toString() + "." + day.getText().toString());
                jsonObject.put("last_school", school.getText().toString());
                jsonObject.put("is_graduated",is_graduated);
                jsonObject.put("classification", classification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("beforejsonObject : ", jsonObject.toString());

            if (jsonObject.has("edu_inst_id")) {
                requestJson(Url_define.BASE + "/api/teacher/signup" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        Log.d("statusCode", statusCode + "");
                        Log.d("onSuccess res", res.toString());
                        try {
                            String str = (String) res.get("message");
                            if (str.equals("duplicated email")) {
                                Toast.makeText(getApplicationContext(), "중복된 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
                            } else if (!res.get("result").toString().equals("fail")) {
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Regi_teacher.this, LoginActivity_.class);
                                startActivity(i);
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
                        Log.d("throwable", String.valueOf(t.getMessage())==null? String.valueOf(t.getMessage()) : "");
                        Log.d("onFailure res", res.toString());
                        Log.d("jsonObject : ", jsonObject.toString());
                        try {
                            if (res.get("message").equals("invalid body")) {
                                Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }else{
                Toast.makeText(getApplicationContext(), "학교가 잘못 선택 되엇습니다.",Toast.LENGTH_SHORT).show();
            }


        }


    }

}
