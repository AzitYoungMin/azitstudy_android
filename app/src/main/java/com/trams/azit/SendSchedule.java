package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2015-09-11.
 */
@EActivity(R.layout.activity_send_schedule)
public class SendSchedule extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Click(R.id.album)
    protected void setAlbum(){
        Toast.makeText(this,"album",Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.cameara)
    protected void setCamera(){
        Toast.makeText(this,"cameara",Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.back_btn)
    protected void setBack() {
        finish();
    }

}
