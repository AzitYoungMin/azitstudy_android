package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.trams.azit.dialog.SettingGoalDialog;
import com.trams.azit.model.StudentChartModel;
import com.trams.azit.model.StudentDailyChartModel;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.DateTimeUtils;
import com.trams.azit.util.Url_define;
import com.trams.azit.view.CircleView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2015-09-08.
 */
public class PageFragment extends Fragment {

    private static final String TAG = PageFragment.class.getName();

    protected String[] mParties = new String[]{"교과", "비교과", "잠", "식사", "휴식", "기타", "버리는시간"};
    protected String[] mContents = new String[]{"문학", "비문학", "화법,작문", "문법"};
    protected String[] mMethod = new String[]{"모의고사", "학교수업", "개념", "오답", "학원"};
    protected String[] mTextbook = new String[]{"기출", "EBS", "개념서"};
    protected String[] mDays = new String[]{"", "일", "월", "화", "수", "목", "금", "토", ""};
    protected String[] mWeeks = new String[]{"저번주", "이번주"};
    List<String> contentsname;
    List<Integer> contents_data, method_data, textbook_data;
    List<Integer> korean_contents_data, math_contents_data, english_contents_data;
    List<Integer> korean_method_data, math_method_data, english_method_data;
    List<Integer> korean_textbook_data, math_textbook_data, english_textbook_data;
    static JSONArray Total_field_method_data, Total_field_textbook_data;

    private PieChart my_day_graph, top_day_graph, this_subject_graph, contents_graph, method_graph, textbook_graph;
    private LineChart this_week_graph;
    private BarChart bar_chart;
    TextView korean, math, english, field, total_title, total_study_time, tv_content1, tv_content2, tv_content3, tv_content4, tv_content5, tv_content6, tv_method_time1, tv_method_time2, tv_method_time3, tv_method_time4, tv_textbook_time1, tv_textbook_time2, tv_textbook_time3, tv_textbook_time4, tv_textbook_time5, tv_textbook_time6, tv_textbook_time7;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int index;
    private Typeface tf;
    LinearLayout totla_ll, part_ll, method_ll, textbook_ll, content5_ll, content6_ll;
    TextView study_time, setting, my_rank, tv_content1_name, tv_content2_name, tv_content3_name, tv_content4_name, tv_content5_name, tv_content6_name;
    ImageView img_setting;

    private CircleView circleView;
    private StudentChartModel studentChartModel = new StudentChartModel();

    private ProgressBar pbStudy, pbRank, pbGap;
    private TextView tvPbStudy, tvPbRank, tvPbGap, tvPbRankTitle;

    private TextView tvSubjectRatio, tvAloneTimeRatio;

    private TextView tvCompareWeek;

    private String currentDay;
    private StudentDailyChartModel studentDailyChartModel;
    private ImageView imgNext, imgPrevious;
    private TextView tvCurrentDay;

