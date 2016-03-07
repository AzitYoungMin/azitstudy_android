package com.trams.azit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Administrator on 2015-09-23.
 */
public class Mentoring extends Fragment {

    RelativeLayout message, ask, answer;
    ImageView request;

    boolean noPayment = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = null;
        if (noPayment) {
            view = inflater.inflate(R.layout.activity_mentoring, null);
            request = (ImageView)view.findViewById(R.id.request);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText((StudentMainActivity)getActivity(), "준비중 입니다.",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view = inflater.inflate(R.layout.activity_mentoring_main, null);
            ((StudentMainActivity) getActivity()).btBack.setVisibility(View.GONE);
            ((StudentMainActivity) getActivity()).btMenu.setVisibility(View.VISIBLE);
            ((StudentMainActivity) getActivity()).tvTitle.setText("맞춤리포트");

            message = (RelativeLayout) view.findViewById(R.id.message);
            ask = (RelativeLayout) view.findViewById(R.id.ask);
            answer = (RelativeLayout) view.findViewById(R.id.answer);

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent((StudentMainActivity) getActivity(), Send_Message.class);
                    startActivity(i);
                }
            });
            ask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent((StudentMainActivity) getActivity(), Ask.class);
                    startActivity(i);
                }
            });
            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent((StudentMainActivity) getActivity(), Answer.class);
                    startActivity(i);
                }
            });
        }
        return view;
    }
}


