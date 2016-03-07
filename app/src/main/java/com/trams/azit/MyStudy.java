package com.trams.azit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-09-07.
 */

public class MyStudy extends Fragment implements View.OnClickListener{

    TextView my_grade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_study, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(this.getChildFragmentManager(),
                getContext()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        my_grade = (TextView)view.findViewById(R.id.my_grade);
        my_grade.setOnClickListener(this);

        return  view;
    }

    @Override
    public void onClick(View v) {
        if (v == my_grade){
            ((StudentMainActivity) getActivity()).onMenuItemClick(2);
        }
    }

}