    private TextView tvThisWeekLearning;
    private TextView tvDetail;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        index = data.getInt(ARG_PAGE);

    }

    private void getDataDailyChart() {
        try {
            LogUtils.d(TAG, "getDataDailyChart start");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("student_id", Integer.valueOf(PreferUtils.getUserId(getActivity())));
            jsonObject.put("date", currentDay);

            NetworkHelper.requestJson(getActivity(), Url_define.STUDENT_DAILY_DATA + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        studentDailyChartModel = new StudentDailyChartModel();

                        studentDailyChartModel.setSubject(Integer.valueOf(response.getString("subject")));
                        studentDailyChartModel.setNonSubject(Integer.valueOf(response.getString("nonsubject")));
                        studentDailyChartModel.setSleep(Integer.valueOf(response.getString("sleep")));
                        studentDailyChartModel.setEat(Integer.valueOf(response.getString("eat")));
                        studentDailyChartModel.setRest(Integer.valueOf(response.getString("rest")));
                        studentDailyChartModel.setEtc(Integer.valueOf(response.getString("etc")));
                        studentDailyChartModel.setWaste(Integer.valueOf(response.getString("waste")));

                        studentDailyChartModel.setHighRankSubject(Integer.valueOf(response.getString("high_rank_subject")));
                        studentDailyChartModel.setHighRankNonSubject(Integer.valueOf(response.getString("high_rank_nonsubject")));
                        studentDailyChartModel.setHighRankSleep(Integer.valueOf(response.getString("high_rank_sleep")));
                        studentDailyChartModel.setHighRankEat(Integer.valueOf(response.getString("high_rank_eat")));
                        studentDailyChartModel.setHighRankRest(Integer.valueOf(response.getString("high_rank_rest")));
                        studentDailyChartModel.setHighRankEtc(Integer.valueOf(response.getString("high_rank_etc")));
                        studentDailyChartModel.setHighRankWaster(Integer.valueOf(response.getString("high_rank_waste")));

                        setMyDayGraph();
                        setTopDayGraph();

                    } catch (Exception e) {
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

    private void getDataChart() {
        try {
            LogUtils.d(TAG, "getDataChart start");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("student_id", Integer.valueOf(PreferUtils.getUserId(getActivity())));

            NetworkHelper.requestJson(getActivity(), Url_define.STUDENT_ANALYTIC_ALL + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        studentChartModel.setTotalStudyTime(response.getString("total_study_time"));

                        studentChartModel.setHeightRankStudyTime(response.getString("high_rank_study_time"));
                        studentChartModel.setHeightRankGoalTime(response.getString("hign_rank_goal_time"));

                        studentChartModel.setGap(response.getString("gap"));

                        studentChartModel.setThisWeekKorean(Long.valueOf(response.getString("this_week_korean")));
                        studentChartModel.setThisWeekMath(Long.valueOf(response.getString("this_week_math")));
                        studentChartModel.setThisWeekEnglisth(Long.valueOf(response.getString("this_week_english")));
                        studentChartModel.setThisWeekSocialScience(Long.valueOf(response.getString("this_week_social_science")));
                        studentChartModel.setThisWeekNonSubject(Long.valueOf(response.getString("this_week_nonsubject")));

                        studentChartModel.setSubjectNonSubject(response.getString("subject_nonsubject"));
                        studentChartModel.setStudyAlone(response.getString("study_alone"));

                        studentChartModel.setLastWeekKorean(Long.valueOf(response.getString("last_week_korean")));
                        studentChartModel.setLastWeekMath(Long.valueOf(response.getString("last_week_math")));
                        studentChartModel.setLastWeekEnglish(Long.valueOf(response.getString("last_week_english")));
                        studentChartModel.setLastWeekSocialScience(Long.valueOf(response.getString("last_week_social_science")));
                        studentChartModel.setLastWeekNonSubject(Long.valueOf(response.getString("last_week_nonsubject")));

                        studentChartModel.setDailySunday(Long.valueOf(response.getString("daily_sunday")));
                        studentChartModel.setDailyMonday(Long.valueOf(response.getString("daily_monday")));
                        studentChartModel.setDailyTuesday(Long.valueOf(response.getString("daily_tuesday")));
                        studentChartModel.setDailyWednesday(Long.valueOf(response.getString("daily_wednesday")));
                        studentChartModel.setDailyThursday(Long.valueOf(response.getString("daily_thursday")));
                        studentChartModel.setDailyFriday(Long.valueOf(response.getString("daily_friday")));
                        studentChartModel.setDailySaturday(Long.valueOf(response.getString("daily_saturday")));

                        studentChartModel.setDailyHeightRankSunday(Long.valueOf(response.getString("daily_high_rank_sunday")));
                        studentChartModel.setDailyHeightRankMonday(Long.valueOf(response.getString("daily_high_rank_monday")));
                        studentChartModel.setDailyHeightRankTuesday(Long.valueOf(response.getString("daily_high_rank_tuesday")));
                        studentChartModel.setDailyHeightRankWednesday(Long.valueOf(response.getString("daily_high_rank_wednesday")));
                        studentChartModel.setDailyHeightRankThursday(Long.valueOf(response.getString("daily_high_rank_thursday")));
                        studentChartModel.setDailyHeightRankFriday(Long.valueOf(response.getString("daily_high_rank_friday")));
                        studentChartModel.setDailyHeightRankSaturday(Long.valueOf(response.getString("daily_high_rank_saturday")));

                        studentChartModel.setGoalTime(response.getString("goal_time"));
                        studentChartModel.setGoalAchieveRatio(Integer.valueOf(response.getString("goal_achieve_ratio")));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    updateWeeklyChart();
                    updateProgressBar();
                    setThisSubjectGraph();

                    setThisWeekGraph();
                    setBarChart();

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

    private void updateWeeklyChart() {
        circleView.updateView(studentChartModel.getGoalTime(), studentChartModel.getTotalStudyTime());
    }

    private void updateProgressBar() {

        if (DateTimeUtils.getSecondsFromDate(studentChartModel.getGoalTime()) != 0) {
            int progressValue = (int) (100 * (float) DateTimeUtils.getSecondsFromDate(studentChartModel.getTotalStudyTime()) / (float) DateTimeUtils.getSecondsFromDate(studentChartModel.getGoalTime()));
            com.trams.azit.util.LogUtils.d(TAG, "pbStudy : " + progressValue);
            pbStudy.setProgress(progressValue);
        }
        tvPbStudy.setText(studentChartModel.getTotalStudyTime());

        if (DateTimeUtils.getSecondsFromDate(studentChartModel.getHeightRankGoalTime()) != 0) {
            int progressValue = (int) (100 * (float) DateTimeUtils.getSecondsFromDate(studentChartModel.getHeightRankStudyTime()) / (float) DateTimeUtils.getSecondsFromDate(studentChartModel.getHeightRankGoalTime()));
            com.trams.azit.util.LogUtils.d(TAG, "pbRank : " + progressValue);
            pbRank.setProgress((progressValue));
        }
        tvPbRank.setText(studentChartModel.getHeightRankStudyTime());

        long timeGap = DateTimeUtils.getSecondsFromDate(studentChartModel.getGap());

        pbGap.setProgress((int) (timeGap / 3600));
        tvPbGap.setText(DateTimeUtils.getDateFromSeconds(timeGap));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        tf = Typeface.createFromAsset(getActivity().getAssets(), "NanumGothic.ttf");

        if (index == 0) {

            v = inflater.inflate(R.layout.tab_total, container, false);
            this_subject_graph = (PieChart) v.findViewById(R.id.this_subject_graph);

            my_day_graph = (PieChart) v.findViewById(R.id.my_day_graph);
            top_day_graph = (PieChart) v.findViewById(R.id.top_ten_day_graph);
            this_week_graph = (LineChart) v.findViewById(R.id.this_week_chart);
            bar_chart = (BarChart) v.findViewById(R.id.bar_chart);
            study_time = (TextView) v.findViewById(R.id.study_time);
            setting = (TextView) v.findViewById(R.id.setting);
            img_setting = (ImageView) v.findViewById(R.id.img_setting);

            circleView = (CircleView) v.findViewById(R.id.circle_view_graph);

            pbStudy = (ProgressBar) v.findViewById(R.id.pb_study);
            pbRank = (ProgressBar) v.findViewById(R.id.pb_rank);
            pbGap = (ProgressBar) v.findViewById(R.id.pb_gap);

            tvPbStudy = (TextView) v.findViewById(R.id.tv_study);
            tvPbRank = (TextView) v.findViewById(R.id.tv_rank);
            tvPbGap = (TextView) v.findViewById(R.id.tv_gap);

            tvPbRankTitle = (TextView) v.findViewById(R.id.tv_rank_title);

            tvSubjectRatio = (TextView) v.findViewById(R.id.tvSubjectRatio);
            tvAloneTimeRatio = (TextView) v.findViewById(R.id.tvAloneTimeRatio);

            tvCompareWeek = (TextView) v.findViewById(R.id.tv_compare_week);

            imgNext = (ImageView) v.findViewById(R.id.img_next_day);
            imgPrevious = (ImageView) v.findViewById(R.id.img_previous_day);
            tvCurrentDay = (TextView) v.findViewById(R.id.tv_day);

            currentDay = DateTimeUtils.getToday();
            tvCurrentDay.setText(currentDay);

            tvThisWeekLearning = (TextView) v.findViewById(R.id.tv_this_week_learning);

            imgNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDay = DateTimeUtils.getNextDate(currentDay);
                    tvCurrentDay.setText(currentDay);
                    getDataDailyChart();
                }
            });

            imgPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDay = DateTimeUtils.getPreviousDate(currentDay);
                    tvCurrentDay.setText(currentDay);
                    getDataDailyChart();
                }
            });

            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent((StudentMainActivity) getActivity(), Setting_Goal.class);
//                    startActivity(i);

                    SettingGoalDialog settingGoalDialog = new SettingGoalDialog(getActivity());
                    settingGoalDialog.setOnCompleteListener(new SettingGoalDialog.OnCompleteListener() {
                        @Override
                        public void onComple() {
                            getDataChart();
                            getDataDailyChart();
                        }
                    });
                    settingGoalDialog.showView();

                }
            });

            img_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent((StudentMainActivity) getActivity(), Setting_Goal.class);
//                    startActivity(i);

                    SettingGoalDialog settingGoalDialog = new SettingGoalDialog(getActivity());
                    settingGoalDialog.setOnCompleteListener(new SettingGoalDialog.OnCompleteListener() {
                        @Override
                        public void onComple() {
                            getDataChart();
                            getDataDailyChart();
                        }
                    });
                    settingGoalDialog.showView();

                }
            });

            tvDetail = (TextView) v.findViewById(R.id.detail_btn);
            tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((StudentMainActivity) getActivity()).onMenuItemClick(5);
                }
            });

            Calendar cal = Calendar.getInstance();
            final int week = cal.get(Calendar.WEEK_OF_YEAR);
            final int day = cal.get(Calendar.DAY_OF_WEEK);

            if (week > PreferUtils.getWeekShowPopup(getActivity()) && day >= 2)
                PreferUtils.setIsShowPopup(getActivity(), true);

            if (PreferUtils.getIsShowPopup(getActivity())) {
                View checkBoxView = View.inflate(getActivity(), R.layout.checkbox_student, null);
                final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    }
                });
                checkBox.setText("일주일간 보이지 않기");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//              builder.setTitle(" MY_TEXT");
                builder.setMessage("일주일 학습데이터는 월요일 03:00AM에 업데이트 됩니다.")
                        .setView(checkBoxView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok_korea, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (checkBox.isChecked()) {
                                    PreferUtils.setIsShowPopup(getActivity(), false);
                                    PreferUtils.setWeekShowPopup(getActivity(), week);
                                }
                            }
                        })
