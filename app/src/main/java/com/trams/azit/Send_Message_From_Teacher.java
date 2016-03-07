package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-25.
 */
public class Send_Message_From_Teacher extends ConnActivity {

    public TextView student_Name;
    public TextView mon, tue, wed, thu, fri, sat, sun;
    public ImageView mock, photo;
    EditText body, edtTitle;
    String str_name, mock_num, image;
    int id, mon_num, tue_num, wed_num, thu_num, fri_num, sat_num, sun_num;
    ImageView back;
    Button send;
    SharedPreferences myPrefs;
    String secret, user_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_teacher);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        Intent i = getIntent();

        id = i.getIntExtra("id", 0);
        str_name = i.getStringExtra("name");
        image = i.getStringExtra("image");
        mon_num = i.getIntExtra("mon", 0);
        tue_num = i.getIntExtra("tue", 0);
        wed_num = i.getIntExtra("wed", 0);
        thu_num = i.getIntExtra("thu", 0);
        fri_num = i.getIntExtra("fri", 0);
        sat_num = i.getIntExtra("sat", 0);
        sun_num = i.getIntExtra("sun", 0);
        mock_num = i.getStringExtra("mock");

        Log.e("id", String.valueOf(id));


        student_Name = (TextView) findViewById(R.id.student_name);
        mon = (TextView) findViewById(R.id.mon_image);
        tue = (TextView) findViewById(R.id.tue_image);
        wed = (TextView) findViewById(R.id.wed_image);
        thu = (TextView) findViewById(R.id.thu_image);
        fri = (TextView) findViewById(R.id.fri_image);
        sat = (TextView) findViewById(R.id.sat_image);
        sun = (TextView) findViewById(R.id.sun_image);
        mock = (ImageView) findViewById(R.id.mock_check);
        photo = (ImageView) findViewById(R.id.photo);
        body = (EditText) findViewById(R.id.body);
        edtTitle = (EditText) findViewById(R.id.edt_title);

        Picasso.with(Send_Message_From_Teacher.this).load(Url_define.BASE_Image + image).into(photo);
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

        back = (ImageView) findViewById(R.id.back_btn);
        send = (Button) findViewById(R.id.send);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTitle.getText().toString().equals("") || body.getText().toString().equals("")) {
                    Toast.makeText(Send_Message_From_Teacher.this, "제목과 내용을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("secret", secret);
                        jsonObject.put("user_id", user_id);
                        jsonObject.put("title", edtTitle.getText().toString());
                        jsonObject.put("message", body.getText().toString());
                        jsonObject.put("receiver_id", String.valueOf(id));

                        requestJson(Url_define.Common_Send_message + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("statusCode : ", statusCode + "");
                                Log.d("response : ", response.toString());
                                super.onSuccess(statusCode, headers, response);

                                Toast.makeText(getApplication(), "전송완료", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                Log.d("statusCode : ", statusCode + "");
                                super.onFailure(statusCode, headers, res, t);
                            }
                        });
                    } catch (JSONException e) {
                        e.getStackTrace();
                    }
                }

            }
        });
    }
}
