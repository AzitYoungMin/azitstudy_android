package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.trams.azit.adapter.RatingAdapter;
import com.trams.azit.customer.HorizontalListView;
import com.trams.azit.model.RatingItem;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-09-25.
 */
public class Record_Student_Grade extends ConnActivity{

    public TextView student_Name;
    public TextView mon, tue, wed, thu, fri, sat, sun;
    public ImageView mock;
    String str_name, image,mock_num;
    int  id, mon_num, tue_num, wed_num, thu_num, fri_num, sat_num, sun_num;
    ImageView back, photo;
    TextView korean, math, english, field,  Record_Student_Study,send_message;
    BarChart bar_chart;
    Typeface tf;
    SharedPreferences myPrefs;
    String secret, user_id;
    LinearLayout graph_ll;
    protected float[] Month_data;
    private List<RatingItem> ratingItemList;
    private RatingAdapter ratingAdapter;

    private int subjectID1 = 10000;
    private int subjectID2 = 30000;
    private int subjectID3 = 50000;
    private int subjectID4 = 70000;

    protected String[] mMonths = new String[]{"3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월"};
    protected String[] mKoreanCate = new String[]{"화법", "작문", "문법", "독서", "문학"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_grade);
        tf = Typeface.createFromAsset(getAssets(), "NanumGothic.ttf");

        myPrefs = getSharedPreferences("Azit", Activity.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        ratingItemList = new ArrayList<>();

        Intent i = getIntent();

        id = i.getIntExtra("id", 0);
        str_name = i.getStringExtra("name");
        image = i.getStringExtra("image");
        mon_num = i.getIntExtra("mon", 0);
        tue_num = i.getIntExtra("tue", 0);
        wed_num = i.getIntExtra("wed",0);
        thu_num = i.getIntExtra("thu",0);
        fri_num = i.getIntExtra("fri", 0);
        sat_num = i.getIntExtra("sat", 0);
        sun_num = i.getIntExtra("sun", 0);
        mock_num = i.getStringExtra("mock");


        back = (ImageView)findViewById(R.id.back_btn);
        korean = (TextView) findViewById(R.id.korean);
        math = (TextView) findViewById(R.id.math);
        english = (TextView) findViewById(R.id.english);
        field = (TextView) findViewById(R.id.field);
        Record_Student_Study = (TextView)findViewById(R.id.my_study);
        send_message = (TextView)findViewById(R.id.message_send);
        photo = (ImageView)findViewById(R.id.photo);
        student_Name = (TextView) findViewById(R.id.student_name);
        graph_ll = (LinearLayout) findViewById(R.id.graph_ll);

        mon = (TextView) findViewById(R.id.mon_image);
        tue = (TextView) findViewById(R.id.tue_image);
        wed = (TextView) findViewById(R.id.wed_image);
        thu = (TextView) findViewById(R.id.thu_image);
        fri = (TextView) findViewById(R.id.fri_image);
        sat = (TextView) findViewById(R.id.sat_image);
        sun = (TextView) findViewById(R.id.sun_image);
        mock = (ImageView) findViewById(R.id.mock_check);

        bar_chart = (BarChart) findViewById(R.id.bar_chart);

        Picasso.with(Record_Student_Grade.this).load(Url_define.BASE_Image+image).into(photo);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        Record_Student_Study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Record_Student_Grade.this, Record_Student_Study.class);
                i.putExtra("name", str_name);
                i.putExtra("id", id);
                i.putExtra("image", image);
                i.putExtra("mon", mon_num);
                i.putExtra("tue", tue_num);
                i.putExtra("wed", wed_num);
                i.putExtra("thu", thu_num);
                i.putExtra("fri", fri_num);
                i.putExtra("sat", sat_num);
                i.putExtra("sun", sun_num);
                i.putExtra("mock", mock_num);
                startActivity(i);
                finish();
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Record_Student_Grade.this,Send_Message_From_Teacher.class);
                i.putExtra("name", str_name);
                i.putExtra("id", id);
                i.putExtra("image", image);
                i.putExtra("mon", mon_num);
                i.putExtra("tue", tue_num);
                i.putExtra("wed", wed_num);
                i.putExtra("thu", thu_num);
                i.putExtra("fri", fri_num);
                i.putExtra("sat", sat_num);
                i.putExtra("sun", sun_num);
                i.putExtra("mock", mock_num);
                startActivity(i);
            }
        });
    }

    private void getDataforMainStudentGrade1(int type) {
        try {

            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("student_id", PreferUtils.getStudentTeacher(getApplication()));
            jsonObject.put("subject_id", type);

            Logger.e(secret);
            Logger.e(user_id);

            requestJson(Url_define.Student_Main_Grade + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getString("result").equals("success")) {
                            graph_ll.removeAllViews();
                            JSONArray data = new JSONArray(response.getString("data"));

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for (int i = 0; i < data.length(); i++) {
                                View GraphView = inflater.from(getApplicationContext()).inflate(R.layout.my_grade_graph, null);

                                JSONObject obj = data.getJSONObject(i);

                                TextView title = (TextView) GraphView.findViewById(R.id.title);
                                TextView title1 = (TextView) GraphView.findViewById(R.id.title1);
                                TextView text1 = (TextView) GraphView.findViewById(R.id.text1);
                                TextView text2 = (TextView) GraphView.findViewById(R.id.text2);
                                TextView text3 = (TextView) GraphView.findViewById(R.id.text3);
                                title.setTypeface(tf);
                                title1.setTypeface(tf);
                                text1.setTypeface(tf);
                                text2.setTypeface(tf);
                                text3.setTypeface(tf);

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

                                ratingAdapter = new RatingAdapter(Record_Student_Grade.this, ratingItemList);
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

}