//                        .setNegativeButton(R.string.cancel_korea, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                }
//                        )

                        .show();
            }

            getDataChart();
            getDataDailyChart();

        } else if (index == 1) {
            v = inflater.inflate(R.layout.tab_subjects, container, false);
            this_subject_graph = (PieChart) v.findViewById(R.id.this_subject_graph);

            TextView tv = (TextView) v.findViewById(R.id.textView);
            String str = "학원, 과외 및 학교의 비율";
            final SpannableStringBuilder sps = new SpannableStringBuilder(str);
            sps.setSpan(new AbsoluteSizeSpan(20), 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.append(sps);

            tvDetail = (TextView) v.findViewById(R.id.detail_btn);
            tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((StudentMainActivity) getActivity()).onMenuItemClick(5);
                }
            });

            korean = (TextView) v.findViewById(R.id.korean);
            math = (TextView) v.findViewById(R.id.math);
            english = (TextView) v.findViewById(R.id.english);
            field = (TextView) v.findViewById(R.id.field);
            total_title = (TextView) v.findViewById(R.id.title_total);
            total_study_time = (TextView) v.findViewById(R.id.total_study_time);
            my_rank = (TextView) v.findViewById(R.id.my_rank);
            totla_ll = (LinearLayout) v.findViewById(R.id.totla_ll);
            part_ll = (LinearLayout) v.findViewById(R.id.part_ll);
            method_ll = (LinearLayout) v.findViewById(R.id.method_ll);
            textbook_ll = (LinearLayout) v.findViewById(R.id.textbook_ll);
            content5_ll = (LinearLayout) v.findViewById(R.id.content5_ll);
            content6_ll = (LinearLayout) v.findViewById(R.id.content6_ll);

            tv_content1_name = (TextView) v.findViewById(R.id.tv_content1_name);
            tv_content2_name = (TextView) v.findViewById(R.id.tv_content2_name);
            tv_content3_name = (TextView) v.findViewById(R.id.tv_content3_name);
            tv_content4_name = (TextView) v.findViewById(R.id.tv_content4_name);
            tv_content5_name = (TextView) v.findViewById(R.id.tv_content5_name);
            tv_content6_name = (TextView) v.findViewById(R.id.tv_content6_name);


            tv_content1 = (TextView) v.findViewById(R.id.tv_content1_time);
            tv_content2 = (TextView) v.findViewById(R.id.tv_content2_time);
            tv_content3 = (TextView) v.findViewById(R.id.tv_content3_time);
            tv_content4 = (TextView) v.findViewById(R.id.tv_content4_time);
            tv_content5 = (TextView) v.findViewById(R.id.tv_content5_time);
            tv_content6 = (TextView) v.findViewById(R.id.tv_content6_time);

            tv_method_time1 = (TextView) v.findViewById(R.id.tv_method_time1);
            tv_method_time2 = (TextView) v.findViewById(R.id.tv_method_time2);
            tv_method_time3 = (TextView) v.findViewById(R.id.tv_method_time3);
            tv_method_time4 = (TextView) v.findViewById(R.id.tv_method_time4);

            tv_textbook_time1 = (TextView) v.findViewById(R.id.tv_textbook_time1);
            tv_textbook_time2 = (TextView) v.findViewById(R.id.tv_textbook_time2);
            tv_textbook_time3 = (TextView) v.findViewById(R.id.tv_textbook_time3);
            tv_textbook_time4 = (TextView) v.findViewById(R.id.tv_textbook_time4);
            tv_textbook_time5 = (TextView) v.findViewById(R.id.tv_textbook_time5);
            tv_textbook_time6 = (TextView) v.findViewById(R.id.tv_textbook_time6);
            tv_textbook_time7 = (TextView) v.findViewById(R.id.tv_textbook_time7);

            contentsname = new ArrayList<String>();
            korean_contents_data = new ArrayList<Integer>();
            math_contents_data = new ArrayList<Integer>();
            english_contents_data = new ArrayList<Integer>();

            korean_method_data = new ArrayList<Integer>();
            math_method_data = new ArrayList<Integer>();
            english_method_data = new ArrayList<Integer>();
//            field_method_data = new ArrayList<Integer>();
            Total_field_method_data = new JSONArray();
            Total_field_textbook_data = new JSONArray();

            korean_textbook_data = new ArrayList<Integer>();
            math_textbook_data = new ArrayList<Integer>();
            english_textbook_data = new ArrayList<Integer>();
//            field_textbook_data = new ArrayList<Integer>();

            getKoreanSubjectData();

            korean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totla_ll.setVisibility(View.GONE);
                    part_ll.setVisibility(View.VISIBLE);
                    method_ll.setVisibility(View.VISIBLE);
                    textbook_ll.setVisibility(View.VISIBLE);
                    korean.setBackgroundResource(R.drawable.subject_icon_push);
                    math.setBackgroundResource(R.drawable.subject_icon);
                    english.setBackgroundResource(R.drawable.subject_icon);
                    field.setBackgroundResource(R.drawable.subject_icon);
                    total_title.setText("이번 주 총 국어 공부시간");

                    getKoreanSubjectData();

                }
            });
            math.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totla_ll.setVisibility(View.GONE);
                    part_ll.setVisibility(View.VISIBLE);
                    method_ll.setVisibility(View.VISIBLE);
                    textbook_ll.setVisibility(View.VISIBLE);
                    korean.setBackgroundResource(R.drawable.subject_icon);
                    math.setBackgroundResource(R.drawable.subject_icon_push);
                    english.setBackgroundResource(R.drawable.subject_icon);
                    field.setBackgroundResource(R.drawable.subject_icon);
                    total_title.setText("이번 주 총 수학 공부시간");

                    getMathSubjectData();

                }
            });
            english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totla_ll.setVisibility(View.GONE);
                    part_ll.setVisibility(View.VISIBLE);
                    method_ll.setVisibility(View.VISIBLE);
                    textbook_ll.setVisibility(View.VISIBLE);
                    korean.setBackgroundResource(R.drawable.subject_icon);
                    math.setBackgroundResource(R.drawable.subject_icon);
                    english.setBackgroundResource(R.drawable.subject_icon_push);
                    field.setBackgroundResource(R.drawable.subject_icon);
                    total_title.setText("이번 주 총 영어 공부시간");

                    getEnglishSubjectData();

                }
            });
            field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totla_ll.setVisibility(View.VISIBLE);
                    part_ll.setVisibility(View.GONE);
                    method_ll.setVisibility(View.GONE);
                    textbook_ll.setVisibility(View.GONE);
                    korean.setBackgroundResource(R.drawable.subject_icon);
                    math.setBackgroundResource(R.drawable.subject_icon);
                    english.setBackgroundResource(R.drawable.subject_icon);
                    field.setBackgroundResource(R.drawable.subject_icon_push);
                    total_title.setText("이번 주 총 탐구 공부시간");
                    totla_ll.removeAllViews();

                    getFieldSubjectData();

                }
            });

            contents_graph = (PieChart) v.findViewById(R.id.content_graph);
            method_graph = (PieChart) v.findViewById(R.id.method_graph);
            textbook_graph = (PieChart) v.findViewById(R.id.textbook_graph);

