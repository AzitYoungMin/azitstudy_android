package com.trams.azit.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.trams.azit.activity.NoticeActivity_;
import com.trams.azit.activity.TeacherSettingAskActivity_;
import com.trams.azit.activity.TeacherSettingDrawActivity_;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.trams.azit.BelongModel;
import com.trams.azit.ChangePass;
import com.trams.azit.Clause;
import com.trams.azit.LoginActivity_;
import com.trams.azit.R;
import com.trams.azit.dialog.BelongPopupUpdateDialog;
import com.trams.azit.dialog.TeacherSettingEducationDialog;
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
@EActivity(R.layout.activity_teacher_setting)
public class TeacherSettingActivity extends ConnActivity {

    @ViewById
    TextView tv_name_teacher, tv_phone_teacher, tv_email_teacher, tv_education_teacher, tv_change_password_teacher, tv_notice, tv_terms_policy,
            tv_mail_contact, tv_open_brower_screen, tv_draw_teacher, tv_logout_teacher, tv_version, tv_belong, tv_school_teacher;

    @ViewById
    ImageView img_back_teacher_setting;

    @ViewById
    Button img_change_phone_teacher;

    @ViewById
    ToggleButton toggle_push_teacher;
    boolean isGraduated;
    final String[] text = {""};
    final int width = 500;
    final int height = 500;
    final String stringImage = "http://i.imgur.com/rBc4Kaf.jpg";
    final String stringUrl = "market://details?id=com.trams.azit";
    private String graduate = "재학";
    private String lastSchool = "";
    private KakaoLink kakaoLink = null;;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {

        try {
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        }catch (KakaoParameterException e) {
            e.printStackTrace();
        }


        toggle_push_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", PreferUtils.getSecret(TeacherSettingActivity.this));
                    jsonObject.put("user_id", PreferUtils.getUserId(TeacherSettingActivity.this));
                    jsonObject.put("push", toggle_push_teacher.isChecked());

                    requestJson(Url_define.PUSH_SETTING + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                Toast.makeText(TeacherSettingActivity.this, "푸시 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
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

//        Intent i = new Intent(TeacherSettingActivity.this, ChangePass.class);
//        startActivity(i);

        Intent i = new Intent(TeacherSettingActivity.this, ChangePass.class);
        startActivity(i);

    }

    @Click(R.id.kakao11)
    protected void kakao() {
        try {
            text[0] = "친구야! 같이 공부하자!\n" +
                    "공부 좀 하는 얘들은 다 쓰는 어플리케이션이래!\n" +
                    "게다가 사용하는 학생이 많은 학교에는 새학기에 간식이 온대!!\n" +
                    "\n" +
                    "< 스케줄러, 스톱워치 à 학습 비교/분석 >\n" +
                    "교재 보관함을 이용해서, 클릭 한두번만으로 ‘스케줄 추가’ 완료!\n" +
                    "스톱워치를 써도 되고, 공부끝나고 기록 정리용으로 써도 되고!\n" +
                    "자동으로 내 공부를 분석해주고,\n" +
                    "경쟁자들과 비교해주는 아지트스터디!\n" +
                    "-과목별 공부비중, 학습 방식 분석, 학습 내용 분석, 일간/주간 학습 분석 등\n" +
                    "\n" +
                    "< Q&A, 학습 상담>\n" +
                    "명문대를 진학한 선배들이 질문도 받아주고, 상담도 해준대!\n" +
                    "\n" +
                    "같이 목표를 달성해보자!\n" +
                    "\n" +
                    "아지트스터디를 이용하는 학생이 가장 많은 학교에, 새학기를 응원하는 간식을 보내드립니다.";
            kakaoTalkLinkMessageBuilder.addImage(stringImage, width, height)
                    .addText(text[0])
                    .addAppButton(stringUrl)
                    .build();

            kakaoLink.sendMessage(this.kakaoTalkLinkMessageBuilder, this);
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.kakao22)
    protected void kakao2() {
        try {
            text[0] = "후배들에게는 피와 살이 되는 조언과 팁을!\n" +
                    "나에게는 피와 살이 되는 기프티콘을!\n" +
                    "\n" +
                    "너무나도 훌륭한 당신을 아지트스터디의 멘토로 모시고 싶습니다.\n" +
                    "\n" +
                    "나만 알고 있기엔 너무 아까운 공부 노하우와 꿀팁!\n" +
                    "\n" +
                    "후배들에게 나눠주고 마음도 풍성해지고, 주머니도 풍성해지세요!\n" +
                    "\n" +
                    "고등학생 예비 후배님들의 학습 질문/상담에 답변을 달아주시면\n" +
                    "질문 수에 비례해서 기프티콘을 드립니다.\n" +
                    "\n" +
                    "아지트스터디의 멘토로 활약해주세요! ";
            kakaoTalkLinkMessageBuilder.addImage(stringImage, width, height)
                    .addText(text[0])
                    .addAppButton(stringUrl)
                    .build();

            kakaoLink.sendMessage(this.kakaoTalkLinkMessageBuilder, this);
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.kakao33)
    protected void kakao33() {
        try {
            text[0] = "학생들 학습관리 어플리케이션이에요\n" +
                    "상담하시고 아이들 독려/지도 하시는 데에 작은 도움이 되지 않을까 해서 추천해드립니다!\n" +
                    "\n" +
                    "< 학생 학습분석자료 >\n" +
                    "담당 학생을 추가하셔서\n" +
                    "학습 데이터 분석자료를 통해 학생 상담과 지도에 도움이 되실 거에요!\n" +
                    "-과목별 공부비중, 학습 방식 분석, 학습 내용 분석, 공부 목표량 달성 여부 등\n" +
                    "\n" +
                    "< 학생 성적분석자료 >\n" +
                    "학생 성적의 변화추이를 한눈에 언제 어디서든 파악하실 수 있습니다.\n" +
                    "\n" +
                    "< 개별/전체 공지 메시지 >\n" +
                    "개별/전체 메시지를 통해 언제 어디서든 학생의 상황에 맞는 격려와 조언을 해주세요!\n" +
                    "\n" +
                    "선생님을 응원합니다!\n" +
                    "\n" +
                    "곧, 컴퓨터로도 사용 가능하다고 하네요!";
            kakaoTalkLinkMessageBuilder.addImage(stringImage, width, height)
                    .addText(text[0])
                    .addAppButton(stringUrl)
                    .build();

            kakaoLink.sendMessage(this.kakaoTalkLinkMessageBuilder, this);
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
    private void loadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));

            requestJson(Url_define.TEACHER_PROFILE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        boolean push = Boolean.valueOf(response.getString("push"));
                        toggle_push_teacher.setChecked(push);

                        tv_name_teacher.setText(response.getString("name"));
                        tv_phone_teacher.setText(response.getString("phone"));
                        tv_email_teacher.setText(response.getString("email"));
                        tv_school_teacher.setText(response.getString("last_school"));
                        tv_belong.setText(response.getString("edu_inst"));
                        lastSchool = response.getString("last_school");

                        isGraduated = Boolean.valueOf(response.getString("is_graduated"));
                        if (isGraduated) {
                            tv_education_teacher.setText("졸업");
                        } else {
                            tv_education_teacher.setText("재학");
                        }
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

    @Click(R.id.tv_terms_policy)
    protected void openTermsAndPolicy() {
        Intent i = new Intent(TeacherSettingActivity.this, Clause.class);
        startActivity(i);
    }

    @Click(R.id.tv_notice)
    protected void showNotice() {
        Intent i = new Intent(TeacherSettingActivity.this, NoticeActivity_.class);
        startActivity(i);
    }

    @Click(R.id.tv_education_teacher)
    protected void changeEducation() {

        TeacherSettingEducationDialog teacherSettingEducationDialog = new TeacherSettingEducationDialog(this, tv_education_teacher.getText().toString(), lastSchool);

        teacherSettingEducationDialog.setEducationSettingListener(new TeacherSettingEducationDialog.EducationSettingListener() {
            @Override
            public void onSuccess(String graduateResponse, String _lastSchool) {
//                tv_education_teacher.setText(graduateResponse);
//                lastSchool = _lastSchool;
                loadFromServer();
            }
        });

        teacherSettingEducationDialog.showView();

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

    @Click(R.id.tv_logout_teacher)
    protected void doLogout() {
        PreferUtils.setLoginString(this, "false");
        startActivityNewTask(LoginActivity_.class);
    }

    @Click(R.id.img_back_teacher_setting)
    protected void onBack() {
        finish();
    }

    @Click(R.id.tv_belong)
    protected void onUpdateBelong() {
        FragmentManager fm = getFragmentManager();
        BelongPopupUpdateDialog dialogFragment = new BelongPopupUpdateDialog();

        dialogFragment.setBelongPopupListener(new BelongPopupUpdateDialog.BelongPopupListener() {
            @Override
            public void onComplete(BelongModel _belongModel) {

                loadFromServer();
            }
        });
        dialogFragment.show(fm, "BelongPopupDialog");
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
        Intent i = new Intent(TeacherSettingActivity.this, TeacherSettingAskActivity_.class);
        startActivity(i);
    }

    @Click(R.id.tv_draw_teacher)
    protected void openDrawScreen() {
        Intent i = new Intent(TeacherSettingActivity.this, TeacherSettingDrawActivity_.class);
        startActivity(i);
    }

}
