package com.trams.azit.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trams.azit.LoginActivity_;
import com.trams.azit.R;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 08/01/2016.
 */
@EActivity(R.layout.activity_teacher_setting_draw)
public class TeacherSettingDrawActivity extends ConnActivity {

    @ViewById
    ImageView img_back_teacher_setting_draw;

    @ViewById
    EditText edt_draw;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Click(R.id.img_back_teacher_setting_draw)
    protected void onBack() {
        finish();
    }

    @Click(R.id.draw_submit)
    protected void submit() {
        String pass = edt_draw.getText().toString();
        if (TextUtils.isEmpty(pass) || pass.length() < 8) {

            edt_draw.setError("비밀번호는 8자 이상입니다.");

        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("secret", PreferUtils.getSecret(this));
                jsonObject.put("user_id", PreferUtils.getUserId(this));
                jsonObject.put("exist_password", edt_draw.getText().toString());

                requestJson(Url_define.WITHDRAWAL + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (response.getString("result").equals("success")) {
                                Toast.makeText(TeacherSettingDrawActivity.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(TeacherSettingDrawActivity.this, LoginActivity_.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
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
    }

}
