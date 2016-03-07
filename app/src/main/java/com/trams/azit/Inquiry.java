package com.trams.azit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015-10-27.
 */
public class Inquiry extends Fragment {

    ImageView go_kakao, send_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inquiry, null);

        go_kakao = (ImageView) view.findViewById(R.id.go_kakao);
        send_email = (ImageView) view.findViewById(R.id.send_email);

        go_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goto.kakao.com/j2gzrkm9"));
                startActivity(i);
            }
        });
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@azitstudy.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                intent.putExtra(Intent.EXTRA_TEXT   , "body of email");
                startActivity(Intent.createChooser(intent, "선택해주세요."));
            }
        });

        return view;

    }
}
