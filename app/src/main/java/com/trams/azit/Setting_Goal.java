package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.trams.azit.view.WheelView;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Administrator on 2015-10-27.
 */
public class Setting_Goal extends ConnActivity {

    TextView week_study, complete;
    ToggleButton toggle_push;
    ImageView cancel;
    Typeface tf;
    int hour_time, mins_time;
    SharedPreferences myPrefs;
    String secret, user_id;
    WheelView hours, mins;
    private static final String[] HOUR = new String[]{"0시간", "1시간", "2시간", "3시간", "4시간", "5시간", "6시간", "7시간", "8시간", "9시간", "10시간", "11시간", "12시간", "13시간", "14시간", "15시간", "16시간", "17시간", "18시간", "19시간", "20시간", "21시간", "22시간", "23시간"};
    private static final String[] MINUTE = new String[]{"0분","1분", "2분", "3분", "4분", "5분", "6분", "7분", "8분", "9분", "10분", "11분", "12분", "13분", "14분", "15분", "16분", "17분", "18분", "19분", "20분", "21분", "22분", "23분", "24분", "25분", "26분", "27분", "28분", "29분", "30분", "31분", "32분", "33분", "34분", "35분", "36분", "37분", "38분", "39분", "40분", "41분", "42분", "43분", "44분", "45분", "46분", "47분", "48분", "49분", "50분", "51분", "52분", "53분", "54분", "55분", "56분", "57분", "58분", "59분"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.setting_goal);

        tf = Typeface.createFromAsset(getAssets(), "NanumGothic.ttf");
        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        setButton();
        setWheel();
        LoadFromServer();
    }

    private void setWheel() {

        hours = (WheelView) findViewById(R.id.hour);
        hours.setOffset(0);
        hours.setItems(Arrays.asList(HOUR));
        mins = (WheelView) findViewById(R.id.mins);
        mins.setOffset(0);
        mins.setItems(Arrays.asList(MINUTE));


        hours.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);

            }
        });

        mins.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);

            }
        });

    }


    private void setButton() {
        cancel = (ImageView) findViewById(R.id.cancel);
        week_study = (TextView) findViewById(R.id.week_study);
        complete = (TextView) findViewById(R.id.complete);
        toggle_push = (ToggleButton) findViewById(R.id.toggle_push);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                request();
            }
        });

    }

    void request() {
        try {
            SharedPreferences myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
            String secret = myPrefs.getString("secret", "");
            String user_id = myPrefs.getString("user_id", "");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("hour", hour_time);
            jsonObject.put("minute", mins_time);
            jsonObject.put("alarm", toggle_push.isChecked());


            Logger.e(secret);
            Logger.e(user_id);

            Log.d("json object  : ", jsonObject.toString() + "");
            Log.d("url : ", Url_define.STUDENT_GOAL_SAVE + Url_define.KEY + "");
            NetworkHelper.requestJson(this, Url_define.STUDENT_GOAL_SAVE + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);
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

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.STUDENT_GOAL + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        int responseHours = Integer.valueOf(response.getString("hour"));
                        int responseMin = Integer.valueOf(response.getString("minute"));
                        boolean responseAlarm = Boolean.getBoolean(response.getString("alarm"));

                        hours.setSeletion(responseHours);
                        mins.setSeletion(responseMin);
                        toggle_push.setChecked(responseAlarm);

                        hour_time = Integer.parseInt(hours.getSeletedItem().replace("시간", "")) * 7;
                        mins_time = (Integer.parseInt(mins.getSeletedItem().replace("분", "")) * 7) % 60;
                        hour_time = hour_time + ((Integer.parseInt(mins.getSeletedItem().replace("분", "")) * 7) / 60);

                        week_study.setText(hour_time + "시간" + mins_time + "분");
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
