package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orhanobut.logger.Logger;
import com.trams.azit.activity.MentoMainActivity;
import com.trams.azit.dialog.TimeModify;
import com.trams.azit.dialog.TimeModifyNonStudy;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015-09-03.
 */

public class Student_Home extends ConnFragment implements View.OnClickListener {

    TextView sat_day, dday_title, dday, today, prev_day, next_day;
    TextView dday_change;
    ImageView lock, plus_act;
    LinearLayout my_study, my_grade;
    LinearLayout meet_mentor, azit_clinic, mentoring;
    Calendar mCalander;
    public static ArrayList<StudentHomeListData> mArrayList = new ArrayList<StudentHomeListData>();

    public static ListViewAdapter home_Adapter;
    private SwipeMenuListView home_List;
    SimpleDateFormat sdf;
    final static int STOP = 0;    //첫화면의 상태
    final static int RUNNING = 1;    // 시작버튼 누른상태
    final static int PAUSE = 2;    // 중지버튼 누른상태
    final static int FINAL = 3;    // 작동 완료
    static int selectedPosition;

    SharedPreferences myPrefs;
    String secret, user_id, todat_sdf;

    public static void setTick() {
        home_Adapter.notifyDataSetChanged();
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem timeModify = new SwipeMenuItem(
                    getActivity());
            // set item background
            timeModify.setBackground(new ColorDrawable(Color.parseColor("#ffc13e")));
            // set item width
            timeModify.setWidth(dp2px(60));
            // set a icon
            timeModify.setIcon(R.drawable.ic_time_modify);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getActivity());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF3E3E")));
            // set item width
            deleteItem.setWidth(dp2px(60));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete_small);
            // add to menu
            menu.addMenuItem(timeModify);
            menu.addMenuItem(deleteItem);
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onResume() {
        super.onResume();

        mCalander = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy.M.d");

        SimpleDateFormat formatter = new SimpleDateFormat("HH", Locale.KOREA);
        Date currentTime = new Date();
        String dTime = formatter.format(currentTime);
        Log.e("dTime from home", dTime);

        if (Integer.parseInt(dTime) < 3) {
            mCalander.add(Calendar.DAY_OF_MONTH, -1);
            todat_sdf = sdf.format(mCalander.getTime());
            today.setText(sdf.format(mCalander.getTime()));
        } else {
            todat_sdf = sdf.format(mCalander.getTime());
            today.setText(sdf.format(mCalander.getTime()));
        }

        if (StudentMainActivity.canMove) {

            mArrayList.clear();
            LoadFromServer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_home, null);

        myPrefs = this.getActivity().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        String IsPoPup = myPrefs.getString("IsPoPup", "");
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");


        home_List = (SwipeMenuListView) view.findViewById(R.id.student_home_list);
        home_List.setMenuCreator(creator);
        home_List.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        sat_day = (TextView) view.findViewById(R.id.sat_day);
        dday_title = (TextView) view.findViewById(R.id.dday_title);
        dday = (TextView) view.findViewById(R.id.dday);
        dday_change = (TextView) view.findViewById(R.id.dday_change);
        my_study = (LinearLayout) view.findViewById(R.id.my_study);
        my_grade = (LinearLayout) view.findViewById(R.id.my_grade);
        meet_mentor = (LinearLayout) view.findViewById(R.id.meet_mentor);
        azit_clinic = (LinearLayout) view.findViewById(R.id.azit_clinic);
        mentoring = (LinearLayout) view.findViewById(R.id.mentoring);
        today = (TextView) view.findViewById(R.id.today);
        prev_day = (TextView) view.findViewById(R.id.prev_day);
        next_day = (TextView) view.findViewById(R.id.next_day);
        lock = (ImageView) view.findViewById(R.id.lock);
        plus_act = (ImageView) view.findViewById(R.id.plus_act);

        mCalander = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy.M.d");

//        SimpleDateFormat formatter = new SimpleDateFormat("HH", Locale.KOREA);
//        Date currentTime = new Date();
//        String dTime = formatter.format(currentTime);
//        Log.e("dTime from home", dTime);
//
//        if (Integer.parseInt(dTime) < 3) {
//            mCalander.add(Calendar.DAY_OF_MONTH, -1);
//            todat_sdf = sdf.format(mCalander.getTime());
//            today.setText(sdf.format(mCalander.getTime()));
//        } else {
//            todat_sdf = sdf.format(mCalander.getTime());
//            today.setText(sdf.format(mCalander.getTime()));
//        }


        prev_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StudentMainActivity.canMove) {
                    mCalander.add(Calendar.DAY_OF_MONTH, -1);
                    today.setText(sdf.format(mCalander.getTime()));
                    next_day.setBackgroundResource(R.drawable.next_day);
                    mArrayList.clear();
                    LoadFromServer();
                } else {
                    Toast.makeText(getActivity(), "타이머가 동작중이면 날짜를 변경할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (today.getText().equals(sdf.format(Calendar.getInstance().getTime()))) {

                } else {
                    next_day.setBackgroundResource(R.drawable.next_day);
                    mCalander.add(Calendar.DAY_OF_MONTH, 1);
                    today.setText(sdf.format(mCalander.getTime()));
                    mArrayList.clear();
                    LoadFromServer();
                    if (today.getText().equals(sdf.format(Calendar.getInstance().getTime()))) {
                        next_day.setBackgroundResource(R.drawable.limit_next_day);
                    }
                }
            }
        });

        dday_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((StudentMainActivity) getActivity(), DDay_Change.class);
                i.putExtra("title", "");
                i.putExtra("date", "");
                startActivity(i);
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText((StudentMainActivity) getActivity(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        plus_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StudentMainActivity.canMove) {
                    Intent i = new Intent((StudentMainActivity) getActivity(), Plus_Act.class);
                    startActivity(i);

                } else {
                    Log.e("boo", StudentMainActivity.mTimer.hasMessages(0) + "");
                    Toast.makeText(getActivity(), "타이머가 동작중이면 일정을 추가할수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        my_study.setOnClickListener(this);
        my_grade.setOnClickListener(this);
        meet_mentor.setOnClickListener(this);
        azit_clinic.setOnClickListener(this);
        mentoring.setOnClickListener(this);


        cal_dday();
        setListView();
//        LoadFromServer();
        LoadDDay();

        if (IsPoPup.equals("")) {
//            setPoPup();
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("IsPoPup", "true"); // "true" : 성적을 한번 이상 수정함. "": 성적을 수정하지 않음. - 팝업을 처음에 한번만 뜨우기 위해 사용
            editor.commit();
        }

        return view;
    }

    private void LoadDDay() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            requestJson(Url_define.Student_Get_Main_DDay + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        dday_title.setText(response.getString("title"));
                        sat_day.setText(response.getString("date"));
                        dday.setText(getDday(response.getString("date")));


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

    private String getDday(String day) {
        Log.e("day", day);
        String d_day = "";
        String[] arr = day.split("/");
        int cal_dday = caldate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));

        Log.e("cal_dday", cal_dday + "");

        if (cal_dday > 0) {
            d_day = "D+" + String.valueOf(cal_dday);
        } else if (cal_dday < 0) {
            d_day = "D" + String.valueOf(cal_dday);
        } else {
            d_day = "D-Day";
        }

        return d_day;
    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("date", today.getText().toString());

            requestJson(Url_define.Student_Get_My_act + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (!response.has("activity_list")) return;

                        JSONArray datas = new JSONArray(response.getString("activity_list"));

                        Logger.e(datas.toString());
                        for (int i = 0; i < datas.length(); i++) {
                            JSONObject classificationObject = (JSONObject) datas.get(i);

                            int activityTypeId = classificationObject.getInt("activity_type_id");
                            int activityType = classificationObject.getInt("activity_type");

                            int act_icon = 0;

                            if (activityType == 1 || activityType == 2) {
                                if (activityTypeId == 1) {
                                    act_icon = R.drawable.study_icon;
                                } else if (activityTypeId == 2) {
                                    act_icon = R.drawable.schedule_type_2;
                                } else if (activityTypeId == 3) {
                                    act_icon = R.drawable.schedule_type_3;
                                } else if (activityTypeId == 4) {
                                    act_icon = R.drawable.class_icon;
                                } else if (activityTypeId == 5) {
                                    act_icon = R.drawable.schedule_type_5;
                                } else if (activityTypeId == 6) {
                                    act_icon = R.drawable.schedule_type_6;
                                } else if (activityTypeId == 7) {
                                    act_icon = R.drawable.schedule_type_7;
                                } else if (activityTypeId == 8) {
                                    act_icon = R.drawable.reading_icon;
                                } else if (activityTypeId == 9) {
                                    act_icon = R.drawable.sleep_icon;
                                } else if (activityTypeId == 10) {
                                    act_icon = R.drawable.leisure_icon;
                                } else if (activityTypeId == 11) {
                                    act_icon = R.drawable.rest_icon;
                                } else if (activityTypeId == 12) {
                                    act_icon = R.drawable.schedule_type_12;
                                } else if (activityTypeId == 13) {
                                    act_icon = R.drawable.exercise_icon;
                                } else {
                                    act_icon = R.drawable.study_icon;
                                }
                            } else if (activityType == 3) {
                                switch (activityTypeId) {
                                    case 21:
                                        act_icon = R.drawable.schedule_create_1;
                                        break;
                                    case 22:
                                        act_icon = R.drawable.schedule_create_2;
                                        break;
                                    case 23:
                                        act_icon = R.drawable.schedule_create_3;
                                        break;
                                    case 24:
                                        act_icon = R.drawable.schedule_create_5;
                                        break;
                                    case 25:
                                        act_icon = R.drawable.schedule_create_5;
                                        break;
                                    case 26:
                                        act_icon = R.drawable.schedule_create_6;
                                        break;
                                    case 27:
                                        act_icon = R.drawable.schedule_create_7;
                                        break;
                                    case 28:
                                        act_icon = R.drawable.schedule_create_8;
                                        break;
                                    case 29:
                                        act_icon = R.drawable.schedule_create_9;
                                        break;
                                    case 30:
                                        act_icon = R.drawable.schedule_create_10;
                                        break;
                                    case 31:
                                        act_icon = R.drawable.schedule_create_11;
                                        break;
                                    case 32:
                                        act_icon = R.drawable.schedule_create_12;
                                        break;
                                    case 33:
                                        act_icon = R.drawable.schedule_create_13;
                                        break;
                                    case 34:
                                        act_icon = R.drawable.schedule_create_14;
                                        break;
                                    case 35:
                                        act_icon = R.drawable.schedule_create_15;
                                        break;
                                    case 36:
                                        act_icon = R.drawable.schedule_create_16;
                                        break;
                                    case 37:
                                        act_icon = R.drawable.schedule_create_17;
                                        break;
                                    case 38:
                                        act_icon = R.drawable.schedule_create_18;
                                        break;
                                    case 39:
                                        act_icon = R.drawable.schedule_create_19;
                                        break;
                                    case 40:
                                        act_icon = R.drawable.schedule_create_20;
                                        break;
                                    case 41:
                                        act_icon = R.drawable.schedule_create_21;
                                        break;
                                    case 42:
                                        act_icon = R.drawable.schedule_create_22;
                                        break;
                                    case 43:
                                        act_icon = R.drawable.schedule_create_23;
                                        break;
                                    case 44:
                                        act_icon = R.drawable.schedule_create_24;
                                        break;
                                    case 45:
                                        act_icon = R.drawable.schedule_create_25;
                                        break;
                                    case 46:
                                        act_icon = R.drawable.schedule_create_26;
                                        break;
                                }
                            }
                            String goal = "";
                            if (datas.getJSONObject(i).isNull("start_time")) {
                                goal = datas.getJSONObject(i).getString("goal");
                            } else {
                                goal = getFormatTime(datas.getJSONObject(i).getString("start_time")) + " - " + getFormatTime(datas.getJSONObject(i).getString("end_time"));
                            }

                            addItem(datas.getJSONObject(i).getInt("id"),
                                    act_icon,
                                    datas.getJSONObject(i).getString("title"),
                                    datas.getJSONObject(i).getString("sub_title"),
                                    datas.getJSONObject(i).isNull("start_time") ? "" : datas.getJSONObject(i).getString("start_time"),
                                    datas.getJSONObject(i).isNull("end_time") ? "" : datas.getJSONObject(i).getString("end_time"),
                                    goal,
                                    datas.getJSONObject(i).getInt("activity_type"),
                                    datas.getJSONObject(i).getInt("activity_type_id"),
                                    datas.getJSONObject(i).isNull("start_page") ? "" : datas.getJSONObject(i).getString("start_page"),
                                    datas.getJSONObject(i).isNull("end_page") ? "" : datas.getJSONObject(i).getString("end_page")
                            );

                        }

                        home_Adapter.notifyDataSetChanged();

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

    public String getFormatTime(String time) {
        String formatTime = "";
        try {
            String[] splitTime = time.split(":");
            String ampm = "";
            int hour = Integer.parseInt(splitTime[0]);
            int minute = Integer.parseInt(splitTime[1]);
            if (hour - 12 == 0) {
                hour = 12;
                ampm = " pm";
            } else if (hour - 12 > 0) {
                hour = hour - 12;
                ampm = " pm";
            } else {
                ampm = " am";
            }
            formatTime = hour + ":" + String.format("%02d", minute) + ampm;
        } catch (Exception e) {
            return formatTime;
        }

        return formatTime;
    }

    private void setPoPup() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent((StudentMainActivity) getActivity(), InputGradeLv1.class);
                startActivity(i);
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder((StudentMainActivity) getActivity())
                .setTitle("성적을 입력하시겠습니까?")
                .setMessage("본인의 현재를 진단하여, 목표에 가까워지는 학습을 찾아드립니다.\n" +
                        "추후 프로필설정에서 수정 가능")
                .setPositiveButton("확인", confirmListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    private void setListView() {

        home_Adapter = new ListViewAdapter(getActivity(), mArrayList);
        home_List.setAdapter(home_Adapter);

        home_List.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int i1) {
                if (i1 != 0) {
                    int count = 0;
                    for (int idx = 0; idx < mArrayList.size(); idx++) {
                        if (!mArrayList.get(idx).isClickable) {
                            count++;
                        }
                    }
                    Log.e("boo", StudentMainActivity.mTimer.hasMessages(0) + "");
                    if (count > 0) {
                        Toast.makeText((StudentMainActivity) getActivity(), "타이머가 동작중입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (todat_sdf.equals(today.getText().toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("secret", secret);
                                jsonObject.put("user_id", user_id);
                                jsonObject.put("activity_id", mArrayList.get(i).id);
                                jsonObject.put("activity_type", mArrayList.get(i).activityType);

                                requestJson(Url_define.Student_Delete_My_act + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Log.d("statusCode : ", statusCode + "");
                                        Log.d("response : ", response.toString());
                                        super.onSuccess(statusCode, headers, response);

                                        Toast.makeText((StudentMainActivity) getActivity(), "일정이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

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
                            mArrayList.remove(i);
                            home_Adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "삭제는 당일 활동만 가능합니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    if (mArrayList.get(i).startTimeforserver == null || !mArrayList.get(i).startTimeforserver.equals("")) {
                        if (mArrayList.get(i).activityType == 1) {
                            if (todat_sdf.equals(today.getText().toString())) {
                                Intent timeModify = new Intent(getActivity(), TimeModify.class);
                                timeModify.putExtra("activity_id", mArrayList.get(i).id);
                                timeModify.putExtra("start_time", mArrayList.get(i).startTimeforserver);
                                timeModify.putExtra("end_time", mArrayList.get(i).endTimeforserver);
                                timeModify.putExtra("duration", mArrayList.get(i).timer.replace("'", ":").replace("\"", ""));
                                timeModify.putExtra("activity_type", mArrayList.get(i).activityType);
                                timeModify.putExtra("date", today.getText().toString());
                                timeModify.putExtra("start_page", mArrayList.get(i).start_page);
                                timeModify.putExtra("end_page", mArrayList.get(i).end_page);
                                startActivity(timeModify);
                            } else {
                                Toast.makeText(getActivity(), "수정은 당일 활동만 가능합니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (todat_sdf.equals(today.getText().toString())) {
                                Intent timeModifyNonStudy = new Intent(getActivity(), TimeModifyNonStudy.class);
                                timeModifyNonStudy.putExtra("activity_id", mArrayList.get(i).id);
                                timeModifyNonStudy.putExtra("start_time", mArrayList.get(i).startTimeforserver);
                                timeModifyNonStudy.putExtra("end_time", mArrayList.get(i).endTimeforserver);
                                timeModifyNonStudy.putExtra("duration", mArrayList.get(i).timer.replace("'", ":").replace("\"", ""));
                                timeModifyNonStudy.putExtra("activity_type", mArrayList.get(i).activityType);
                                timeModifyNonStudy.putExtra("date", today.getText().toString());
                                startActivity(timeModifyNonStudy);
                            } else {
                                Toast.makeText(getActivity(), "수정은 당일 활동만 가능합니다.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {
                        Toast.makeText(getActivity(), "해당 일정을 마치고 수정하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;

            }

        });

    }

    public void addItem(int id, int Icon, String Category, String Textbook, String start_time, String end_time, String goal, int activityType, int activityTypeId, String start_page, String end_page) {
        StudentHomeListData addInfo = null;
        addInfo = new StudentHomeListData();
        addInfo.id = id;
        addInfo.icon = Icon;
        addInfo.category = Category;
        addInfo.textbook = Textbook;
        addInfo.startTimeforserver = start_time;
        addInfo.endTimeforserver = end_time;
        addInfo.goal = goal;
        addInfo.activityType = activityType;
        addInfo.activityTypeId = activityTypeId;
        addInfo.start_page = start_page;
        addInfo.end_page = end_page;

        mArrayList.add(addInfo);
    }

    private class ViewHolder {
        public ImageView icon, start, stop;
        public TextView Category, Textbook, Comment, Timer;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;

        public ArrayList<StudentHomeListData> getmListData() {
            return mListData;
        }

        private ArrayList<StudentHomeListData> mListData;

        public ListViewAdapter(Context mContext, ArrayList<StudentHomeListData> mList) {
            super();
            this.mContext = mContext;
            this.mListData = mList;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.home_list_item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon_image);
                holder.Category = (TextView) convertView.findViewById(R.id.category);
                holder.Textbook = (TextView) convertView.findViewById(R.id.textbook);
                holder.Comment = (TextView) convertView.findViewById(R.id.time);
                holder.Timer = (TextView) convertView.findViewById(R.id.timer);
                holder.start = (ImageView) convertView.findViewById(R.id.start);
                holder.stop = (ImageView) convertView.findViewById(R.id.stop);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final StudentHomeListData mData = mListData.get(position);

            String str = mData.timer;
            final SpannableStringBuilder sps = new SpannableStringBuilder(str);
            sps.setSpan(new AbsoluteSizeSpan(40), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.Timer.setText(sps);
            holder.icon.setBackgroundResource(mData.icon);
            holder.Category.setText(mData.category);
            holder.Textbook.setText(mData.textbook);
            if (mData.mStatus == STOP) {
                holder.Timer.setTextColor(Color.parseColor("#000000"));
                holder.Comment.setTextColor(Color.parseColor("#4c4c4c"));
                holder.Category.setTextColor(Color.parseColor("#000000"));
                holder.Textbook.setTextColor(Color.parseColor("#aaaaaa"));
                holder.start.setImageResource(R.drawable.start);
                holder.Comment.setText(mData.goal);

            } else if (mData.mStatus == RUNNING) {
                holder.Timer.setTextColor(Color.parseColor("#1FB4FF"));
                holder.Comment.setTextColor(Color.parseColor("#1FB4FF"));
                holder.Category.setTextColor(Color.parseColor("#1FB4FF"));
                holder.Textbook.setTextColor(Color.parseColor("#1FB4FF"));
                holder.start.setImageResource(R.drawable.pause);
                holder.Comment.setText(mData.startTime + " - ");
            } else if (mData.mStatus == PAUSE) {
                holder.Timer.setTextColor(Color.parseColor("#000000"));
                holder.Comment.setTextColor(Color.parseColor("#4c4c4c"));
                holder.Category.setTextColor(Color.parseColor("#000000"));
                holder.Textbook.setTextColor(Color.parseColor("#aaaaaa"));
                holder.start.setImageResource(R.drawable.start);
                holder.Comment.setText(mData.startTime + " - " + mData.endTime);
            } else if (mData.mStatus == FINAL) {
                holder.Timer.setTextColor(Color.parseColor("#000000"));
                holder.Comment.setTextColor(Color.parseColor("#4c4c4c"));
                holder.Category.setTextColor(Color.parseColor("#000000"));
                holder.Textbook.setTextColor(Color.parseColor("#aaaaaa"));
                holder.start.setImageResource(R.drawable.start);
                if (mData.startTime == null) {
                    mData.startTime = getNowTime();
                }
                holder.Comment.setText(mData.startTime + " - " + mData.endTime);
            }


            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (todat_sdf.equals(today.getText().toString())) {
                        if (mData.isClickable) {
                            StudentMainActivity.canMove = false;
                            if (mData.mStatus == STOP || mData.mStatus == FINAL) {
                                selectedPosition = position;
                                mData.mBaseTime = SystemClock.elapsedRealtime();        //SystemClock=날짜지원 클래스,	elapsedRealtim=부팅후 경과 시간
                                mData.mStatus = RUNNING;
                                holder.start.setImageResource(R.drawable.pause);
                                mData.startTime = getNowTime();
                                mData.startTimeforserver = getNowTimeForServer();
                                holder.Timer.setTextColor(Color.parseColor("#1FB4FF"));
                                holder.Comment.setTextColor(Color.parseColor("#1FB4FF"));
                                holder.Category.setTextColor(Color.parseColor("#1FB4FF"));
                                holder.Textbook.setTextColor(Color.parseColor("#1FB4FF"));
                                for (int i = 0; i < mListData.size(); i++) {
                                    mListData.get(i).isClickable = false;
                                }
                                mData.isClickable = true;
                                StudentMainActivity.mTimer.sendEmptyMessage(0);    //빈 메세지 보냄
                            } else if (mData.mStatus == RUNNING) {
                                mData.mPauseTime = SystemClock.elapsedRealtime();
                                holder.start.setImageResource(R.drawable.start);
                                mData.mStatus = PAUSE;
                                mData.endTime = getNowTime();
                                mData.endTimeforserver = getNowTimeForServer();
                                holder.Timer.setTextColor(Color.parseColor("#000000"));
                                holder.Comment.setTextColor(Color.parseColor("#4c4c4c"));
                                holder.Category.setTextColor(Color.parseColor("#000000"));
                                holder.Textbook.setTextColor(Color.parseColor("#aaaaaa"));
                                StudentMainActivity.mTimer.removeMessages(0);
                                home_Adapter.notifyDataSetChanged();
                            } else if (mData.mStatus == PAUSE) {
                                long now = SystemClock.elapsedRealtime();
                                mData.mBaseTime += (now - mData.mPauseTime);
                                mData.mStatus = RUNNING;
                                holder.start.setImageResource(R.drawable.pause);
                                holder.Timer.setTextColor(Color.parseColor("#1FB4FF"));
                                holder.Comment.setTextColor(Color.parseColor("#1FB4FF"));
                                holder.Category.setTextColor(Color.parseColor("#1FB4FF"));
                                holder.Textbook.setTextColor(Color.parseColor("#1FB4FF"));
                                StudentMainActivity.mTimer.sendEmptyMessage(0);    //빈 메세지 보냄

                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "타이머는 당일에만 작동합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (todat_sdf.equals(today.getText().toString())) {
                        if (mData.isClickable) {
                            if (mData.mStatus != STOP) {

                                StudentMainActivity.canMove = true;
                                DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mData.mStatus = FINAL;
                                        mData.endTime = getNowTime();
                                        mData.endTimeforserver = getNowTimeForServer();
                                        StudentMainActivity.mTimer.removeMessages(0);
                                        if (mData.startTimeforserver == null) {
                                            mData.startTimeforserver = getNowTimeForServer();
                                        }

                                        if (mData.activityType == 1) {
                                            Intent FinishActivity = new Intent(getActivity(), com.trams.azit.dialog.FinishActivity.class);
                                            FinishActivity.putExtra("activity_id", mData.id);
                                            FinishActivity.putExtra("start_time", mData.startTimeforserver);
                                            Log.e("start", mData.startTime);
                                            Log.e("startTimeforserver", mData.startTimeforserver);
                                            FinishActivity.putExtra("end_time", mData.endTimeforserver);
                                            FinishActivity.putExtra("duration", mData.timer.replace("'", ":").replace("\"", ""));
                                            FinishActivity.putExtra("activity_type", mData.activityType);
                                            startActivity(FinishActivity);

                                            holder.start.setImageResource(R.drawable.start);
                                            mData.timer = "00:00\'00\"";
                                            if (mData.startTime == null) {
                                                mData.startTime = getNowTime();
                                            }
                                            mData.endTime = getNowTime();
                                            holder.Timer.setTextColor(Color.parseColor("#000000"));
                                            holder.Comment.setTextColor(Color.parseColor("#4c4c4c"));
                                            holder.Category.setTextColor(Color.parseColor("#000000"));
                                            holder.Textbook.setTextColor(Color.parseColor("#aaaaaa"));

                                        } else {
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("secret", secret);
                                                jsonObject.put("user_id", user_id);
                                                jsonObject.put("start_time", mData.startTimeforserver);
                                                jsonObject.put("end_time", mData.endTimeforserver);
                                                jsonObject.put("activity_id", mData.id);
                                                jsonObject.put("duration", mData.timer.replace("'", ":").replace("\"", ""));
                                                jsonObject.put("activity_type", mData.activityType);

                                                Log.e("jsonObject", jsonObject.toString());

                                                requestJson(Url_define.Student_Send_My_Time + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                        Log.d("statusCode : ", statusCode + "");
                                                        Log.d("response : ", response.toString());
                                                        super.onSuccess(statusCode, headers, response);
                                                        try {
                                                            if (response.getString("result").equals("success")) {

                                                                holder.start.setImageResource(R.drawable.start);
                                                                mData.timer = "00:00\'00\"";
                                                                if (mData.startTime == null) {
                                                                    mData.startTime = getNowTime();
                                                                }
                                                                mData.endTime = getNowTime();
                                                                holder.Timer.setTextColor(Color.parseColor("#000000"));
                                                                holder.Comment.setTextColor(Color.parseColor("#4c4c4c"));
                                                                holder.Category.setTextColor(Color.parseColor("#000000"));
                                                                holder.Textbook.setTextColor(Color.parseColor("#aaaaaa"));

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

                                            for (int i = 0; i < mListData.size(); i++) {
                                                mListData.get(i).isClickable = true;
                                            }
                                            home_Adapter.notifyDataSetChanged();
                                        }
                                    }

                                };

                                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                };

                                new AlertDialog.Builder((StudentMainActivity) getActivity())
                                        .setTitle("오늘 활동을 마치시겠습니까?")
                                        .setPositiveButton("예", confirmListener)
                                        .setNegativeButton("아니오", cancelListener)
                                        .show();
                            }

                        }
                    } else {
                        Toast.makeText(getActivity(), "타이머는 당일에만 작동합니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            return convertView;
        }

    }

    private String getNowTime() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String time1 = sdf.format(dt);
        return time1;
    }

    private String getNowTimeForServer() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String time1 = sdf.format(dt);
        return time1;
    }


    private void cal_dday() {
        String str_sat = sat_day.getText().toString();
        if (!str_sat.equals("")) {
            String[] arr = str_sat.split("/");
            int day = caldate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
            dday.setText("D" + String.valueOf(day));
        } else {
            dday.setText("");
        }
    }


    public int caldate(int myear, int mmonth, int mday) {
        try {
            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            Calendar dday = Calendar.getInstance();

            dday.set(myear, mmonth, mday);// D-day의 날짜를 입력합니다.

            long day = dday.getTimeInMillis() / 86400000;
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )


            long tday = today.getTimeInMillis() / 86400000;
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count + 1; // 날짜는 하루 + 시켜줘야합니다.
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == my_study) {
            ((StudentMainActivity) getActivity()).onMenuItemClick(1);
        } else if (v == my_grade) {
            ((StudentMainActivity) getActivity()).onMenuItemClick(2);
        } else if (v == meet_mentor) {
            ((StudentMainActivity) getActivity()).onMenuItemClick(4);
        } else if (v == azit_clinic) {
            ((StudentMainActivity) getActivity()).onMenuItemClick(5);
        } else if (v == mentoring) {
            ((StudentMainActivity) getActivity()).onMenuItemClick(6);
        }

    }
}
