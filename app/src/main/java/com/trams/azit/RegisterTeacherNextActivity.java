package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.trams.azit.LoginActivity_;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.trams.azit.view.WheelViewDate;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015-09-02.
 */
@EActivity(R.layout.activity_register_teacher_next)
public class RegisterTeacherNextActivity extends ConnActivity {


    @ViewById
    EditText edt_name, edt_phone, edt_education;

    @ViewById
    static TextView edt_year, edt_month, edt_day;

    @ViewById
    RadioGroup rdg_education;

    @ViewById
    TextView tv_belong_popup, tv_msg_error;

    @ViewById
    Button btn_register, btn_male, btn_female;

    private String genderFlag = "";
    private String is_graduated;
    private Calendar calendar;
    private BelongModel belongModel;
    private String classification;
    private String email;
    private String password;
    int yearMin, yearMax, yearCount, yearResult;
    int dayMin, dayMax, dayCount, dayResult;
    List<String> yearList = new ArrayList<String>();
    List<String> dayList = new ArrayList<String>();
    String months[] = new String[]{"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};

    @ViewById
    ImageView img_pick_birthday;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {
        Intent intent = getIntent();

        classification = intent.getStringExtra(RegisterTeacherActivity.TEACHER_TYPE_EXTRA);
        email = intent.getStringExtra(RegisterTeacherActivity.TEACHER_EMAIL_EXTRA);
        password = intent.getStringExtra(RegisterTeacherActivity.TEACHER_PASS_EXTRA);

    }

    public void showDatePickerDialog() {
        View outerView = LayoutInflater.from(this).inflate(R.layout.datepick_wheel, null);
        final WheelViewDate year = (WheelViewDate) outerView.findViewById(R.id.year);
        final WheelViewDate month = (WheelViewDate) outerView.findViewById(R.id.month);
        final WheelViewDate day = (WheelViewDate) outerView.findViewById(R.id.day);

        calendar = Calendar.getInstance();

        yearMin = 1950;
        yearMax = calendar.get(Calendar.YEAR) + 10;
        yearCount = yearMax - yearMin;
        yearResult = yearMin;
        for (int i = 0; yearResult < yearMax; i++) {
            yearList.add(String.valueOf(yearResult++) + "년");
        }

        dayMin = 1;
        dayMax = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        dayCount = dayMax - dayMin;
        dayResult = dayMin;
        for (int i= 0 ; dayResult < dayMax; i ++){
            dayList.add(String.valueOf(dayResult++) + "일");
        }

        year.setOffset(2);
        year.setItems(yearList);
        year.setSeletion(yearList.size()-10);
        year.setOnWheelViewListener(new WheelViewDate.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("test", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
        month.setOffset(2);
        month.setItems(Arrays.asList(months));
        month.setSeletion(0);
        month.setOnWheelViewListener(new WheelViewDate.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("test", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
        day.setOffset(2);
        day.setItems(dayList);
        day.setSeletion(0);
        day.setOnWheelViewListener(new WheelViewDate.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("test", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_year.setText(year.getSeletedItem().replace("년", ""));
                edt_month.setText(month.getSeletedItem().replace("월", ""));
                edt_day.setText(day.getSeletedItem().replace("일", ""));
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("생년월일을 선택해주세요.")
                .setView(outerView)
                .setPositiveButton("확인", confirmListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }




    @Click(R.id.btn_register)
    protected void setBtn_register() {
        doRegister();
    }

    @Click(R.id.img_pick_birthday)
    protected void setImg_pick_birthday() {
        doPickDate();
    }

    private void doPickDate() {
        showDatePickerDialog();
    }

    private void doRegister() {
        try {

            if (TextUtils.isEmpty(edt_name.getText().toString())) {
                showErrorMessage();
            } else if (TextUtils.isEmpty(edt_year.getText().toString())) {
                showErrorMessage();
            } else if (TextUtils.isEmpty(edt_phone.getText().toString())) {
                showErrorMessage();
            } else if (TextUtils.isEmpty(edt_education.getText().toString())) {
                showErrorMessage();
            } else if (TextUtils.isEmpty(tv_belong_popup.getText().toString())) {
                showErrorMessage();
            } else if (genderFlag.equals("")) {
                showErrorMessage();
            } else {
                requestServer();
            }

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("email", edt_email.getText().toString());
//            jsonObject.put("password", edt_password.getText().toString());
//            requestJson(Url_define.Login + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    Log.d("statusCode", "로그인 성공잼 ㅎㅎ");
//                    Log.d("statusCode : ", statusCode + "");
//                    Log.d("response : ", response.toString());
//                    super.onSuccess(statusCode, headers, response);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//                    Log.d("statusCode", "로그인 실패잼 후..");
//                    super.onFailure(statusCode, headers, res, t);
//                }
//            });

//            Intent i = new Intent(RegisterTeacherNextActivity.this, StudentMainActivity.class);
//            startActivity(i);
//            finish();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void requestServer() {

        if (rdg_education.getCheckedRadioButtonId() == R.id.rd_education_going) {
            is_graduated = "false";
        } else {
            is_graduated = "true";
        }

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("name", edt_name.getText().toString());
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("gender", genderFlag);
            jsonObject.put("phone", edt_phone.getText().toString());
            jsonObject.put("edu_inst_id", belongModel.getId());

            jsonObject.put("classification", classification);
            jsonObject.put("birthday", edt_year.getText().toString() + "." + edt_month.getText().toString() + "." + edt_day.getText().toString());
            jsonObject.put("last_school", edt_education.getText().toString());
            jsonObject.put("is_graduated", is_graduated);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson(Url_define.Teacher_signup + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    PreferUtils.setSecret(RegisterTeacherNextActivity.this, response.getString("secret"));
                    PreferUtils.setUserId(RegisterTeacherNextActivity.this, response.getString("user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivityNewTask(LoginActivity_.class);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
            }
        });


    }

    private void showErrorMessage() {
        tv_msg_error.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btn_male)
    protected void btnMaleClick() {
        btn_male.setBackgroundResource(R.drawable.btn_bg_active);
        btn_female.setBackgroundResource(R.drawable.btn_bg_not_active);
        genderFlag = "M";
    }

    @Click(R.id.btn_female)
    protected void btnFemaleClick() {
        btn_male.setBackgroundResource(R.drawable.btn_bg_not_active);
        btn_female.setBackgroundResource(R.drawable.btn_bg_active);
        genderFlag = "F";
    }

    @Click(R.id.tv_belong_popup)
    protected void openBelongPopup() {
//        FragmentManager fm = getFragmentManager();
        BelongPopupDialog dialogFragment = new BelongPopupDialog(RegisterTeacherNextActivity.this);
        dialogFragment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialogFragment.setBelongPopupListener(new BelongPopupDialog.BelongPopupListener() {
            @Override
            public void onComplete(BelongModel _belongModel) {
                belongModel = _belongModel;
                tv_belong_popup.setText(belongModel.getName());
            }
        });
        dialogFragment.showView();
    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

}
