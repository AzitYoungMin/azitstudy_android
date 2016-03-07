package com.trams.azit.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trams.azit.Mento_Act;
import com.trams.azit.Mento_Profile;
import com.trams.azit.R;
import com.trams.azit.activity.MentoMainActivity;
import com.trams.azit.activity.MyPointActivity;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-03.
 */
public class Mento_Home extends ConnFragment implements View.OnClickListener {

    TextView profile, mento_name, mento_univercity, mento_introduce, mento_point, mento_num_answer, mento_grade, mento_point_home;
    RelativeLayout my_point;
    LinearLayout my_answer_count, menti, mentoring, clinic;
    JSONObject jsonObject;
    JSONObject resData;
    ImageView main_image;
    SharedPreferences mPref;

    @Override
    public void onResume() {
        LoadFromServer();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mento_home, null);

        mPref = getActivity().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        profile = (TextView) view.findViewById(R.id.profile_change);
        my_point = (RelativeLayout) view.findViewById(R.id.my_point);
        my_answer_count = (LinearLayout) view.findViewById(R.id.my_answer_count);
        menti = (LinearLayout) view.findViewById(R.id.menti);
        mentoring = (LinearLayout) view.findViewById(R.id.mentoring);
        clinic = (LinearLayout) view.findViewById(R.id.azit_clinic);

        mento_name = (TextView) view.findViewById(R.id.mento_name);
        mento_univercity = (TextView) view.findViewById(R.id.mento_univercity);
        mento_introduce = (TextView) view.findViewById(R.id.mento_introduce);
        mento_point = (TextView) view.findViewById(R.id.mento_point);
        mento_point_home = (TextView) view.findViewById(R.id.mento_point_home);
        mento_num_answer = (TextView) view.findViewById(R.id.mento_num_answer);
        mento_grade = (TextView) view.findViewById(R.id.mento_grade);
        main_image = (ImageView) view.findViewById(R.id.main_image);


        profile.setOnClickListener(this);
        my_answer_count.setOnClickListener(this);
        my_point.setOnClickListener(this);
        menti.setOnClickListener(this);
        mentoring.setOnClickListener(this);
        clinic.setOnClickListener(this);


//        LoadFromServer();
        return view;
    }

    private void LoadFromServer() {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", mPref.getString("user_id", "default value"));
            jsonObject.put("secret", mPref.getString("secret", "default value"));

            requestJson(Url_define.BASE + "/api/mentor/info" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    super.onSuccess(statusCode, headers, res);
                    Log.d("statusCode", statusCode + "");
                    Log.d("onSuccess res", res.toString());
                    resData = res;
                    Log.d("resdata", resData.toString());
                    try {
                        mento_name.setText(resData.getString("name"));
                        mento_univercity.setText(resData.getString("university"));
                        mento_introduce.setText(resData.getString("introduce"));
                        mento_point.setText(resData.getString("point") + "pt");
                        mento_point_home.setText(resData.getString("point") + "pt");
                        mento_num_answer.setText(resData.getString("num_of_answer"));
                        mento_grade.setText(resData.getString("grade"));
                        Picasso.with((MentoMainActivity) getActivity()).load(Url_define.BASE_Image + resData.getString("profile_image")).fit().into(main_image);
                    } catch (JSONException e) {
                        e.getStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                    if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                    Log.d("statusCode", statusCode + "");
                    Log.d("throwable", String.valueOf(t.getMessage()) == null ? String.valueOf(t.getMessage()) : "");
                    Log.d("onFailure res", res.toString());
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == my_point) {
//            ((MentoMainActivity) getActivity()).onMenuItemClick(1);

            Intent i = new Intent(getActivity(), MyPointActivity.class);
            try {
                i.putExtra("point", resData.getString("point"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(i);

        } else if (v == menti) {
            ((MentoMainActivity) getActivity()).onMenuItemClick(2);
        } else if (v == mentoring) {
            ((MentoMainActivity) getActivity()).onMenuItemClick(1);
        } else if (v == clinic) {
            ((MentoMainActivity) getActivity()).onMenuItemClick(2);
        } else if (v == my_answer_count) {
            Intent i = new Intent(getActivity(), Mento_Act.class);
            startActivity(i);
        } else if (v == profile) {
            Intent i = new Intent(getActivity(), Mento_Profile.class);
            startActivity(i);
        }
    }
}
