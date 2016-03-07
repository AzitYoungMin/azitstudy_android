package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.orhanobut.logger.Logger;
import com.trams.azit.adapter.RatingAdapter;
import com.trams.azit.customer.HorizontalListView;
import com.trams.azit.model.RatingItem;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-09-10.
 */
public class MyGrade extends Fragment implements View.OnClickListener {

    TextView korean, math, english, field, my_study;
    Typeface tf;

    protected String[] mMonths;
    protected float[] Month_data;
    LinearLayout graph_ll;
    SharedPreferences myPrefs;
    String secret, user_id;

    private int subjectID1 = 10000;
    private int subjectID2 = 30000;
    private int subjectID3 = 50000;
    private int subjectID4 = 70000;

    private List<RatingItem> ratingItemList;
    private RatingAdapter ratingAdapter;
    RelativeLayout rlDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_grade, null);
        ratingItemList = new ArrayList<>();
        tf = Typeface.createFromAsset(getActivity().getAssets(), "NanumGothic.ttf");
        myPrefs = getContext().getSharedPreferences("Azit", Activity.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        korean = (TextView) view.findViewById(R.id.korean);
        math = (TextView) view.findViewById(R.id.math);
        english = (TextView) view.findViewById(R.id.english);
        field = (TextView) view.findViewById(R.id.field);
        my_study = (TextView) view.findViewById(R.id.my_study);
        graph_ll = (LinearLayout) view.findViewById(R.id.graph_ll);

        getDataforMainStudentGrade1(subjectID1);


        korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataforMainStudentGrade1(subjectID1);
                korean.setBackgroundResource(R.drawable.subject_icon_push);
                math.setBackgroundResource(R.drawable.subject_icon);
                english.setBackgroundResource(R.drawable.subject_icon);
                field.setBackgroundResource(R.drawable.subject_icon);
            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataforMainStudentGrade1(subjectID2);
                korean.setBackgroundResource(R.drawable.subject_icon);
                math.setBackgroundResource(R.drawable.subject_icon_push);
                english.setBackgroundResource(R.drawable.subject_icon);
                field.setBackgroundResource(R.drawable.subject_icon);
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataforMainStudentGrade1(subjectID3);
                korean.setBackgroundResource(R.drawable.subject_icon);
                math.setBackgroundResource(R.drawable.subject_icon);
                english.setBackgroundResource(R.drawable.subject_icon_push);
                field.setBackgroundResource(R.drawable.subject_icon);
            }
        });
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataforMainStudentGrade1(subjectID4);
                korean.setBackgroundResource(R.drawable.subject_icon);
                math.setBackgroundResource(R.drawable.subject_icon);
                english.setBackgroundResource(R.drawable.subject_icon);
                field.setBackgroundResource(R.drawable.subject_icon_push);
            }
        });

        my_study.setOnClickListener(this);

        rlDetail = (RelativeLayout) view.findViewById(R.id.detail_btn);
        rlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentMainActivity) getActivity()).onMenuItemClick(5);
            }
        });

        return view;
    }


    private void setBarChart(BarChart bar_chart) {
        bar_chart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        bar_chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        bar_chart.setPinchZoom(false);
        bar_chart.setTouchEnabled(false);

        bar_chart.setDrawGridBackground(false);
        bar_chart.setDrawBarShadow(false);

        bar_chart.setDrawValueAboveBar(true);

        // change the position of the y-labels
        YAxis yLabels = bar_chart.getAxisLeft();
//        yLabels.setAxisMaxValue(500f);
        yLabels.setValueFormatter(null);
        yLabels.setLabelCount(2, false);
        yLabels.setDrawGridLines(false);
        yLabels.setTypeface(tf);

        ValueFormatter vf = new ValueFormatter() {

            private DecimalFormat mFormat = new DecimalFormat(("#######"));

            @Override
            public String getFormattedValue(float v) {
                return mFormat.format(v);
            }
        };

        yLabels.setValueFormatter(vf);
        bar_chart.getAxisRight().setEnabled(false);

        XAxis xLabels = bar_chart.getXAxis();
        xLabels.setTypeface(tf);
        xLabels.setDrawGridLines(false);
        xLabels.setTextSize(10);
        xLabels.setSpaceBetweenLabels(1);
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);


        bar_chart.animateY(2000);

        Legend l = bar_chart.getLegend();
        l.setEnabled(false);
    }


    private void setBarChartData(int count, float[] Month_data, BarChart bar_chart) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {

            xVals.add(mMonths[i]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < Month_data.length; i++) {
            yVals1.add(new BarEntry(new float[]{Month_data[i]}, i));
        }
        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColor(Color.parseColor("#1FB4FF"));
        set1.setValueTypeface(tf);
        set1.setBarSpacePercent(30);
        set1.setValueTextSize(10);
        set1.setValueTextColor(Color.BLACK);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        ValueFormatter vf = new ValueFormatter() {

            private DecimalFormat mFormat = new DecimalFormat(("#######.#"));

            @Override
            public String getFormattedValue(float v) {
                if (v == 0) {
                    return "";
                } else {
                    return mFormat.format(v);
                }
            }
        };

        data.setValueFormatter(vf);

        bar_chart.setData(data);
        bar_chart.invalidate();
    }


    @Override
    public void onClick(View v) {
        if (v == my_study) {
            ((StudentMainActivity) getActivity()).onMenuItemClick(1);
        }
    }

    private void getDataforMainStudentGrade1(int type) {
        try {

            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("student_id", Integer.valueOf(user_id));
            jsonObject.put("subject_id", type);

            Logger.e(secret);
            Logger.e(user_id);

            NetworkHelper.requestJson(getContext(), Url_define.Student_Main_Grade + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getString("result").equals("success")) {
                            graph_ll.removeAllViews();
                            JSONArray data = new JSONArray(response.getString("data"));

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for (int i = 0; i < data.length(); i++) {
                                View GraphView = inflater.from(getActivity()).inflate(R.layout.my_grade_graph, null);

                                JSONObject obj = data.getJSONObject(i);

                                TextView title = (TextView) GraphView.findViewById(R.id.title);
                                TextView title1 = (TextView) GraphView.findViewById(R.id.title1);

                                title.setText(obj.getString("title") + " 등급");
                                title1.setText(obj.getString("title") + " 전국 백분위");

                                HorizontalListView lsvRating = (HorizontalListView) GraphView.findViewById(R.id.lsvRating);
                                BarChart bar_chart = (BarChart) GraphView.findViewById(R.id.bar_chart);

                                JSONArray jsonArray = obj.getJSONArray("percentiles");
                                mMonths = new String[jsonArray.length()];
                                Month_data = new float[jsonArray.length()];
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    mMonths[j] = jsonObject1.getString("month") + "월";
                                    Month_data[j] = Float.valueOf(jsonObject1.getString("percentile"));
                                }
                                setBarChartData(mMonths.length, Month_data, bar_chart);


                                JSONArray jsonArrRating = obj.getJSONArray("ratings");
                                List<RatingItem> ratingItemsRes = new ArrayList<RatingItem>();
                                for (int k = 0; k < jsonArrRating.length(); k++) {
                                    JSONObject jsonObj = jsonArrRating.getJSONObject(k);
                                    RatingItem ratingItem = new RatingItem();
                                    ratingItem.setMonth(jsonObj.getString("month"));
                                    ratingItem.setStandard(jsonObj.getString("standard"));
                                    ratingItem.setOrigin(jsonObj.getString("origin"));
                                    ratingItem.setRating(jsonObj.getString("rating"));
                                    ratingItemsRes.add(ratingItem);
                                }

                                ratingAdapter = new RatingAdapter(getActivity(), ratingItemList);
                                lsvRating.setAdapter(ratingAdapter);
                                setBarChart(bar_chart);
                                updateListRating(ratingItemsRes);

                                graph_ll.addView(GraphView);
                            }

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

    private void updateListRating(List<RatingItem> ratingItemLists) {
        ratingItemList.clear();
        for (RatingItem item : ratingItemLists) {
            ratingItemList.add(item);
        }
        ratingAdapter.notifyDataSetChanged();
    }

}
