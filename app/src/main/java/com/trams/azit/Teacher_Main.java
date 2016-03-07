package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.Plus_Student_;
import com.orhanobut.logger.Logger;
import com.trams.azit.activity.TeacherSettingActivity_;
import com.trams.azit.adapter.MonthAdapter;
import com.trams.azit.adapter.StudentMainTeacherAdapter;
import com.trams.azit.model.ExamModel;
import com.trams.azit.model.Student;
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

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-09-03.
 */
@EActivity(R.layout.activity_teacher_main)
public class Teacher_Main extends ConnActivity {
    private static final String TAG = Teacher_Main.class.getName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @ViewById
    ListView student_list;

    @ViewById
    ImageView plus;

    @ViewById
    ImageView img_setting_teacher;

    @ViewById
    TextView tv_send_msg;

    @ViewById
    EditText edt_msg;

    private BackPressCloseHandler backPressCloseHandler;
    SharedPreferences myPrefs;
    String secret, user_id;
    Boolean showquestion = false;
    LinearLayout question_ll;

    @ViewById
    CheckBox cb_select_all;

    @ViewById
    Spinner sp_month;

    private StudentMainTeacherAdapter studentAdapter;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Student> studentsFilterMonth = new ArrayList<>();

    Boolean Allcheck = false;
    private MonthAdapter monthAdapter;
    private ArrayList<ExamModel> exams = new ArrayList<>();

    @AfterViews
    protected void init() {
        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");
        question_ll = (LinearLayout) findViewById(R.id.question_ll);
        question_ll.setVisibility(View.GONE);
        LoadFromServer();
        backPressCloseHandler = new BackPressCloseHandler(this);

        cb_select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < studentsFilterMonth.size(); i++) {
                    studentsFilterMonth.get(i).setIsSelected(isChecked);
                }
                studentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void refreshExamList() {
        monthAdapter = new MonthAdapter(this, exams);
        sp_month.setAdapter(monthAdapter);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            requestJson(Url_define.Teacher_Main + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Log.e("res", response.toString());
                        if (response.getString("result").equals("success")) {
                            //update example list
                            JSONArray examArr = new JSONArray(response.getString("exam_list"));
                            for (int i = 0; i < examArr.length(); i++) {
                                ExamModel examModel = new ExamModel(examArr.getJSONObject(i).getInt("exam_id"), examArr.getJSONObject(i).getString("title"));
                                exams.add(examModel);
                            }

                            refreshExamList();

                            JSONArray datas = new JSONArray(response.getString("student_list"));
                            Logger.e(datas.toString());
                            for (int i = 0; i < datas.length(); i++) {

                                JSONObject jsonStudent = datas.getJSONObject(i);
                                String schedules = jsonStudent.getString("goal_achieve");
                                String[] arr = schedules.split(",");

                                Student student = new Student();

                                student.setId(jsonStudent.getInt("student_id"));
                                student.setPhoto(jsonStudent.getString("profile_image"));
                                student.setName(jsonStudent.getString("name"));
                                student.setMon(Integer.parseInt(arr[0]));
                                student.setTue(Integer.parseInt(arr[1]));
                                student.setWed(Integer.parseInt(arr[2]));
                                student.setThu(Integer.parseInt(arr[3]));
                                student.setFri(Integer.parseInt(arr[4]));
                                student.setSat(Integer.parseInt(arr[5]));
                                student.setSun(Integer.parseInt(arr[6]));

                                student.setTestDescription("입력완료");

                                ArrayList<ExamModel> listExamsModelStudent = new ArrayList<ExamModel>();

                                JSONArray examsList = new JSONArray(jsonStudent.getString("student_exam"));
                                for (int j = 0; j < examsList.length(); j++) {
                                    ExamModel examModel = new ExamModel();
                                    examModel.setId(examsList.getJSONObject(j).getInt("exam_id"));
                                    examModel.setHasScore(examsList.getJSONObject(j).getBoolean("has_score"));
                                    listExamsModelStudent.add(examModel);
                                }

                                student.setExamModels(listExamsModelStudent);
                                students.add(student);
                            }

                            refreshListView();
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

    private void refreshListView() {
        LogUtils.d(TAG, "refreshListView start");

        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int examId = exams.get(position).getId();

                for (int i = 0; i < students.size(); i++) {
                    students.get(i).setIsSelected(false);
                }

                studentsFilterMonth = new ArrayList<>();

                for (int i = 0; i < students.size(); i++) {
                    ArrayList<ExamModel> listStudentExams = students.get(i).getExamModels();
                    for (int j = 0; j < listStudentExams.size(); j++) {
                        if (listStudentExams.get(j).getId() == examId) {
                            Student s = students.get(i);

                            if (listStudentExams.get(j).isHasScore()) {
                                s.setMockTest("1");
                            } else {
                                s.setMockTest("2");
                            }

                            studentsFilterMonth.add(s);
                        }
                    }
                }

                studentAdapter = new StudentMainTeacherAdapter(Teacher_Main.this, studentsFilterMonth);
                student_list.setAdapter(studentAdapter);
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        student_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student currentStudent = studentsFilterMonth.get(position);
                PreferUtils.setStudentTeacher(Teacher_Main.this, currentStudent.getId());
                Intent i = new Intent(Teacher_Main.this, Record_Student_Study.class);
                i.putExtra("id", currentStudent.getId());
                i.putExtra("image", currentStudent.getPhoto());
                i.putExtra("name", currentStudent.getName());
                i.putExtra("mon", currentStudent.getMon());
                i.putExtra("tue", currentStudent.getTue());
                i.putExtra("wed", currentStudent.getWed());
                i.putExtra("thu", currentStudent.getThu());
                i.putExtra("fri", currentStudent.getFri());
                i.putExtra("sat", currentStudent.getSat());
                i.putExtra("sun", currentStudent.getSun());
                i.putExtra("mock", currentStudent.getMockTest());
                startActivity(i);
            }
        });

    }

