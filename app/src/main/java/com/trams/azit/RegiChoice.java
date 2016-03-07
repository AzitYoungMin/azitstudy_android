package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.trams.azit.RegisterTeacherActivity_;
import com.trams.azit.activity.MentorJoinStep1Activity;
import com.trams.azit.activity.register.student.JoinStudent1Activity;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2015-09-02.
 */
@EActivity(R.layout.activity_regichoice)
public class RegiChoice extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

    @Click(R.id.choice_student)
    protected void setChoice_student() {
        //Intent i = new Intent(RegiChoice.this, Regi_student_.class);
        Intent i = new Intent(RegiChoice.this, JoinStudent1Activity.class);
        startActivity(i);
    }

    @Click(R.id.choice_mento)
    protected void setChoice_mento() {
//        Intent i = new Intent(RegiChoice.this, Regi_mento_.class);
        Intent i = new Intent(RegiChoice.this, MentorJoinStep1Activity.class);
        startActivity(i);
    }

    @Click(R.id.choice_teacher)
    protected void setChoice_teacher() {
        Intent i = new Intent(RegiChoice.this, RegisterTeacherActivity_.class);
        startActivity(i);
    }

}
