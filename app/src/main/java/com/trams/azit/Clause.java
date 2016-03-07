package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-11-04.
 */
public class Clause extends Activity {

    TextView personal_btn, service_btn, personal_text, service_text;
    View personal_ll, service_ll;
    LinearLayout personal_info_body, service_body;
    ImageView back;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause);

        setButton();
        setBack();
        setPersonal();
        setService();

    }

    private void setBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setService() {
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_btn.setTextColor(Color.parseColor("#1FB4FF"));
                personal_btn.setTextColor(Color.parseColor("#b2b2b2"));
                service_ll.setBackgroundColor(Color.parseColor("#1FB4FF"));
                personal_ll.setBackgroundColor(Color.parseColor("#ffffff"));
                service_body.setVisibility(View.VISIBLE);
                personal_info_body.setVisibility(View.GONE);
            }
        });
    }

    private void setPersonal() {
        personal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_btn.setTextColor(Color.parseColor("#1FB4FF"));
                service_btn.setTextColor(Color.parseColor("#b2b2b2"));
                personal_ll.setBackgroundColor(Color.parseColor("#1FB4FF"));
                service_ll.setBackgroundColor(Color.parseColor("#ffffff"));
                personal_info_body.setVisibility(View.VISIBLE);
                service_body.setVisibility(View.GONE);
            }
        });
    }

    private void setButton() {
        back = (ImageView)findViewById(R.id.back_btn);
        personal_btn = (TextView)findViewById(R.id.personal_info);
        service_btn = (TextView)findViewById(R.id.service);
        personal_ll = findViewById(R.id.personal_ll);
        service_ll = findViewById(R.id.service_ll);
        personal_info_body = (LinearLayout)findViewById(R.id.personal_info_body);
        service_body = (LinearLayout)findViewById(R.id.service_body);
        personal_text = (TextView)findViewById(R.id.personal_text);
        service_text = (TextView)findViewById(R.id.service_text);

        personal_text.setText(getString(R.string.personal));
        service_text.setText(getString(R.string.service));
    }
}
