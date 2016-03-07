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
 * Created by Administrator on 2015-10-27.
 */
public class Edit_DDay extends ConnActivity {

    EditText et_event_name;
    TextView date, title, edit_btn, delete_btn;
    ImageView cancel;
    Typeface tf;
    int dday_id;
    SharedPreferences myPrefs;
    String secret, user_id, dday_title;
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
        setContentView(R.layout.edit_dday);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        dday_id = getIntent().getIntExtra("dday_id", 0);
        dday_title = getIntent().getStringExtra("title");

        setButton();
        setWheel();
        setEditBtn();
        setDeleteBtn();
    }

    private void setWheel() {
        Calendar calendar = Calendar.getInstance();

        month = (WheelView) findViewById(R.id.month);
        year = (WheelView) findViewById(R.id.year);
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

    private void setDeleteBtn() {
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("dday_id", String.valueOf(dday_id));

                    Logger.e(secret);
                    Logger.e(user_id);
                    requestJson(Url_define.Student_Delete_DDay + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            Intent i = new Intent(Edit_DDay.this, DDay_Change.class);
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
        });
    }

    private void setEditBtn() {
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("dday_id", String.valueOf(dday_id));
                    jsonObject.put("title", et_event_name.getText().toString());
                    jsonObject.put("date", date.getText().toString().replaceAll(" ", "").replace("년", "/").replace("월", "/").replace("일", ""));

                    Logger.e(secret);
                    Logger.e(user_id);
                    requestJson(Url_define.Student_Edit_DDay + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            Intent i = new Intent(Edit_DDay.this, DDay_Change.class);
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
        });
    }

    private void setButton() {
        et_event_name = (EditText)findViewById(R.id.et_event_name);
        date = (TextView)findViewById(R.id.date);
        title = (TextView)findViewById(R.id.title);
        edit_btn = (TextView)findViewById(R.id.edit_event);
        delete_btn = (TextView)findViewById(R.id.delete_event);
        cancel = (ImageView)findViewById(R.id.cancel);

        tf = Typeface.createFromAsset(getAssets(), "NanumGothic.ttf");
        et_event_name.setText(dday_title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