//            setThisSubjectGraph();
            setContextsGrapg();
            setMethodGraph(korean_method_data, method_graph);
            setTextbookGraph(korean_textbook_data, textbook_graph);
        }

        return v;
    }

    private void getFieldSubjectData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("student_id", Integer.valueOf(PreferUtils.getUserId(getActivity())));
            jsonObject.put("subject_id", 70000);

            NetworkHelper.requestJson(getActivity(), Url_define.STUDENT_SUBJECT_DATA + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    contentsname.clear();
//                    field_textbook_data.clear();
//                    field_method_data.clear();
                    try {
                        if (response.getString("result").equals("success")) {
                            total_study_time.setText(response.getString("total_study_time"));
                            my_rank.setText("상위 " + response.getString("my_rank") + "등");


                            JSONArray typeArray = new JSONArray(response.getString("type_analysis"));
                            JSONArray contentsArray = new JSONArray(response.getString("contents_analysis"));
//                            List<Integer> field_textbook_data = new  ArrayList<Integer>();
//                            for (int ii = 0; ii < typeArray.length(); ii++) {
//
//                                JSONObject typeobj = typeArray.getJSONObject(ii);
//                                JSONArray typetimes = typeobj.getJSONArray("times");
//                                for (int i = 0; i < typetimes.length(); i++) {
//                                    JSONObject typetime = (JSONObject) typetimes.get(i);
//                                    field_textbook_data.add(typetime.getInt("time"));
//                                }
//                                Log.e("ii", ii +"    " + field_textbook_data);
//                                Total_field_textbook_data.put(new JSONObject(field_textbook_data.toString()));
//                            }

//                            List<Integer> field_method_data = new  ArrayList<Integer>();
//                            for (int ii = 0; ii < contentsArray.length(); ii++) {
//                                JSONObject contentsobj = contentsArray.getJSONObject(ii);
//                                JSONArray contentstimes = contentsobj.getJSONArray("times");
//                                for (int i = 0; i < contentstimes.length(); i++) {
//                                    JSONObject contentstime = (JSONObject) contentstimes.get(i);
//                                    field_method_data.add(contentstime.getInt("time"));
//                                }
//                                Log.e("ii", ii +"    " + field_method_data);
//                                Total_field_method_data.put(new JSONObject(field_method_data.toString()));
//                            }

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            int j = 0;
                            for (int i = 0; i < typeArray.length(); i++) {
                                View fieldView = inflater.from(getActivity()).inflate(R.layout.tab_subject_field, null);

                                List<Integer> field_textbook_data = new ArrayList<Integer>();
//                                for (int ii = 0; ii < typeArray.length(); ii++) {

                                JSONObject typeobj = typeArray.getJSONObject(i);
                                JSONArray typetimes = typeobj.getJSONArray("times");
                                for (int ii = 0; ii < typetimes.length(); ii++) {
                                    JSONObject typetime = (JSONObject) typetimes.get(ii);
                                    field_textbook_data.add(typetime.getInt("time"));
                                }
                                Log.e("ii", i + "    " + field_textbook_data);
//                                    Total_field_textbook_data.put(new JSONObject(field_textbook_data.toString()));
//                                }


                                List<Integer> field_method_data = new ArrayList<Integer>();
//                                for (int ii = 0; ii < contentsArray.length(); ii++) {
                                JSONObject contentsobj = contentsArray.getJSONObject(i);
                                JSONArray contentstimes = contentsobj.getJSONArray("times");
                                for (int ii = 0; ii < contentstimes.length(); ii++) {
                                    JSONObject contentstime = (JSONObject) contentstimes.get(ii);
                                    field_method_data.add(contentstime.getInt("time"));
                                }
                                Log.e("ii", i + "    " + field_method_data);
//                                    Total_field_method_data.put(new JSONObject(field_method_data.toString()));
//                                }

                                TextView subject_title = (TextView) fieldView.findViewById(R.id.subject_title);
                                TextView subject_title1 = (TextView) fieldView.findViewById(R.id.subject_title1);
                                subject_title.setText(typeArray.getJSONObject(i).getString("title"));
                                subject_title1.setText(typeArray.getJSONObject(i).getString("title"));

                                TextView field_tv_method_time1 = (TextView) fieldView.findViewById(R.id.tv_method_time1);
                                TextView field_tv_method_time2 = (TextView) fieldView.findViewById(R.id.tv_method_time2);
                                TextView field_tv_method_time3 = (TextView) fieldView.findViewById(R.id.tv_method_time3);
                                TextView field_tv_method_time4 = (TextView) fieldView.findViewById(R.id.tv_method_time4);

                                field_tv_method_time1.setText(field_method_data.get(0) / 60 + " 분");
                                field_tv_method_time2.setText(field_method_data.get(1) / 60 + " 분");
                                field_tv_method_time3.setText(field_method_data.get(2) / 60 + " 분");
                                field_tv_method_time4.setText(field_method_data.get(3) / 60 + " 분");

                                TextView field_tv_textbook_time1 = (TextView) fieldView.findViewById(R.id.tv_textbook_time1);
                                TextView field_tv_textbook_time2 = (TextView) fieldView.findViewById(R.id.tv_textbook_time2);
                                TextView field_tv_textbook_time3 = (TextView) fieldView.findViewById(R.id.tv_textbook_time3);
                                TextView field_tv_textbook_time4 = (TextView) fieldView.findViewById(R.id.tv_textbook_time4);
                                TextView field_tv_textbook_time5 = (TextView) fieldView.findViewById(R.id.tv_textbook_time5);
                                TextView field_tv_textbook_time6 = (TextView) fieldView.findViewById(R.id.tv_textbook_time6);
                                TextView field_tv_textbook_time7 = (TextView) fieldView.findViewById(R.id.tv_textbook_time7);

                                field_tv_textbook_time1.setText(field_textbook_data.get(0) / 60 + " 분");
                                field_tv_textbook_time2.setText(field_textbook_data.get(1) / 60 + " 분");
                                field_tv_textbook_time3.setText(field_textbook_data.get(2) / 60 + " 분");
                                field_tv_textbook_time4.setText(field_textbook_data.get(3) / 60 + " 분");
                                field_tv_textbook_time5.setText(field_textbook_data.get(4) / 60 + " 분");
                                field_tv_textbook_time6.setText(field_textbook_data.get(5) / 60 + " 분");
                                field_tv_textbook_time7.setText(field_textbook_data.get(6) / 60 + " 분");

                                PieChart field_method_graph = (PieChart) fieldView.findViewById(R.id.method_graph);
                                PieChart field_textbook_graph = (PieChart) fieldView.findViewById(R.id.textbook_graph);

//                                Total_field_method_data.add(field_method_data);
//                                Total_field_textbook_data.add(field_textbook_data);
//                                List<Integer> field_method_datas = (List<Integer>) Total_field_method_data.get(i);
                                setMethod_data(field_method_data);
                                setTextbook_data(field_textbook_data);

//                                Log.e("field_method_data", Total_field_method_data.get(i).toString());
//                                Log.e("field_textbook_data", Total_field_textbook_data.get(i).toString());

                                setMethodData(4, 100, field_method_graph);
                                setTextbookData(7, 100, field_textbook_graph);
//                                j++;

                                setMethodGraph(field_method_data, field_method_graph);
                                setTextbookGraph(field_textbook_data, field_textbook_graph);

                                totla_ll.addView(fieldView);
                            }

                        } else {
                            Toast.makeText(getActivity(), "데이터를 받아오지 못햇습니다.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getEnglishSubjectData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("student_id", Integer.valueOf(PreferUtils.getUserId(getActivity())));
            jsonObject.put("subject_id", 50000);

            NetworkHelper.requestJson(getActivity(), Url_define.STUDENT_SUBJECT_DATA + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    contentsname.clear();
                    english_textbook_data.clear();
                    english_contents_data.clear();
                    english_method_data.clear();
                    try {
                        if (response.getString("result").equals("success")) {
                            total_study_time.setText(response.getString("total_study_time"));
                            my_rank.setText("상위 " + response.getString("my_rank") + "등");

                            JSONArray typeArray = new JSONArray(response.getString("type_analysis"));
                            JSONObject typeobj = typeArray.getJSONObject(0);
                            JSONArray typetimes = typeobj.getJSONArray("times");
                            for (int i = 0; i < typetimes.length(); i++) {
                                JSONObject typetime = (JSONObject) typetimes.get(i);
                                english_textbook_data.add(typetime.getInt("time"));
                            }

                            JSONArray partArray = new JSONArray(response.getString("part_analysis"));
                            JSONObject partsobj = partArray.getJSONObject(0);
                            JSONArray parttimes = partsobj.getJSONArray("times");
                            for (int i = 0; i < parttimes.length(); i++) {
                                JSONObject parttime = (JSONObject) parttimes.get(i);
                                english_contents_data.add(parttime.getInt("time"));
                            }
                            for (int i = 0; i < parttimes.length(); i++) {
                                JSONObject parttime = (JSONObject) parttimes.get(i);
                                contentsname.add(parttime.getString("title"));
                            }

                            JSONArray contentsArray = new JSONArray(response.getString("contents_analysis"));
                            JSONObject contentsobj = contentsArray.getJSONObject(0);
                            JSONArray contentstimes = contentsobj.getJSONArray("times");
                            for (int i = 0; i < contentstimes.length(); i++) {
                                JSONObject contentstime = (JSONObject) contentstimes.get(i);
                                english_method_data.add(contentstime.getInt("time"));
                            }
                            content5_ll.setVisibility(View.VISIBLE);
                            content6_ll.setVisibility(View.GONE);
                            tv_content5.setVisibility(View.VISIBLE);
                            tv_content6.setVisibility(View.GONE);

                            tv_content1_name.setText(contentsname.get(0));
                            tv_content2_name.setText(contentsname.get(1));
                            tv_content3_name.setText(contentsname.get(2));
                            tv_content4_name.setText(contentsname.get(3));
                            tv_content5_name.setText(contentsname.get(4));

                            tv_content1.setText(english_contents_data.get(0) / 60 + " 분");
                            tv_content2.setText(english_contents_data.get(1) / 60 + " 분");
                            tv_content3.setText(english_contents_data.get(2) / 60 + " 분");
                            tv_content4.setText(english_contents_data.get(3) / 60 + " 분");
                            tv_content5.setText(english_contents_data.get(4) / 60 + " 분");

                            tv_method_time1.setText(english_method_data.get(0) / 60 + " 분");
                            tv_method_time2.setText(english_method_data.get(1) / 60 + " 분");
                            tv_method_time3.setText(english_method_data.get(2) / 60 + " 분");
                            tv_method_time4.setText(english_method_data.get(3) / 60 + " 분");

                            tv_textbook_time1.setText(english_textbook_data.get(0) / 60 + " 분");
                            tv_textbook_time2.setText(english_textbook_data.get(1) / 60 + " 분");
                            tv_textbook_time3.setText(english_textbook_data.get(2) / 60 + " 분");
                            tv_textbook_time4.setText(english_textbook_data.get(3) / 60 + " 분");
                            tv_textbook_time5.setText(english_textbook_data.get(4) / 60 + " 분");
                            tv_textbook_time6.setText(english_textbook_data.get(5) / 60 + " 분");
                            tv_textbook_time7.setText(english_textbook_data.get(6) / 60 + " 분");

                            setContents_data(english_contents_data);
                            setMethod_data(english_method_data);
                            setTextbook_data(english_textbook_data);

                            setContentsData(english_contents_data.size(), 100);
                            setMethodData(english_method_data.size(), 100, method_graph);
                            setTextbookData(english_textbook_data.size(), 100, textbook_graph);

                            setMethodGraph(english_method_data, method_graph);
                            setTextbookGraph(english_textbook_data, textbook_graph);

                        } else {
                            Toast.makeText(getActivity(), "데이터를 받아오지 못햇습니다.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMathSubjectData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("student_id", Integer.valueOf(PreferUtils.getUserId(getActivity())));
            jsonObject.put("subject_id", 30000);

            NetworkHelper.requestJson(getActivity(), Url_define.STUDENT_SUBJECT_DATA + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    contentsname.clear();
                    math_textbook_data.clear();
                    math_contents_data.clear();
                    math_method_data.clear();
                    try {
                        if (response.getString("result").equals("success")) {
                            total_study_time.setText(response.getString("total_study_time"));
                            my_rank.setText("상위 " + response.getString("my_rank") + "등");

                            JSONArray typeArray = new JSONArray(response.getString("type_analysis"));
                            JSONObject typeobj = typeArray.getJSONObject(0);
                            JSONArray typetimes = typeobj.getJSONArray("times");
                            for (int i = 0; i < typetimes.length(); i++) {
                                JSONObject typetime = (JSONObject) typetimes.get(i);
                                math_textbook_data.add(typetime.getInt("time"));
                            }

                            JSONArray partArray = new JSONArray(response.getString("part_analysis"));
                            JSONObject partsobj = partArray.getJSONObject(0);
                            JSONArray parttimes = partsobj.getJSONArray("times");
                            for (int i = 0; i < parttimes.length(); i++) {
                                JSONObject parttime = (JSONObject) parttimes.get(i);
                                math_contents_data.add(parttime.getInt("time"));
                            }
                            for (int i = 0; i < parttimes.length(); i++) {
                                JSONObject parttime = (JSONObject) parttimes.get(i);
                                contentsname.add(parttime.getString("title"));
                            }

                            JSONArray contentsArray = new JSONArray(response.getString("contents_analysis"));
                            JSONObject contentsobj = contentsArray.getJSONObject(0);
                            JSONArray contentstimes = contentsobj.getJSONArray("times");
                            for (int i = 0; i < contentstimes.length(); i++) {
                                JSONObject contentstime = (JSONObject) contentstimes.get(i);
                                math_method_data.add(contentstime.getInt("time"));
                            }

                            if (contentsname.size() == 4) {
                                content5_ll.setVisibility(View.GONE);
                                content6_ll.setVisibility(View.GONE);
                                tv_content5.setVisibility(View.GONE);
                                tv_content6.setVisibility(View.GONE);

                                tv_content1_name.setText(contentsname.get(0));
                                tv_content2_name.setText(contentsname.get(1));
                                tv_content3_name.setText(contentsname.get(2));
                                tv_content4_name.setText(contentsname.get(3));

                                tv_content1.setText(math_contents_data.get(0) / 60 + " 분");
                                tv_content2.setText(math_contents_data.get(1) / 60 + " 분");
                                tv_content3.setText(math_contents_data.get(2) / 60 + " 분");
                                tv_content4.setText(math_contents_data.get(3) / 60 + " 분");

                            } else {

                                content5_ll.setVisibility(View.VISIBLE);
                                content6_ll.setVisibility(View.VISIBLE);
                                tv_content5.setVisibility(View.VISIBLE);
                                tv_content6.setVisibility(View.VISIBLE);

                                tv_content1_name.setText(contentsname.get(0));
                                tv_content2_name.setText(contentsname.get(1));
                                tv_content3_name.setText(contentsname.get(2));
                                tv_content4_name.setText(contentsname.get(3));
                                tv_content5_name.setText(contentsname.get(4));
                                tv_content6_name.setText(contentsname.get(5));

                                tv_content1.setText(math_contents_data.get(0) / 60 + " 분");
                                tv_content2.setText(math_contents_data.get(1) / 60 + " 분");
                                tv_content3.setText(math_contents_data.get(2) / 60 + " 분");
                                tv_content4.setText(math_contents_data.get(3) / 60 + " 분");
                                tv_content5.setText(math_contents_data.get(4) / 60 + " 분");
                                tv_content6.setText(math_contents_data.get(5) / 60 + " 분");
                            }

                            tv_method_time1.setText(math_method_data.get(0) / 60 + " 분");
                            tv_method_time2.setText(math_method_data.get(1) / 60 + " 분");
                            tv_method_time3.setText(math_method_data.get(2) / 60 + " 분");
                            tv_method_time4.setText(math_method_data.get(3) / 60 + " 분");

                            tv_textbook_time1.setText(math_textbook_data.get(0) / 60 + " 분");
                            tv_textbook_time2.setText(math_textbook_data.get(1) / 60 + " 분");
                            tv_textbook_time3.setText(math_textbook_data.get(2) / 60 + " 분");
                            tv_textbook_time4.setText(math_textbook_data.get(3) / 60 + " 분");
                            tv_textbook_time5.setText(math_textbook_data.get(4) / 60 + " 분");
                            tv_textbook_time6.setText(math_textbook_data.get(5) / 60 + " 분");
                            tv_textbook_time7.setText(math_textbook_data.get(6) / 60 + " 분");

                            setContents_data(math_contents_data);
                            setMethod_data(math_method_data);
                            setTextbook_data(math_textbook_data);

                            setContentsData(math_contents_data.size(), 100);
                            setMethodData(math_method_data.size(), 100, method_graph);
                            setTextbookData(math_textbook_data.size(), 100, textbook_graph);

                            setMethodGraph(math_method_data, method_graph);
                            setTextbookGraph(math_textbook_data, textbook_graph);

                        } else {
                            Toast.makeText(getActivity(), "데이터를 받아오지 못햇습니다.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getKoreanSubjectData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("student_id", Integer.valueOf(PreferUtils.getUserId(getActivity())));
            jsonObject.put("subject_id", 10000);

            NetworkHelper.requestJson(getActivity(), Url_define.STUDENT_SUBJECT_DATA + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    contentsname.clear();
                    korean_textbook_data.clear();
                    korean_contents_data.clear();
                    korean_method_data.clear();
                    try {
                        if (response.getString("result").equals("success")) {
                            total_study_time.setText(response.getString("total_study_time"));
                            my_rank.setText("상위 " + response.getString("my_rank") + "등");

                            JSONArray typeArray = new JSONArray(response.getString("type_analysis"));
                            JSONObject typeobj = typeArray.getJSONObject(0);
                            JSONArray typetimes = typeobj.getJSONArray("times");
                            for (int i = 0; i < typetimes.length(); i++) {
                                JSONObject typetime = (JSONObject) typetimes.get(i);
                                korean_textbook_data.add(typetime.getInt("time"));
                            }

                            JSONArray partArray = new JSONArray(response.getString("part_analysis"));
                            JSONObject partsobj = partArray.getJSONObject(0);
                            JSONArray parttimes = partsobj.getJSONArray("times");
                            for (int i = 0; i < parttimes.length(); i++) {
                                JSONObject parttime = (JSONObject) parttimes.get(i);
                                korean_contents_data.add(parttime.getInt("time"));
                            }
                            for (int i = 0; i < parttimes.length(); i++) {
                                JSONObject parttime = (JSONObject) parttimes.get(i);
                                contentsname.add(parttime.getString("title"));
                            }

                            JSONArray contentsArray = new JSONArray(response.getString("contents_analysis"));
                            JSONObject contentsobj = contentsArray.getJSONObject(0);
                            JSONArray contentstimes = contentsobj.getJSONArray("times");
                            for (int i = 0; i < contentstimes.length(); i++) {
                                JSONObject contentstime = (JSONObject) contentstimes.get(i);
                                korean_method_data.add(contentstime.getInt("time"));
                            }
                            content5_ll.setVisibility(View.GONE);
                            content6_ll.setVisibility(View.GONE);
                            tv_content5.setVisibility(View.GONE);
                            tv_content6.setVisibility(View.GONE);

                            tv_content1_name.setText(contentsname.get(0));
                            tv_content2_name.setText(contentsname.get(1));
                            tv_content3_name.setText(contentsname.get(2));
                            tv_content4_name.setText(contentsname.get(3));

                            tv_content1.setText(korean_contents_data.get(0) / 60 + " 분");
                            tv_content2.setText(korean_contents_data.get(1) / 60 + " 분");
                            tv_content3.setText(korean_contents_data.get(2) / 60 + " 분");
                            tv_content4.setText(korean_contents_data.get(3) / 60 + " 분");

                            tv_method_time1.setText(korean_method_data.get(0) / 60 + " 분");
                            tv_method_time2.setText(korean_method_data.get(1) / 60 + " 분");
                            tv_method_time3.setText(korean_method_data.get(2) / 60 + " 분");
                            tv_method_time4.setText(korean_method_data.get(3) / 60 + " 분");

                            tv_textbook_time1.setText(korean_textbook_data.get(0) / 60 + " 분");
                            tv_textbook_time2.setText(korean_textbook_data.get(1) / 60 + " 분");
                            tv_textbook_time3.setText(korean_textbook_data.get(2) / 60 + " 분");
                            tv_textbook_time4.setText(korean_textbook_data.get(3) / 60 + " 분");
                            tv_textbook_time5.setText(korean_textbook_data.get(4) / 60 + " 분");
                            tv_textbook_time6.setText(korean_textbook_data.get(5) / 60 + " 분");
                            tv_textbook_time7.setText(korean_textbook_data.get(6) / 60 + " 분");

                            setContents_data(korean_contents_data);
                            setMethod_data(korean_method_data);
                            setTextbook_data(korean_textbook_data);

                            setContentsData(korean_contents_data.size(), 100);
                            setMethodData(korean_method_data.size(), 100, method_graph);
                            setTextbookData(korean_textbook_data.size(), 100, textbook_graph);

                            setMethodGraph(korean_method_data, method_graph);
                            setTextbookGraph(korean_textbook_data, textbook_graph);

                        } else {
                            Toast.makeText(getActivity(), "데이터를 받아오지 못햇습니다.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setMyDayGraph() {
        my_day_graph.setUsePercentValues(true);
        my_day_graph.setDescription("");
        my_day_graph.setDragDecelerationFrictionCoef(0);

        my_day_graph.setDrawHoleEnabled(false);
        my_day_graph.setHoleColorTransparent(true);

        my_day_graph.setTransparentCircleColor(Color.TRANSPARENT);
        my_day_graph.setTransparentCircleAlpha(0);

        my_day_graph.setHoleRadius(0f);
        my_day_graph.setTransparentCircleRadius(0f);

        my_day_graph.setDrawCenterText(false);

        my_day_graph.setRotationAngle(0);
        my_day_graph.setRotationEnabled(false);
        my_day_graph.setTouchEnabled(false);

        setMyData(6, 100);

        my_day_graph.animateY(200, Easing.EasingOption.EaseInOutQuad);

        Legend l = my_day_graph.getLegend();
        l.setEnabled(false);
    }

    private void setTopDayGraph() {
        top_day_graph.setUsePercentValues(true);
        top_day_graph.setDescription("");
        top_day_graph.setDragDecelerationFrictionCoef(0);

        top_day_graph.setDrawHoleEnabled(false);
        top_day_graph.setHoleColorTransparent(true);

        top_day_graph.setTransparentCircleColor(Color.TRANSPARENT);
        top_day_graph.setTransparentCircleAlpha(0);

        top_day_graph.setHoleRadius(0);
        top_day_graph.setTransparentCircleRadius(0);

        top_day_graph.setDrawCenterText(false);

        top_day_graph.setRotationAngle(0);
        top_day_graph.setRotationEnabled(false);
        top_day_graph.setTouchEnabled(false);


        setTopData(6, 100);

        top_day_graph.animateY(200, Easing.EasingOption.EaseInOutQuad);

        Legend l = top_day_graph.getLegend();
        l.setEnabled(false);

    }

    private void updateThisWeekTv() {

        long timeStudySeconds = 0;

//        Calendar c = Calendar.getInstance();
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//
//        LogUtils.d(TAG, "updateThisWeekTv , dayOfWeek : " + dayOfWeek);

        long[] arrStudy = {studentChartModel.getDailyMonday(), studentChartModel.getDailyTuesday(),
                studentChartModel.getDailyWednesday(), studentChartModel.getDailyThursday(), studentChartModel.getDailyFriday(), studentChartModel.getDailySaturday(), studentChartModel.getDailySunday()};

//        for (int i = 0; i < dayOfWeek-1; i++) {
//            timeStudySeconds = timeStudySeconds + arrStudy[i];
//        }


        for (int i = 0; i < 7; i++) {
            Log.e("arrStudy", arrStudy[i] + "");
            timeStudySeconds += arrStudy[i];
        }

        Log.e("timeStudySeconds", timeStudySeconds + "");
        long timeStudyAverage = timeStudySeconds / 7 / 60;

        String tvTimeStudy = timeStudyAverage / 60 + "시간" + " " + +timeStudyAverage % 60 + "분";

        tvThisWeekLearning.setText(tvTimeStudy);
    }

    private void setThisWeekGraph() {

        updateThisWeekTv();

        this_week_graph.setDrawGridBackground(false);
        this_week_graph.setDrawBorders(false);
        this_week_graph.setBorderColor(Color.parseColor("#b2b2b2"));
        this_week_graph.setBorderWidth(0);
        this_week_graph.setDescription("");
        this_week_graph.setNoDataTextDescription("You need to provide data for the chart.");
        this_week_graph.setHighlightEnabled(false);
        this_week_graph.setTouchEnabled(false);
        this_week_graph.setDragEnabled(false);
        this_week_graph.setScaleEnabled(false);
        this_week_graph.setPinchZoom(false);

        setMyWeekData(9, 1);

        this_week_graph.animateX(2500);

        Legend l = this_week_graph.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextSize(10f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextColor(Color.parseColor("#b2b2b2"));
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setXOffset(20);

        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.POS_LEFT);
        llXAxis.setTextSize(10f);

        XAxis xAxis = this_week_graph.getXAxis();
        xAxis.setTypeface(tf);
//        xAxis.enableGridDashedLine(10f,10f,0f);
        xAxis.setSpaceBetweenLabels(10);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15);
        xAxis.setTextColor(Color.parseColor("#b2b2b2"));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = this_week_graph.getAxisLeft();
        leftAxis.setEnabled(false);

        YAxis rightAxis = this_week_graph.getAxisRight();
        rightAxis.setEnabled(false);

    }

    private void setBarChart() {
        bar_chart.setDescription("");


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        bar_chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        bar_chart.setPinchZoom(false);
        bar_chart.setTouchEnabled(false);

        bar_chart.setDrawGridBackground(false);
        bar_chart.setDrawBarShadow(false);

        bar_chart.setDrawValueAboveBar(false);

        // change the position of the y-labels
        YAxis yLabels = bar_chart.getAxisLeft();
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
        xLabels.setTextSize(10);
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

        setBarChartData(2);

        bar_chart.animateY(500);

        Legend l = bar_chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);
        l.setFormSize(8f);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setMaxSizePercent(0.3f);
    }

    private void setMyData(int count, float range) {

        float mult = range;
        //{버리는시간, 교과, 비교과, 잠, 식사, 휴식, 기타}
//        int[] my_day_data = {1, 1, 2, 3, 4, 5, 6};
        int[] my_day_data = {studentDailyChartModel.getSubject(), studentDailyChartModel.getNonSubject(), studentDailyChartModel.getSleep(), studentDailyChartModel.getEat(),
                studentDailyChartModel.getRest(), studentDailyChartModel.getEtc(), studentDailyChartModel.getWaste()};

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (my_day_data[i] * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(0);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        //subject
        colors.add(Color.parseColor("#bbedff"));

        //non subject
        colors.add(Color.parseColor("#88e0ff"));

        //sleep
        colors.add(Color.parseColor("#56c7e2"));

        //eat
        colors.add(Color.parseColor("#1ca1b7"));

        //rest
        colors.add(Color.parseColor("#006289"));

        //waste
        colors.add(Color.parseColor("#33393d"));

        //etc
        colors.add(Color.parseColor("#dbdbdb"));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        my_day_graph.setData(data);

        // undo all highlights
        my_day_graph.highlightValues(null);

        my_day_graph.invalidate();
    }

    private void setTopData(int count, float range) {

        float mult = range;
        //{버리는시간, 교과, 비교과, 잠, 식사, 휴식, 기타}
//        int[] my_day_data = {1, 4, 5, 3, 2, 3, 1};

        int[] my_day_data = {studentDailyChartModel.getHighRankSubject(), studentDailyChartModel.getHighRankNonSubject(), studentDailyChartModel.getHighRankSleep(), studentDailyChartModel.getHighRankEat(),
                studentDailyChartModel.getHighRankRest(), studentDailyChartModel.getHighRankEtc(), studentDailyChartModel.getHighRankWaster()};


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (my_day_data[i] * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(0);

        ArrayList<Integer> colors = new ArrayList<Integer>();

//        colors.add(Color.parseColor("#dbdbdb"));
//        colors.add(Color.parseColor("#bbedff"));
//        colors.add(Color.parseColor("#88e0ff"));
//        colors.add(Color.parseColor("#56c7e2"));
//        colors.add(Color.parseColor("#1ca1b7"));
//        colors.add(Color.parseColor("#006289"));
//        colors.add(Color.parseColor("#33393d"));

        //subject
        colors.add(Color.parseColor("#bbedff"));

        //non subject
        colors.add(Color.parseColor("#88e0ff"));

        //sleep
        colors.add(Color.parseColor("#56c7e2"));

        //eat
        colors.add(Color.parseColor("#1ca1b7"));

        //rest
        colors.add(Color.parseColor("#006289"));

        //waste
        colors.add(Color.parseColor("#33393d"));

        //etc
        colors.add(Color.parseColor("#dbdbdb"));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        top_day_graph.setData(data);

        // undo all highlights
        top_day_graph.highlightValues(null);

        top_day_graph.invalidate();
    }

    private void setMyWeekData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mDays[i]);
        }

//        int[] my_week_data = {0, 3, 4, 7, 4, 5, 2, 1, 0};
        int[] my_week_data = {0, (int) studentChartModel.getDailyMonday(), (int) studentChartModel.getDailyTuesday(), (int) studentChartModel.getDailyWednesday(), (int) studentChartModel.getDailyThursday(),
                (int) studentChartModel.getDailyFriday(), (int) studentChartModel.getDailySaturday(), (int) studentChartModel.getDailySunday(), 0};

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float mult = range;
            float val = (float) (my_week_data[i] * mult);
            yVals1.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "나");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setDrawFilled(true);
        set1.setLineWidth(0);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.WHITE);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
//        set1.setDrawHorizontalHighlightIndicator(false);
//        set1.setVisible(false);
//        set1.setCircleHoleColor(Color.WHITE);

//        int[] top_week_data = {0, 5, 7, 5, 8, 7, 6, 7, 0};
        int[] top_week_data = {0, (int) studentChartModel.getDailyHeightRankMonday(), (int) studentChartModel.getDailyHeightRankTuesday(), (int) studentChartModel.getDailyHeightRankWednesday(), (int) studentChartModel.getDailyHeightRankThursday(),
                (int) studentChartModel.getDailyHeightRankFriday(), (int) studentChartModel.getDailyHeightRankSaturday(), (int) studentChartModel.getDailyHeightRankSunday(), 0};

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float mult = range;
            float val = (float) (top_week_data[i] * mult);
            yVals2.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals2, "상위 10%");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.BLUE);
        set2.setDrawFilled(true);
        set2.setLineWidth(0);
        set2.setCircleSize(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.parseColor("#9DCEFF"));
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set2);
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextSize(0);

        // set data
        this_week_graph.setData(data);
    }

    private void updateTvCompare() {

        long totalLastWeek = studentChartModel.getLastWeekKorean() + studentChartModel.getLastWeekMath() + studentChartModel.getLastWeekEnglish() + studentChartModel.getLastWeekSocialScience() + studentChartModel.getLastWeekNonSubject();

        long totalThisWeek = studentChartModel.getThisWeekKorean() + studentChartModel.getThisWeekMath() + studentChartModel.getThisWeekEnglisth() + studentChartModel.getThisWeekSocialScience() + studentChartModel.getThisWeekNonSubject();

        if (totalLastWeek > totalThisWeek) {
            tvCompareWeek.setText("저번 주의 나에게 졌습니다!");
        } else {
            tvCompareWeek.setText("저번 주의 나에게 이겼습니다!");
        }

    }

    private void setBarChartData(int count) {

        updateTvCompare();

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mWeeks[i]);
        }
        //    {"국어", "수학", "영어", "탐구", "비교과"}
        int[] lastWeek = {(int) studentChartModel.getLastWeekKorean(), (int) studentChartModel.getLastWeekMath(), (int) studentChartModel.getLastWeekEnglish(),
                (int) studentChartModel.getLastWeekSocialScience(), (int) studentChartModel.getLastWeekNonSubject()};
        int[] thisWeek = {(int) studentChartModel.getThisWeekKorean(), (int) studentChartModel.getThisWeekMath(), (int) studentChartModel.getThisWeekEnglisth(),
                (int) studentChartModel.getThisWeekSocialScience(), (int) studentChartModel.getThisWeekNonSubject()};

//        int[] lastWeek = {1492, 370, 632, 0, 0};
//        int[] thisWeek = {260, 490, 678, 553, 436};


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        float lastval1 = (float) (lastWeek[0]);
        float lastval2 = (float) (lastWeek[1]);
        float lastval3 = (float) (lastWeek[2]);
        float lastval4 = (float) (lastWeek[3]);
        float lastval5 = (float) (lastWeek[4]);

        float thisval1 = (float) (thisWeek[0]);
        float thisval2 = (float) (thisWeek[1]);
        float thisval3 = (float) (thisWeek[2]);
        float thisval4 = (float) (thisWeek[3]);
        float thisval5 = (float) (thisWeek[4]);

        yVals1.add(new BarEntry(new float[]{lastval1 / 60, lastval2 / 60, lastval3 / 60, lastval4 / 60, lastval5 / 60}, 0));
        yVals1.add(new BarEntry(new float[]{thisval1 / 60, thisval2 / 60, thisval3 / 60, thisval4 / 60, thisval5 / 60}, 1));


        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(getColors());
        set1.setBarSpacePercent(40);
        set1.setValueTextColor(Color.WHITE);
        set1.setValueTextSize(10);
        set1.setValueTypeface(tf);

        set1.setStackLabels(new String[]{"국어", "수학", "영어", "탐구", "비교과"});

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        ValueFormatter vf = new ValueFormatter() {

            private DecimalFormat mFormat = new DecimalFormat(("#######"));

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

    private int[] getColors() {
        int[] colors = {Color.parseColor("#80acca"), Color.parseColor("#476fa6"), Color.parseColor("#092337"), Color.parseColor("#53777a"), Color.parseColor("#83af9b")};
        return colors;
    }

    private void setThisSubjectGraph() {

        //update tv ratio
        tvSubjectRatio.setText(studentChartModel.getSubjectNonSubject());
        tvAloneTimeRatio.setText(studentChartModel.getStudyAlone());

        this_subject_graph.setUsePercentValues(true);
        this_subject_graph.setDescription("");
        this_subject_graph.setDragDecelerationFrictionCoef(0.95f);
        this_subject_graph.setDrawHoleEnabled(false);
        this_subject_graph.setHoleColorTransparent(true);

        this_subject_graph.setTransparentCircleColor(Color.WHITE);
        this_subject_graph.setTransparentCircleAlpha(110);

        this_subject_graph.setHoleRadius(58f);
        this_subject_graph.setTransparentCircleRadius(61f);

        this_subject_graph.setDrawCenterText(false);

        this_subject_graph.setRotationAngle(0);
        this_subject_graph.setRotationEnabled(false);
        this_subject_graph.setTouchEnabled(false);

        setThisSubjectData(4, 100);

        this_subject_graph.animateY(500, Easing.EasingOption.EaseInOutQuad);

        Legend l = this_subject_graph.getLegend();
        l.setEnabled(false);
    }

    private void setContextsGrapg() {
        contents_graph.setUsePercentValues(true);
        contents_graph.setDescription("");
        contents_graph.setDragDecelerationFrictionCoef(0.95f);
        contents_graph.setDrawHoleEnabled(false);
        contents_graph.setHoleColorTransparent(true);

        contents_graph.setTransparentCircleColor(Color.WHITE);
        contents_graph.setTransparentCircleAlpha(110);

        contents_graph.setHoleRadius(58f);
        contents_graph.setTransparentCircleRadius(61f);

        contents_graph.setDrawCenterText(false);

        contents_graph.setRotationAngle(0);
        contents_graph.setRotationEnabled(false);
        contents_graph.setTouchEnabled(false);

        setContents_data(korean_contents_data);

        setContentsData(korean_contents_data.size(), 100);

        contents_graph.animateY(500, Easing.EasingOption.EaseInOutQuad);

        Legend l = contents_graph.getLegend();
        l.setEnabled(false);

    }

    private void setMethodGraph(List<Integer> data, PieChart piechart) {
        piechart.setUsePercentValues(true);
        piechart.setDescription("");
        piechart.setDragDecelerationFrictionCoef(0.95f);
        piechart.setDrawHoleEnabled(false);
        piechart.setHoleColorTransparent(true);

        piechart.setTransparentCircleColor(Color.WHITE);
        piechart.setTransparentCircleAlpha(110);

        piechart.setHoleRadius(58f);
        piechart.setTransparentCircleRadius(61f);

        piechart.setDrawCenterText(false);

        piechart.setRotationAngle(0);
        piechart.setRotationEnabled(false);
        piechart.setTouchEnabled(false);

        setMethod_data(data);

        setMethodData(data.size(), 100, piechart);

        piechart.animateY(500, Easing.EasingOption.EaseInOutQuad);

        Legend l = piechart.getLegend();
        l.setEnabled(false);
    }

    private void setTextbookGraph(List<Integer> data, PieChart piechart) {
        piechart.setUsePercentValues(true);
        piechart.setDescription("");
        piechart.setDragDecelerationFrictionCoef(0.95f);
        piechart.setDrawHoleEnabled(false);
        piechart.setHoleColorTransparent(true);

        piechart.setTransparentCircleColor(Color.WHITE);
        piechart.setTransparentCircleAlpha(110);

        piechart.setHoleRadius(58f);
        piechart.setTransparentCircleRadius(61f);

        piechart.setDrawCenterText(false);

        piechart.setRotationAngle(0);
        piechart.setRotationEnabled(false);
        piechart.setTouchEnabled(false);

        setTextbook_data(data);


        setTextbookData(data.size(), 100, piechart);

        piechart.animateY(500, Easing.EasingOption.EaseInOutQuad);

        Legend l = piechart.getLegend();
        l.setEnabled(false);
    }

    private void setThisSubjectData(int count, float range) {
        float mult = range;
        //{국어, 수학, 영어, 탐구}
//        int[] my_day_data = {1, 1, 2, 2};

        int[] my_day_data = {(int) studentChartModel.getThisWeekKorean(), (int) studentChartModel.getThisWeekMath(), (int) studentChartModel.getThisWeekEnglisth(),
                (int) studentChartModel.getThisWeekSocialScience()};

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) (my_day_data[i] * mult) + mult / 4, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(0);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#c5e5f6"));
        colors.add(Color.parseColor("#92d4f7"));
        colors.add(Color.parseColor("#76cbef"));
        colors.add(Color.parseColor("#299cce"));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        this_subject_graph.setData(data);

        // undo all highlights
        this_subject_graph.highlightValues(null);

        this_subject_graph.invalidate();

    }

    public void setContents_data(List<Integer> contents_data) {
        this.contents_data = contents_data;
    }

    public void setMethod_data(List<Integer> method_data) {
        this.method_data = method_data;
    }

    public void setTextbook_data(List<Integer> textbook_data) {

        this.textbook_data = textbook_data;
    }

    private void setContentsData(int count, float range) {
        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) (contents_data.get(i) * mult) + mult / count, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++)
            xVals.add(mContents[i % mContents.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(0);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.parseColor("#C2ecff"));
        colors.add(Color.parseColor("#88e0ff"));
        colors.add(Color.parseColor("#56c7e2"));
        colors.add(Color.parseColor("#1ca1b7"));
        colors.add(Color.parseColor("#006289"));
        colors.add(Color.parseColor("#083c4c"));
        colors.add(Color.parseColor("#000000"));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        contents_graph.setData(data);

        // undo all highlights
        contents_graph.highlightValues(null);

        contents_graph.invalidate();
    }

    private void setMethodData(int count, float range, PieChart chart) {
        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) (method_data.get(i) * mult) + mult / count, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mMethod[i % mMethod.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(0);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.parseColor("#C2ecff"));
        colors.add(Color.parseColor("#88e0ff"));
        colors.add(Color.parseColor("#56c7e2"));
        colors.add(Color.parseColor("#1ca1b7"));
        colors.add(Color.parseColor("#006289"));
        colors.add(Color.parseColor("#083c4c"));
        colors.add(Color.parseColor("#000000"));


        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void setTextbookData(int count, float range, PieChart chart) {
        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) (textbook_data.get(i) * mult) + mult / count, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mTextbook[i % mTextbook.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(0);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.parseColor("#C2ecff"));
        colors.add(Color.parseColor("#88e0ff"));
        colors.add(Color.parseColor("#56c7e2"));
        colors.add(Color.parseColor("#1ca1b7"));
        colors.add(Color.parseColor("#006289"));
        colors.add(Color.parseColor("#083c4c"));
        colors.add(Color.parseColor("#000000"));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }
}