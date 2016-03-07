package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.Teacher_Main_;
import com.orhanobut.logger.Logger;
import com.trams.azit.dialog.PlusStudentDialog;
import com.trams.azit.model.Student;
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

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Administrator on 2015-09-18.
 */
@EActivity(R.layout.activity_plus_student)
public class Plus_Student extends ConnActivity {

    @ViewById
    Button search_student;

    @ViewById
    EditText edt_name, edt_email;
    @ViewById
    TextView edt_school_1;

    Hashtable<String, String> hashTable;

    SharedPreferences myPrefs;
    String secret, user_id;

    @ViewById
    TextView tv_belong_popup, tv_msg_error;

    private BelongModel belongModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Click(R.id.back_btn)
    protected void setBack() {
        Intent i = new Intent(Plus_Student.this, Teacher_Main_.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Plus_Student.this, Teacher_Main_.class);
        startActivity(i);
        finish();
    }

    @AfterViews
    protected void init() {
       /* String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        String[] arrayOfSchoolid = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolid[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        edt_school_1.setAdapter(adapter);

        edt_school_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e(edt_school_1.getText().toString(), hashTable.get(edt_school_1.getText().toString()));
            }
        });*/

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

    }

    @Click(R.id.edt_school_1)
    protected void pickSchool() {
//        FragmentManager fm = getFragmentManager();
        BelongPopupDialog dialogFragment = new BelongPopupDialog(Plus_Student.this);
        dialogFragment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialogFragment.setBelongPopupListener(new BelongPopupDialog.BelongPopupListener() {
            @Override
            public void onComplete(BelongModel _belongModel) {
                belongModel = _belongModel;
                edt_school_1.setText(belongModel.getName());
            }
        });
        dialogFragment.showView();
    }

    @Click(R.id.search_student)
    protected void setSearchBtn() {

        LoadFromServer();

//        Intent i = new Intent(Plus_Student.this, Plus_Student_Pop.class);
////        i.putExtra("name", et_name.getText().toString());
////        i.putExtra("school", hashTable.get(et_school.getText().toString()));
//        i.putExtra("email", edt_email.getText().toString());
//        startActivity(i);

    }
    //request server

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("name", edt_name.getText().toString());
            int edu_inst_id;
            if (belongModel == null ||belongModel.getId().equals("")){
                edu_inst_id = 0;
            }else{
                edu_inst_id = Integer.parseInt(belongModel.getId());
            }
            jsonObject.put("edu_inst_id", edu_inst_id);
            jsonObject.put("email", edt_email.getText().toString());

            Logger.e(secret);
            Logger.e(user_id);

           /* ArrayList<Student> students = new ArrayList<Student>();

            PlusStudentDialog plusStudentDialog = new PlusStudentDialog(Plus_Student.this, students);
            plusStudentDialog.showView();
            plusStudentDialog.setOnCompleteListener(new PlusStudentDialog.OnCompleteListener() {
                @Override
                public void onComple() {
                    Intent i = new Intent(Plus_Student.this, Teacher_Main_.class);
                    startActivity(i);
                    finish();
                }
            });*/

            requestJson(Url_define.Teacher_Search_student + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    try {

                        //get list students
                        // show dialog result
                        ArrayList<Student> students = new ArrayList<Student>();

                        if (response.has("message")) {
                            String message = response.getString("message");
                            if (message.equals("there are not search results")) {
                                Toast.makeText(getBaseContext(), "일치하는 학생이 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        String jsonArrStr = response.getString("student_list");
                        JSONArray jsonArray = new JSONArray(jsonArrStr);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Student student = new Student();
                            student.setId(jsonObject1.getInt("user_id"));
                            student.setName(jsonObject1.getString("name"));
                            student.setEmail(jsonObject1.getString("email"));
                            student.setSchool(jsonObject1.getString("school"));
                            students.add(student);
                        }

                        PlusStudentDialog plusStudentDialog = new PlusStudentDialog(Plus_Student.this, students);
                        plusStudentDialog.showView();
                        plusStudentDialog.setOnCompleteListener(new PlusStudentDialog.OnCompleteListener() {
                            @Override
                            public void onComple() {
                                Intent i = new Intent(Plus_Student.this, Teacher_Main_.class);
                                startActivity(i);
                                finish();
                            }
                        });

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

   /* @Click(R.id.tv_belong_popup)
    protected void openBelongPopup() {
        FragmentManager fm = getFragmentManager();
        BelongPopupDialog dialogFragment = new BelongPopupDialog();
        dialogFragment.setBelongPopupListener(new BelongPopupDialog.BelongPopupListener() {
            @Override
            public void onComplete(BelongModel _belongModel) {
                belongModel = _belongModel;
                tv_belong_popup.setText(belongModel.getName());
            }
        });
        dialogFragment.show(fm, "BelongPopupDialog");
    }*/

}
