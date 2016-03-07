package com.trams.azit;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.trams.azit.activity.NoticeActivity_;
import com.trams.azit.activity.TeacherSettingAskActivity_;
import com.trams.azit.activity.TeacherSettingDrawActivity_;
import com.trams.azit.dialog.MainStudentSettingDialog;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.DeviceUtils;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-10-23.
 */
public class App_Setting extends ConnFragment {

    RelativeLayout nick_ll, grade_ll, pass_ll, clause_ll, logout_ll, ask_ll, Contact_ll, notice_ll, draw_ll;
    TextView grade, change_phone, email, version, nick;
    EditText et_phone;
    View view;
    ImageView imgPopupSetting;
    SharedPreferences myPrefs;
    String secret, user_id;
    String str_version;

    private ToggleButton togglePush;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_setting, container, false);
        imgPopupSetting = (ImageView) view.findViewById(R.id.imgPopupSetting);
        myPrefs = getActivity().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        settingPush();
        setButton();
        setNickBtn();
        setGradeBtn();
        setPassBtn();
        setChangePhone();
        setNotice();
        setContact();
        setAsk();
        setClause();
        setLogout();
        setDraw();

        LoadFromServer();

        return view;
    }

    private void setDraw() {
        draw_ll = (RelativeLayout)view.findViewById(R.id.draw_ll);
        draw_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TeacherSettingDrawActivity_.class);
                startActivity(i);
            }
        });
    }

    private void setNotice() {
        notice_ll = (RelativeLayout)view.findViewById(R.id.notice_ll);
        notice_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NoticeActivity_.class);
                startActivity(i);
            }
        });
    }

    private void setContact() {
        Contact_ll = (RelativeLayout)view.findViewById(R.id.Contact_ll);
        Contact_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = "<br></br> <br></br> ------------------";
                body += getString(R.string.mail_contact_body_header) + " <br></br> ";
                body += "User : " + "AZit" + " <br></br> ";
                body += "Device : " + DeviceUtils.getModel() + " <br></br> ";
                body += "OS version : " + DeviceUtils.getOSVersion() + " <br></br> ";
                body += "AZit version : " + str_version + " <br></br> ";

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
        });
    }

    private void setAsk() {
        ask_ll = (RelativeLayout)view.findViewById(R.id.ask_ll);
        ask_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TeacherSettingAskActivity_.class);
                startActivity(i);
            }
        });
    }

    private void settingPush() {

        togglePush = (ToggleButton) view.findViewById(R.id.toggle_push);
        togglePush.setChecked(false);

        togglePush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferUtils.setPushSetting(getActivity(), togglePush.isChecked());

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("secret", secret);
                    obj.put("user_id", user_id);
                    obj.put("push", PreferUtils.getPushSetting(getActivity()));

                    requestJson(Url_define.Student_Push_Setting + Url_define.KEY, obj, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);
                            Toast.makeText(getActivity(), "푸시 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Log.d("statusCode : ", statusCode + "");
                            super.onFailure(statusCode, headers, res, t);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.Student_get_profile + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {

                        nick.setText(response.getString("nickname"));
                        et_phone.setText(response.getString("phone"));
                        email.setText(response.getString("email"));

                        if (response.getString("push").equals("true")) {
                            togglePush.setChecked(true);
                        } else {
                            togglePush.setChecked(false);
                        }

                        if (response.getString("year").equals("")) {
                            grade.setText("재도전");
                        } else {
                            grade.setText("고" + response.getString("year").toString());
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

    private void setLogout() {
        logout_ll = (RelativeLayout) view.findViewById(R.id.logout_ll);
        logout_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("login", "false");
                editor.commit();

                Intent i = new Intent((StudentMainActivity) getActivity(), LoginActivity_.class);
                startActivity(i);
                getActivity().finish();

            }
        });
    }

    private void setClause() {
        clause_ll = (RelativeLayout) view.findViewById(R.id.clause_ll);
        clause_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((StudentMainActivity) getActivity(), Clause.class);
                startActivity(i);
            }
        });
    }

    private void setChangePhone() {
        change_phone = (TextView) view.findViewById(R.id.change_phone);
        et_phone = (EditText) view.findViewById(R.id.et_phone);

        change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_phone.requestFocus();

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
                    jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
                    jsonObject.put("phone", et_phone.getText().toString());

                    requestJson(Url_define.CHANGE_PHONE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                if (response.getString("result").equals("success")) {
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
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
        });

        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_phone, InputMethodManager.SHOW_IMPLICIT);
                et_phone.setSelection(et_phone.length());

            }
        });
        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_phone && actionId == EditorInfo.IME_ACTION_DONE) {
                    email.requestFocus();
                }

                return false;
            }
        });
    }

    private void setPassBtn() {
        pass_ll = (RelativeLayout) view.findViewById(R.id.pass_ll);
        pass_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((StudentMainActivity) getActivity(), ChangePass.class);
                startActivity(i);
            }
        });
    }

    private void setGradeBtn() {
        grade_ll = (RelativeLayout) view.findViewById(R.id.grade_ll);
        grade_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"고1", "고2", "고3", "재도전"};
                AlertDialog.Builder builder = new AlertDialog.Builder((StudentMainActivity) getActivity());
                builder.setTitle("학년 선택");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        grade.setText(items[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void setNickBtn() {
        nick_ll = (RelativeLayout) view.findViewById(R.id.nick_ll);
        nick_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((StudentMainActivity) getActivity(), Edit_Student_Profile_.class);
                startActivity(i);
            }
        });
    }

    private void setButton() {
        grade = (TextView) view.findViewById(R.id.grade);
        email = (TextView) view.findViewById(R.id.email);
        version = (TextView) view.findViewById(R.id.version);
        nick = (TextView) view.findViewById(R.id.nick);

        imgPopupSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainStudentSetting = new Intent(getActivity(), MainStudentSettingDialog.class);
                startActivity(mainStudentSetting);
            }
        });


        try {
            PackageInfo i = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getApplicationContext().getPackageName(), 0);
            str_version = i.versionName;
            version.setText(str_version);
        } catch (PackageManager.NameNotFoundException e) {
        }

    }
}
