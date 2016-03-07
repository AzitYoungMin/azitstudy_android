package com.trams.azit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-09-23.
 */
public class Send_Message1 extends Activity {

    ImageView back, cancel;
    TextView send;
    EditText title, body;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message1);

        back = (ImageView)findViewById(R.id.back_btn);
        cancel = (ImageView)findViewById(R.id.cancel);
        send = (TextView)findViewById(R.id.send);
        title = (EditText)findViewById(R.id.title);
        body = (EditText)findViewById(R.id.body);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      Intent i = new Intent(Send_Message1.this, StudentMainActivity.class);
                        i.putExtra("position",6);
                        startActivity(i);
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(Send_Message1.this)
                        .setTitle("해당 질문을 삭제하시겠어요?")
                        .setPositiveButton("확인", confirmListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Send_Message1.this,"보내기 완료!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Send_Message1.this, StudentMainActivity.class);
                i.putExtra("position", 6);
                startActivity(i);
            }
        });

    }
}
