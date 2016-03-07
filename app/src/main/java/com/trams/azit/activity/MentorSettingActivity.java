package com.trams.azit.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.trams.azit.activity.NoticeActivity_;
import com.trams.azit.activity.TeacherSettingAskActivity_;
import com.trams.azit.activity.TeacherSettingDrawActivity_;
import com.trams.azit.ChangePass;
import com.trams.azit.Clause;
import com.trams.azit.LoginActivity_;
import com.trams.azit.R;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.DeviceUtils;
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
 * Created by Administrator on 07/01/2016.
 */
@EActivity(R.layout.activity_mentor_setting)
public class MentorSettingActivity extends ConnActivity {

    @ViewById
    TextView tv_email_teacher, tv_education_teacher, tv_change_password_teacher, tv_notice, tv_terms_policy, tv_mail_contact, tv_open_brower_screen, tv_draw_teacher,
            tv_logout_teacher, tv_name_teacher, tv_phone_teacher, tv_version;

    @ViewById
    ImageView img_back_teacher_setting;

    @ViewById
    Button img_change_phone_teacher;

    @ViewById
    ToggleButton toggle_push_teacher;

    @ViewById
    Spinner sp_level;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {
        toggle_push_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", PreferUtils.getSecret(MentorSettingActivity.this));
                    jsonObject.put("user_id", PreferUtils.getUserId(MentorSettingActivity.this));
                    jsonObject.put("push", toggle_push_teacher.isChecked());

                    requestJson(Url_define.PUSH_SETTING + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                Toast.makeText(MentorSettingActivity.this, "푸시 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            super.onFailure(statusCode, headers, res, t);
                        }
                    });
                } catch (JSONException e) {
                    e.getStackTrace();
                }
            }
        });

        loadFromServer();

        tv_version.setText(DeviceUtils.getVersionInfo(this).getVersionName());

    }

    @Click(R.id.tv_change_password_teacher)
    protected void changePassword() {
        Intent i = new Intent(MentorSettingActivity.this, ChangePass.class);
        startActivity(i);
    }

    @Click(R.id.tv_logout_teacher)
    protected void tv_logout_teacher() {
        PreferUtils.setLoginString(this, "false");
        startActivityNewTask(LoginActivity_.class);
    }

    @Click(R.id.tv_terms_policy)
    protected void openTermsAndPolicy() {
        Intent i = new Intent(MentorSettingActivity.this, Clause.class);
        startActivity(i);
    }

    @Click(R.id.tv_notice)
    protected void showNotice() {
        Intent i = new Intent(MentorSettingActivity.this, NoticeActivity_.class);
        startActivity(i);
    }

    @Click(R.id.tv_education_teacher)
    protected void changeEducation() {
//        TeacherSettingEducationDialog.getInstance(this).showView();
    }

    @Click(R.id.img_change_phone_teacher)
    protected void changePhoneTeacher() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));
            jsonObject.put("phone", tv_phone_teacher.getText().toString());

            requestJson(Url_define.CHANGE_PHONE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    @Click(R.id.img_back_teacher_setting)
    protected void onBack() {
        finish();
    }

    private void loadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));

            requestJson(Url_define.Mento_Get_Profile + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        boolean push = Boolean.valueOf(response.getString("push"));
                        toggle_push_teacher.setChecked(push);

                        tv_name_teacher.setText(response.getString("name"));
                        tv_phone_teacher.setText(response.getString("phone"));
                        tv_email_teacher.setText(response.getString("email"));

                        int year = Integer.valueOf(response.getString("year")) - 1;

                        sp_level.setSelection(year);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    @Click(R.id.tv_mail_contact)
    protected void openMailContact() {

        String body = "<br></br> <br></br> ------------------";
        body += getString(R.string.mail_contact_body_header) + " <br></br> ";
        body += "User : " + "AZit" + " <br></br> ";
        body += "Device : " + DeviceUtils.getModel() + " <br></br> ";
        body += "OS version : " + DeviceUtils.getOSVersion() + " <br></br> ";
        body += "AZit version : " + DeviceUtils.getVersionInfo(this).getVersionName() + " <br></br> ";

        String[] arrAddress = getString(R.string.mail_contact_to).split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, arrAddress);
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.mail_contact_title));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_contact_title));

        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Click(R.id.tv_open_brower_screen)
    protected void openIntroScreen() {
        Intent i = new Intent(MentorSettingActivity.this, TeacherSettingAskActivity_.class);
        startActivity(i);
    }

    @Click(R.id.tv_draw_teacher)
    protected void openDrawScreen() {
        Intent i = new Intent(MentorSettingActivity.this, TeacherSettingDrawActivity_.class);
        startActivity(i);
    }

}
