package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-09-24.
 */
public class My_Act extends FragmentActivity {

    ImageView back;
    int position;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_act);
        Intent i = getIntent();
        position = i.getIntExtra("position",0);

        back = (ImageView)findViewById(R.id.button_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewPagerNoScroll viewPager = (ViewPagerNoScroll) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyActFragmentPagerAdapter(getSupportFragmentManager(),
                My_Act.this));

        viewPager.setCurrentItem(position);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



    }
}
