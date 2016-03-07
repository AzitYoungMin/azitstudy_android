package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-10-28.
 */
public class Mento_Act extends FragmentActivity {

    ImageView back;
    int position;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mento_act);
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
        viewPager.setAdapter(new MentoActFragmentPagerAdapter(getSupportFragmentManager(),
                Mento_Act.this));

        viewPager.setCurrentItem(position);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



    }
}
