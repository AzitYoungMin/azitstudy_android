package com.trams.azit.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;
import com.trams.azit.view.WheelView;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by zin9x on 1/13/2016.
 */
public class TimeModify extends ConnActivity {

    ImageView btn_close;
    EditText page_from, page_to;
    WheelView startHourWheel, startMinuteWheel, endHourWheel, endMinuteWheel;
    Button next_btn;
    SharedPreferences myPrefs;
    int activity_id, activity_type;
    String start_time, end_time, duration, secret, user_id, date, start_page, end_page, start_time_hour, start_time_minute, end_time_hour, end_time_minute;
    private static final String[] HOUR = new String[]{"1시", "2시", "3시", "4시", "5시", "6시", "7시", "8시", "9시", "10시", "11시", "12시", "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시", "24시"};
    private static final String[] MINUTE = new String[]{"1분", "2분", "3분", "4분", "5분", "6분", "7분", "8분", "9분", "10분", "11분", "12분", "13분", "14분", "15분", "16분", "17분", "18분", "19분", "20분", "21분", "22분", "23분", "24분", "25분", "26분", "27분", "28분", "29분", "30분", "31분", "32분", "33분", "34분", "35분", "36분", "37분", "38분", "39분", "40분", "41분", "42분", "43분", "44분", "45분", "46분", "47분", "48분", "49분", "50분", "51분", "52분", "53분", "54분", "55분", "56분", "57분", "58분", "59분", "60분"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.dialog_timemodify);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        myPrefs = getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        activity_id = getIntent().getIntExtra("activity_id", 0);
        activity_type = getIntent().getIntExtra("activity_type", 0);
        start_time = getIntent().getStringExtra("start_time");
        end_time = getIntent().getStringExtra("end_time");
        duration = getIntent().getStringExtra("duration");
        date = getIntent().getStringExtra("date");
        start_page = getIntent().getStringExtra("start_page");
        end_page = getIntent().getStringExtra("end_page");

        String[] startTime = start_time.split(":");
        String[] endTime = end_time.split(":");

        start_time_hour = startTime[0];
        start_time_minute = startTime[1];

        end_time_hour = endTime[0];
        end_time_minute = endTime[1];

        Log.e("start_time_hour", start_time_hour);
        Log.e("start_time_minute", start_time_minute);
        Log.e("end_time_hour", end_time_hour);
        Log.e("end_time_minute", end_time_minute);

        btn_close = (ImageView) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startHourWheel = (WheelView) findViewById(R.id.startHourWheel);
        startHourWheel.setOffset(0);
        startHourWheel.setItems(Arrays.asList(HOUR));
        startHourWheel.setSeletion(Integer.parseInt(start_time_hour) - 1);

        startMinuteWheel = (WheelView) findViewById(R.id.startMinuteWheel);
        startMinuteWheel.setOffset(0);
        startMinuteWheel.setItems(Arrays.asList(MINUTE));
        startMinuteWheel.setSeletion(Integer.parseInt(start_time_minute) - 1);

        endHourWheel = (WheelView) findViewById(R.id.endHourWheel);
        endHourWheel.setOffset(0);
        endHourWheel.setItems(Arrays.asList(HOUR));
        endHourWheel.setSeletion(Integer.parseInt(end_time_hour) - 1);

        endMinuteWheel = (WheelView) findViewById(R.id.endMinuteWheel);
        endMinuteWheel.setOffset(0);
        endMinuteWheel.setItems(Arrays.asList(MINUTE));
        endMinuteWheel.setSeletion(Integer.parseInt(end_time_minute) - 1);

        page_from = (EditText) findViewById(R.id.page_from);
        page_from.setText(start_page);
        page_to = (EditText) findViewById(R.id.page_to);
        page_to.setText(end_page);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyTime();
            }
        });

    }

    private void ModifyTime() {
        if (page_from.getText().toString().equals("") || page_to.getText().toString().equals("")) {
            Toast.makeText(TimeModify.this, "페이지를 설정해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("secret", secret);
                jsonObject.put("user_id", user_id);
                jsonObject.put("start_time", startHourWheel.getSeletedItem().replace("시", ":") + startMinuteWheel.getSeletedItem().replace("분", ":") + "00");
                jsonObject.put("end_time", endHourWheel.getSeletedItem().replace("시", ":") + endMinuteWheel.getSeletedItem().replace("분", ":") + "00");
                jsonObject.put("activity_id", activity_id);
                jsonObject.put("duration", makeDuration());
                jsonObject.put("activity_type", activity_type);
                jsonObject.put("start_page", Integer.parseInt(page_from.getText().toString()));
                jsonObject.put("end_page", Integer.parseInt(page_to.getText().toString()));
                jsonObject.put("date", date);

                Log.e("jsonObject", jsonObject.toString());

                requestJson(Url_define.Student_UPDATE_My_Time + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("statusCode : ", statusCode + "");
                        Log.d("response : ", response.toString());
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getString("result").equals("success")) {
                                Toast.makeText(TimeModify.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private String makeDuration() {
        String duration;
        String startHour = startHourWheel.getSeletedItem().replace("시", "");
        String startMnute = startMinuteWheel.getSeletedItem().replace("분", "");
        String endHour = endHourWheel.getSeletedItem().replace("시", "");
        String endMinute = endMinuteWheel.getSeletedItem().replace("분", "");

        int StartHour = Integer.parseInt(startHour);
        int StartMinute = Integer.parseInt(startMnute);
        int EndHour = Integer.parseInt(endHour);
        int EndMinute = Integer.parseInt(endMinute);

        int TotalStartMinute = StartHour * 60 + StartMinute;
        int TotalEndMinute = EndHour * 60 + EndMinute;

        int durationTime;
        if ((TotalEndMinute - TotalStartMinute) < 0) {
            durationTime = (24 * 60) - TotalStartMinute + TotalEndMinute;
        } else {
            durationTime = TotalEndMinute - TotalStartMinute;
        }

        duration = String.format("%02d", durationTime / 60) + ":" + String.format("%02d", durationTime % 60) + ":" + "00";

        return duration;

    }
}
