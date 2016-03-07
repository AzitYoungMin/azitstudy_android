package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2015-10-23.
 */
public class DDay_Change extends ConnActivity {

    ListView dday_list;
    ImageView back, plus;
    int selected_position = -1;
    private ArrayList<DdayListData> mArrayList = new ArrayList<DdayListData>();
    private ListViewAdapter dday_Adapter;
    SharedPreferences myPrefs;
    String secret, user_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DDay_Change.this, StudentMainActivity.class);
        i.putExtra("position", 0);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday_change);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        setBackBtn();
        setPlusBtn();
        setListView();
        LoadFromServer();

    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.Student_Get_DDayList + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        JSONArray datas = new JSONArray(response.getString("dday_list"));

                        for (int i = 0; i < datas.length(); i++) {
                            addItem(datas.getJSONObject(i).getInt("id"), datas.getJSONObject(i).getString("title"), datas.getJSONObject(i).getString("date"), getDday(datas.getJSONObject(i).getString("date")), datas.getJSONObject(i).getBoolean("isActive"));
                        }
                        dday_Adapter.notifyDataSetChanged();
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

    private void setListView() {
        dday_list = (ListView) findViewById(R.id.dday_list);
        View footer = getLayoutInflater().inflate(R.layout.dday_footer, null, false);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("dday_id", String.valueOf(mArrayList.get(selected_position).id));

                    Logger.e(secret);
                    Logger.e(user_id);
                    requestJson(Url_define.Student_Set_DDay + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            Intent i = new Intent(DDay_Change.this, StudentMainActivity.class);
                            i.putExtra("position", 0);
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
        dday_list.addFooterView(footer);
        dday_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        dday_Adapter = new ListViewAdapter(this, mArrayList);
        dday_list.setAdapter(dday_Adapter);

        dday_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dday_Adapter.notifyDataSetChanged();
            }
        });
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

    public int caldate(int myear, int mmonth, int mday) {
        try {
            Log.e("myear", myear + "");
            Log.e("mmonth", mmonth + "");
            Log.e("mday", mday + "");

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

    private void setPlusBtn() {
        plus = (ImageView) findViewById(R.id.plus_btn);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DDay_Change.this, Plus_DDay.class);
                startActivity(i);


            }
        });
    }

    private void setBackBtn() {
        back = (ImageView) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addItem(int id, String title, String day, String dday, Boolean isActive) {
        DdayListData addInfo = null;
        addInfo = new DdayListData();
        addInfo.id = id;
        addInfo.title = title;
        addInfo.day = day;
        addInfo.dday = dday;
        addInfo.selected = isActive;
        mArrayList.add(addInfo);
    }

    private class ViewHolder {
        public RelativeLayout dday_ll;
        public TextView Title, day, dday;
        public ImageView next_btn;
        public RadioButton event_rbtn;
    }

    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<DdayListData> mListData;

        public ListViewAdapter(Context mContext, ArrayList<DdayListData> mList) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.dday_list_item, null);

                holder.dday_ll = (RelativeLayout) convertView.findViewById(R.id.dday_ll);
                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.day = (TextView) convertView.findViewById(R.id.day);
                holder.dday = (TextView) convertView.findViewById(R.id.dday);
                holder.next_btn = (ImageView) convertView.findViewById(R.id.next_btn);
                holder.event_rbtn = (RadioButton) convertView.findViewById(R.id.event_rbtn);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final DdayListData mData = mListData.get(position);

            holder.event_rbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < mListData.size(); i++) {
                        mListData.get(i).selected = false;
                    }
                    mData.selected = true;
                    selected_position = position;
                    dday_Adapter.notifyDataSetChanged();
                }
            });

            holder.dday_ll.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.Title.setTextColor(Color.parseColor("#000000"));
            holder.event_rbtn.setChecked(mData.selected);
            holder.event_rbtn.setBackgroundResource(R.drawable.event_round);
            holder.dday.setTextColor(Color.parseColor("#1FB4FF"));
            holder.next_btn.setBackgroundResource(R.drawable.next_black);

            if (mData.selected) {
                holder.dday_ll.setBackgroundColor(Color.parseColor("#33393d"));
                holder.Title.setTextColor(Color.parseColor("#ffffff"));
                holder.event_rbtn.setChecked(true);
                holder.event_rbtn.setBackgroundResource(R.drawable.event_round_push);
                holder.dday.setTextColor(Color.parseColor("#ff92a2"));
                holder.next_btn.setBackgroundResource(R.drawable.next_wht);
                selected_position = position;
            } else {

                holder.dday_ll.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.Title.setTextColor(Color.parseColor("#000000"));
                holder.event_rbtn.setChecked(false);
                holder.event_rbtn.setBackgroundResource(R.drawable.event_round);
                holder.dday.setTextColor(Color.parseColor("#1FB4FF"));
                holder.next_btn.setBackgroundResource(R.drawable.next_black);
            }

            holder.Title.setText(mData.title);
            holder.day.setText(mData.day);
            holder.dday.setText(mData.dday);
            holder.next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DDay_Change.this, Edit_DDay.class);
                    i.putExtra("dday_id", mData.id);
                    i.putExtra("title", mData.title);
                    startActivity(i);
                }
            });
            return convertView;
        }
    }
}
