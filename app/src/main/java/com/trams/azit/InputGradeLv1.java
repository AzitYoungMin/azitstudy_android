package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-10-23.
 */
public class InputGradeLv1 extends Activity {

    Spinner spin_korean, spin_math, spin_english, spin_social, spin_science;
    TextView next_btn;
    ImageView back_btn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inputgrade_lv1);

        setButton();
        setSpinner();

    }

    private void setButton() {
        back_btn = (ImageView)findViewById(R.id.back_btn);
        next_btn = (TextView)findViewById(R.id.next_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputGradeLv1.this, InputGradeLv2.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void setSpinner() {
        spin_korean = (Spinner) findViewById(R.id.spin_korean);
        spin_math = (Spinner) findViewById(R.id.spin_math);
        spin_english = (Spinner) findViewById(R.id.spin_english);
        spin_social = (Spinner) findViewById(R.id.spin_social);
        spin_science = (Spinner) findViewById(R.id.spin_science);

        String[] PeriodOption = getResources().getStringArray(R.array.grade);
        SpinnerAdapter spinadapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, PeriodOption);
        spin_korean.setAdapter(spinadapter);
        spin_math.setAdapter(spinadapter);
        spin_english.setAdapter(spinadapter);
        spin_social.setAdapter(spinadapter);
        spin_science.setAdapter(spinadapter);

    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[]{};

        public SpinnerAdapter(final Context context,
                              final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        /**
         * 스피너 클릭시 보여지는 View의 정의
         */
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);
            tv.setHeight(50);
            return convertView;
        }

        /**
         * 기본 스피너 View 정의
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);
            tv.setHeight(60);
            return convertView;
        }
    }

}
