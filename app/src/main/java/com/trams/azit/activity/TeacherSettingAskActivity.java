package com.trams.azit.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.trams.azit.R;
import com.trams.azit.util.ConnActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 08/01/2016.
 */
@EActivity(R.layout.activity_teacher_setting_ask)
public class TeacherSettingAskActivity extends ConnActivity {

    @ViewById
    ImageView img_back_teacher_setting_ask, go_kakao, send_email;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {

    }

    @Click(R.id.img_back_teacher_setting_ask)
    protected void goBack() {
        finish();
    }

    @Click(R.id.go_kakao)
    protected void goKakao() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goto.kakao.com/j2gzrkm9"));
        startActivity(i);
    }

    @Click(R.id.send_email)
    protected void goMail() {

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@azitstudy.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            intent.putExtra(Intent.EXTRA_TEXT   , "body of email");
            startActivity(Intent.createChooser(intent, "선택해주세요."));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
