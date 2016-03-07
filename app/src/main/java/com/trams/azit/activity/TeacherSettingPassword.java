package com.trams.azit.activity;

import android.widget.ImageView;

import com.trams.azit.R;
import com.trams.azit.util.ConnActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 07/01/2016.
 */
@EActivity(R.layout.activity_teacher_setting_password)
public class TeacherSettingPassword extends ConnActivity {

    @ViewById
    ImageView img_back_teacher_setting_pass;


    @AfterViews
    protected void init() {

    }

    @Click(R.id.img_back_teacher_setting_pass)
    protected void back() {
        finish();
    }

}
