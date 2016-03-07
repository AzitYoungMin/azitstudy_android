package com.trams.azit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.trams.azit.R;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.Url_define;
import com.trams.azit.view.WheelView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Administrator on 21/01/2016.
 */
public class SettingGoalDialog extends Dialog {

    public interface OnCompleteListener {
        public void onComple();
    }

    private OnCompleteListener onCompleteListener;

    public OnCompleteListener getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    private TextView week_study, complete;
    private ToggleButton toggle_push;
    private ImageView cancel;
    private Typeface tf;
    private int hour_time, mins_time;
    private SharedPreferences myPrefs;
    WheelView hours, mins;
    private static final String[] HOUR = new String[]{"0시간", "1시간", "2시간", "3시간", "4시간", "5시간", "6시간", "7시간", "8시간", "9시간", "10시간", "11시간", "12시간", "13시간", "14시간", "15시간", "16시간", "17시간", "18시간", "19시간", "20시간", "21시간", "22시간", "23시간"};
    private static final String[] MINUTE = new String[]{"0분", "1분", "2분", "3분", "4분", "5분", "6분", "7분", "8분", "9분", "10분", "11분", "12분", "13분", "14분", "15분", "16분", "17분", "18분", "19분", "20분", "21분", "22분", "23분", "24분", "25분", "26분", "27분", "28분", "29분", "30분", "31분", "32분", "33분", "34분", "35분", "36분", "37분", "38분", "39분", "40분", "41분", "42분", "43분", "44분", "45분", "46분", "47분", "48분", "49분", "50분", "51분", "52분", "53분", "54분", "55분", "56분", "57분", "58분", "59분"};

    public SettingGoalDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_goal);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        setButton();
        setWheel();
        LoadFromServer();

    }

    private void setButton() {
        cancel = (ImageView) findViewById(R.id.cancel);
        week_study = (TextView) findViewById(R.id.week_study);
        complete = (TextView) findViewById(R.id.complete);
        toggle_push = (ToggleButton) findViewById(R.id.toggle_push);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideView();
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

    private void setWheel() {

        hours = (WheelView) findViewById(R.id.hour);
        hours.setOffset(0);
        hours.setItems(Arrays.asList(HOUR));
        mins = (WheelView) findViewById(R.id.mins);
        mins.setOffset(0);
        mins.setItems(Arrays.asList(MINUTE));


        hours.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                hour_time = Integer.parseInt(hours.getSeletedItem().replace("시간", "")) * 7;
                mins_time = (Integer.parseInt(mins.getSeletedItem().replace("분", "")) * 7) % 60;
                hour_time = hour_time + ((Integer.parseInt(mins.getSeletedItem().replace("분", "")) * 7) / 60);

                week_study.setText(hour_time + "시간" + mins_time + "분");
            }
        });

        mins.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                hour_time = Integer.parseInt(hours.getSeletedItem().replace("시간", "")) * 7;
                mins_time = (Integer.parseInt(mins.getSeletedItem().replace("분", "")) * 7) % 60;
                hour_time = hour_time + ((Integer.parseInt(mins.getSeletedItem().replace("분", "")) * 7) / 60);

                week_study.setText(hour_time + "시간" + mins_time + "분");
            }
        });

    }

    void request() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(getContext()));
            jsonObject.put("user_id", PreferUtils.getUserId(getContext()));
            jsonObject.put("hour", hour_time);
            jsonObject.put("minute", mins_time);
            jsonObject.put("alarm", toggle_push.isChecked());

            NetworkHelper.requestJson(getContext(), Url_define.STUDENT_GOAL_SAVE + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideView();
                    if (onCompleteListener != null) onCompleteListener.onComple();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
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
            jsonObject.put("secret", PreferUtils.getSecret(getContext()));
            jsonObject.put("user_id", PreferUtils.getUserId(getContext()));

            NetworkHelper.requestJson(getContext(), Url_define.STUDENT_GOAL + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }


    /**
     * Show view
     */
    public void showView() {
        if (!this.isShowing()) {
            this.show();
        }
    }

    /**
     * hide view
     */
    public void hideView() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

}

