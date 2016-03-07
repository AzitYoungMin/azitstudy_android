package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-09-24.
 */
public class Record_Student_Study extends FragmentActivity {

    public TextView student_Name;
    public TextView mon, tue, wed, thu, fri, sat, sun;
    String str_name, mock_num, image;
    int id, mon_num, tue_num, wed_num, thu_num, fri_num, sat_num, sun_num;
    public ImageView mock;
    ImageView back, photo;
    TextView Record_Student_Grade, send_message;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_study);
        Intent i = getIntent();

        id = i.getIntExtra("id", 0);
        image = i.getStringExtra("image");
        str_name = i.getStringExtra("name");
        mon_num = i.getIntExtra("mon", 0);
        tue_num = i.getIntExtra("tue", 0);
        wed_num = i.getIntExtra("wed", 0);
        thu_num = i.getIntExtra("thu", 0);
        fri_num = i.getIntExtra("fri", 0);
        sat_num = i.getIntExtra("sat", 0);
        sun_num = i.getIntExtra("sun", 0);
        mock_num = i.getStringExtra("mock");

        back = (ImageView) findViewById(R.id.back_btn);
        Record_Student_Grade = (TextView) findViewById(R.id.my_grade);
        send_message = (TextView) findViewById(R.id.message_send);
        student_Name = (TextView) findViewById(R.id.student_name);
        mon = (TextView) findViewById(R.id.mon_image);
        tue = (TextView) findViewById(R.id.tue_image);
        wed = (TextView) findViewById(R.id.wed_image);
        thu = (TextView) findViewById(R.id.thu_image);
        fri = (TextView) findViewById(R.id.fri_image);
        sat = (TextView) findViewById(R.id.sat_image);
        sun = (TextView) findViewById(R.id.sun_image);
        mock = (ImageView) findViewById(R.id.mock_check);
        photo = (ImageView) findViewById(R.id.image);


        Picasso.with(Record_Student_Study.this).load(Url_define.BASE_Image + image).into(photo);
        student_Name.setText(str_name);

        if (mon_num == 1) {
            mon.setBackgroundResource(R.drawable.circle_gray);
            mon.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (mon_num == 2) {
            mon.setBackgroundResource(R.drawable.circle_yellow);
            mon.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (mon_num == 0) {
            mon.setBackgroundResource(0);
            mon.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (tue_num == 1) {
            tue.setBackgroundResource(R.drawable.circle_gray);
            tue.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (tue_num == 2) {
            tue.setBackgroundResource(R.drawable.circle_yellow);
            tue.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (tue_num == 0) {
            tue.setBackgroundResource(0);
            tue.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (wed_num == 1) {
            wed.setBackgroundResource(R.drawable.circle_gray);
            wed.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (wed_num == 2) {
            wed.setBackgroundResource(R.drawable.circle_yellow);
            wed.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (wed_num == 0) {
            wed.setBackgroundResource(0);
            wed.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (thu_num == 1) {
            thu.setBackgroundResource(R.drawable.circle_gray);
            thu.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (thu_num == 2) {
            thu.setBackgroundResource(R.drawable.circle_yellow);
            thu.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (thu_num == 0) {
            thu.setBackgroundResource(0);
            thu.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (fri_num == 1) {
            fri.setBackgroundResource(R.drawable.circle_gray);
            fri.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (fri_num == 2) {
            fri.setBackgroundResource(R.drawable.circle_yellow);
            fri.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (fri_num == 0) {
            fri.setBackgroundResource(0);
            fri.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (sat_num == 1) {
            sat.setBackgroundResource(R.drawable.circle_gray);
            sat.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (sat_num == 2) {
            sat.setBackgroundResource(R.drawable.circle_yellow);
            sat.setTextColor(ContextCompat.getColor(this, R.color.black));
        } else if (sat_num == 0) {
            sat.setBackgroundResource(0);
            sat.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (sun_num == 1) {
            sun.setBackgroundResource(R.drawable.circle_gray);
            sun.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (sun_num == 2) {
            sun.setBackgroundResource(R.drawable.circle_yellow);
            sun.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (sun_num == 0) {
            sun.setBackgroundResource(0);
            sun.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        if (mock_num.equals("1")) {
            mock.setBackgroundResource(R.drawable.mock_check);
        } else if (mock_num.equals("2")) {
            mock.setBackgroundResource(R.drawable.mock_check);
            mock.setVisibility(View.INVISIBLE);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager(),
                Record_Student_Study.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Record_Student_Grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Record_Student_Study.this, Record_Student_Grade.class);
                i.putExtra("name", str_name);
                i.putExtra("id", id);
                i.putExtra("image", image);
                i.putExtra("mon", mon_num);
                i.putExtra("tue", tue_num);
                i.putExtra("wed", wed_num);
                i.putExtra("thu", thu_num);
                i.putExtra("fri", fri_num);
                i.putExtra("sat", sat_num);
                i.putExtra("sun", sun_num);
                i.putExtra("mock", mock_num);
                startActivity(i);
                finish();
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Record_Student_Study.this, Send_Message_From_Teacher.class);
                i.putExtra("name", str_name);
                i.putExtra("id", id);
                i.putExtra("image", image);
                i.putExtra("mon", mon_num);
                i.putExtra("tue", tue_num);
                i.putExtra("wed", wed_num);
                i.putExtra("thu", thu_num);
                i.putExtra("fri", fri_num);
                i.putExtra("sat", sat_num);
                i.putExtra("sun", sun_num);
                i.putExtra("mock", mock_num);
                startActivity(i);
            }
        });


    }
}
