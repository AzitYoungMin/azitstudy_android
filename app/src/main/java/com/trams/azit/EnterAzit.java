package com.trams.azit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015-09-11.
 */

public class EnterAzit extends Fragment {

    boolean noinfo = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (noinfo){
            view = inflater.inflate(R.layout.activity_enterazit, null);
        }else{
            view = inflater.inflate(R.layout.activity_enterazit_main, null);
            ((StudentMainActivity)getActivity()).btBack.setVisibility(View.GONE);
            ((StudentMainActivity)getActivity()).btMenu.setVisibility(View.VISIBLE);
            ((StudentMainActivity)getActivity()).tvTitle.setText("아지트");
        }


        return view;
    }
}
