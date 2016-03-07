package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.Regi_student_detail_;
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
@EActivity(R.layout.activity_regi_basic)
public class Regi_student extends ConnActivity {

    @ViewById
    TextView man, woman, my_grade;
    @ViewById
    AutoCompleteTextView et_school;

    @ViewById
    EditText et_name, et_nick, et_email, et_pwd, et_pwd_check, et_phone;

    String genderFlag;
    Hashtable<String, String> hashTable;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
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
        et_school.setAdapter(adapter);

        et_school.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e(et_school.getText().toString(), hashTable.get(et_school.getText().toString()));
            }
        });

    }


    @Click(R.id.confirm_nick)
    protected void setConfirm_nick() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickname", et_nick.getText().toString());

            requestJson(Url_define.BASE + "/api/nickname/check" + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))){
                            Toast.makeText(Regi_student.this, "해당 닉네임이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Regi_student.this, "해당 닉네임은  사용 가능 합니다.", Toast.LENGTH_SHORT).show();
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

    @Click(R.id.confirm_email)
    protected void setCnfirm_email(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", et_email.getText().toString());

            requestJson(Url_define.BASE + "/api/email/check" + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))){
                            Toast.makeText(Regi_student.this, "해당 이메일이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Regi_student.this, "해당 이메일은  사용 가능 합니다.", Toast.LENGTH_SHORT).show();
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

    @Click(R.id.my_grade)
    protected void setMy_grade() {
        final CharSequence[] items = {"고1", "고2", "고3", "재도전"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("학년 선택");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                my_grade.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Click(R.id.man)
    protected void setMan() {
        man.setBackgroundResource(R.drawable.man_push);
        woman.setBackgroundResource(R.drawable.woman);
        genderFlag = "M";
        Log.d("gender", genderFlag);
    }

    @Click(R.id.woman)
    protected void setWoman() {
        man.setBackgroundResource(R.drawable.man);
        woman.setBackgroundResource(R.drawable.woman_push);
        genderFlag = "F";
        Log.d("gender", genderFlag);
    }

    /**
     * 텍스트 null인지 아닌지 체크한다.
     *
     * @param text
     * @return boolean
     */
    public boolean textNullcheck(TextView text) {
        boolean flag = false;
        if (text.getText().equals(" ")) text.setText("");
        if (text.getText().toString().equals("") || text.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT);
            flag = true;
        }
        return flag;
    }

    @Click(R.id.btnComplete)
    protected void setComplete() {
        Log.d("complete", "complete");
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String pwd = et_pwd.getText().toString();
        if (textNullcheck(et_name) == true || textNullcheck(et_email) == true ||
                textNullcheck(et_pwd) == true || textNullcheck(et_nick) == true ||
                textNullcheck(et_school) == true || textNullcheck(et_pwd_check) == true || textNullcheck(my_grade) == true) {
            Toast.makeText(getApplicationContext(), "입력되지 않은 데이터가 있습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            et_name.requestFocus();
            return;
        }else if (!(et_pwd_check.getText().toString().equals(et_pwd.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "비밀번호와 확인칸이 일치하지않습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            et_name.requestFocus();
        }else{
            String year = my_grade.getText().toString();
            int yearCode = 1;
            if (year.equals("고1")) yearCode = 1;
            else if (year.equals("고2")) yearCode = 2;
            else if (year.equals("고3")) yearCode = 3;
            else if (year.equals("재도전")) yearCode = 0;
            Log.d("year", yearCode+"");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", et_name.getText().toString());
                jsonObject.put("email", et_email.getText().toString());
                jsonObject.put("nickname", et_nick.getText().toString());
                jsonObject.put("password", et_pwd.getText().toString());
                jsonObject.put("edu_inst_id", hashTable.get(et_school.getText().toString()));//추후 학교코드로 변경해야함!
                jsonObject.put("phone", et_phone.getText().toString());
                jsonObject.put("year", yearCode);
                jsonObject.put("gender", genderFlag);

                if (jsonObject.has("edu_inst_id")) {
                    Intent i = new Intent(Regi_student.this, Regi_student_detail_.class);
                    Log.d("putput", jsonObject.toString());
                    i.putExtra("studentData", jsonObject.toString());
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "학교가 잘못 선택 되엇습니다.",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.getStackTrace();
            }
        }

    }

}
