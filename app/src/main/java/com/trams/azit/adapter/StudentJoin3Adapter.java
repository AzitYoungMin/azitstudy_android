package com.trams.azit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.trams.azit.FragmentPagerAdapter;
import com.trams.azit.fragment.LiberalArtFragment;
import com.trams.azit.fragment.NaturalScienceFragment;

/**
 * Created by sonnv on 1/13/2016.
 */
public class StudentJoin3Adapter extends FragmentStatePagerAdapter {

    public StudentJoin3Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LiberalArtFragment tab1 = new LiberalArtFragment();
                return tab1;
            case 1:
                NaturalScienceFragment tab2 = new NaturalScienceFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
