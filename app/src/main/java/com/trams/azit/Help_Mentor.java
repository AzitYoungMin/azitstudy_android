package com.trams.azit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.trams.azit.activity.MentoMainActivity;

public class Help_Mentor extends AppCompatActivity implements View.OnTouchListener {

    ViewFlipper flipper;
    ImageButton btn;
    private float m_nPreTouchPosX = 0;
    private float m_nPreTouchPosY = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help__mentor);

        flipper = (ViewFlipper) findViewById(R.id.Flipper);
        flipper.setOnTouchListener(this);

        btn = (ImageButton)findViewById(R.id.button12);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Help_Mentor.this, MentoMainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != flipper) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                m_nPreTouchPosY = event.getX();

                if (m_nPreTouchPosY < m_nPreTouchPosX) {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                    flipper.showNext();
                } else if (m_nPreTouchPosY > m_nPreTouchPosX) {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                    flipper.showPrevious();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                m_nPreTouchPosX = event.getX();
                break;
        }

        return true;
    }



}
