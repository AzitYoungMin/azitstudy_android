package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.trams.azit.view.WheelView;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2015-10-26.
 */
public class Plus_DDay extends ConnActivity {

    EditText et_event_name;
    TextView date, add_event;
    ImageView cancel;
    Typeface tf;
    SharedPreferences myPrefs;
    String secret, user_id;
    WheelView year, month, day;
    int yearMin, yearMax, yearCount, yearResult;
    int dayMin, dayMax, dayCount, dayResult;
    List<String> yearList = new ArrayList<String>();
    List<String> dayList = new ArrayList<String>();
    String months[] = new String[]{"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.plus_dday);


        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        tf = Typeface.createFromAsset(getAssets(), "NanumGothic.ttf");

        Calendar calendar = Calendar.getInstance();
        et_event_name = (EditText) findViewById(R.id.et_event_name);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        date = (TextView) findViewById(R.id.date);
        add_event = (TextView) findViewById(R.id.add_event);

        year = (WheelView) findViewById(R.id.year);
        month = (WheelView) findViewById(R.id.month);
        day = (WheelView) findViewById(R.id.day);

        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;

        yearMin = 2010;
        yearMax = calendar.get(Calendar.YEAR) + 10;
        yearCount = yearMax - yearMin;
        yearResult = yearMin;
        for (int i = 0; yearResult < yearMax; i++) {
            yearList.add(String.valueOf(yearResult++) + "년");
        }

        dayMin = 1;
        dayMax = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        dayCount = dayMax - dayMin;
        dayResult = dayMin;
        for (int i= 0 ; dayResult < dayMax; i ++){
            dayList.add(String.valueOf(dayResult++) + "일");
        }

        year.setOffset(0);
        year.setItems(yearList);
        year.setSeletion(yearList.size()-10);

        month.setOffset(0);
        month.setItems(Arrays.asList(months));
        month.setSeletion(curMonth);

        day.setOffset(0);
        day.setItems(dayList);
        day.setSeletion(curDay);

        date.setText(year.getSeletedItem() + " " + month.getSeletedItem() + " " + day.getSeletedItem());

        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_event_name.getText().toString().equals("")) {
                    Toast.makeText(Plus_DDay.this, "이벤트 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("secret", secret);
                        jsonObject.put("user_id", user_id);
                        jsonObject.put("title", et_event_name.getText().toString());
                        jsonObject.put("date", date.getText().toString().replaceAll(" ", "").replace("년", "/").replace("월", "/").replace("일", ""));

                        Logger.e(secret);
                        Logger.e(user_id);
                        requestJson(Url_define.Student_Plus_DDay + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("statusCode : ", statusCode + "");
                                Log.d("response : ", response.toString());
                                super.onSuccess(statusCode, headers, response);

                                Intent i = new Intent(Plus_DDay.this, DDay_Change.class);
                                startActivity(i);
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


        year.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                date.setText(year.getSeletedItem() + " " + month.getSeletedItem() + " " + day.getSeletedItem());
            }
        });

        month.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                date.setText(year.getSeletedItem() + " " + month.getSeletedItem() + " " + day.getSeletedItem());
            }
        });

        month.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                date.setText(year.getSeletedItem() + " " + month.getSeletedItem() + " " + day.getSeletedItem());
            }
        });


    }

}