    @Click(R.id.plus)
    protected void setPlus() {
        Intent i = new Intent(Teacher_Main.this, Plus_Student_.class);
        startActivity(i);
        finish();
    }

    @Click(R.id.img_setting_teacher)
    protected void openTeacherSetting() {
        Intent i = new Intent(Teacher_Main.this, TeacherSettingActivity_.class);
        startActivity(i);
    }

    @Click(R.id.tv_send_msg)
    protected void sendMessage() {
        LogUtils.d(TAG, "sendMessage start");
        if (edt_msg.getText().toString().equals("")) {
            Toast.makeText(Teacher_Main.this, "메시지를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
        } else {

            try {

                String receiverId = "";

                for (int i = 0; i < studentsFilterMonth.size(); i++) {
                    if (studentsFilterMonth.get(i).isSelected())
                        receiverId = receiverId + studentsFilterMonth.get(i).getId() + ",";
                }

                if (receiverId.length() > 0) {
                    receiverId = receiverId.substring(0, receiverId.length() - 1);
                }

                LogUtils.d(TAG, "sendMessage , receiverId : " + receiverId);

                if (receiverId.length() > 0 && !TextUtils.isEmpty(edt_msg.getText().toString())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("title", edt_msg.getText().toString());
                    jsonObject.put("message", "");
                    jsonObject.put("receiver_id", receiverId);

                    requestJson(Url_define.STUDENT_SEND_MESSAGE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                Toast.makeText(Teacher_Main.this, "선택하신 학생에게 메시지를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                edt_msg.setText("");
                                View view = Teacher_Main.this.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                }
            } catch (JSONException e) {
                e.getStackTrace();
            }
        }
    }

    @Click(R.id.img_question)
    protected void ShowQuestion() {
        if (showquestion) {
            //안보이기
            question_ll.setVisibility(View.GONE);
        } else {
            //보이기
            question_ll.setVisibility(View.VISIBLE);
        }
        showquestion = !showquestion;
    }
}
