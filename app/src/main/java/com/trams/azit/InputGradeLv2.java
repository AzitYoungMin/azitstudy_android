package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-10-23.
 */
public class InputGradeLv2 extends Activity {

    TextView next_btn;
    ImageView back_btn;
    LinearLayout mock_point_ll, mock_standard_point_ll;
    Typeface tf;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inputgrade_lv2);

        tf = Typeface.createFromAsset(getAssets(), "NanumGothic.ttf");

        setButton();
        setMockPoint("언어");
        setMockPoint("수학");
        setMockPoint("영어");
        setStandardMockPoint("언어");
        setStandardMockPoint("수학");
        setStandardMockPoint("영어");

    }

    private void setStandardMockPoint(String title) {
        mock_standard_point_ll = (LinearLayout) findViewById(R.id.mock_standard_point_ll);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View point_view = inflater.from(getApplicationContext()).inflate(R.layout.dynamic_point, null);
        TextView subject = (TextView) point_view.findViewById(R.id.subject);
        TextView point = (TextView) point_view.findViewById(R.id.point);
        EditText et_point = (EditText) point_view.findViewById(R.id.et_point);
        subject.setTypeface(tf);
        point.setTypeface(tf);
        et_point.setTypeface(tf);
        subject.setText(title);
        mock_standard_point_ll.addView(point_view);

    }

    private void setMockPoint(String title) {
        mock_point_ll = (LinearLayout) findViewById(R.id.mock_point_ll);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View point_view = inflater.from(getApplicationContext()).inflate(R.layout.dynamic_point, null);
        TextView subject = (TextView) point_view.findViewById(R.id.subject);
        TextView point = (TextView) point_view.findViewById(R.id.point);
        EditText et_point = (EditText) point_view.findViewById(R.id.et_point);
        subject.setTypeface(tf);
        point.setTypeface(tf);
        et_point.setTypeface(tf);
        subject.setText(title);
        mock_point_ll.addView(point_view);
    }

    private void setButton() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        next_btn = (TextView) findViewById(R.id.next_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getSharedPreferences("Azit", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("IsPoPup", "true"); // "true" : 성적을 한번 이상 수정함. "": 성적을 수정하지 않음. - 팝업을 처음에 한번만 뜨우기 위해 사용
                editor.commit();

                Intent i = new Intent(InputGradeLv2.this, StudentMainActivity.class);
                i.putExtra("position", 0);
                startActivity(i);
                finish();
            }
        });
    }
}
