package com.trams.azit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.R;
import com.trams.azit.dialog.RefundPointActivity;
import com.trams.azit.model.PointModel;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 14/01/2016.
 */
public class MyPointActivity extends ConnActivity implements View.OnClickListener {

    private TextView btnSave, btnRefund, point_btn, my_point_text;
    private LinearLayout no_point_ll;
    private ImageView img_back;
    private ListView lvPoint;
    SharedPreferences myPrefs;
    String secret, user_id;
    private ListViewAdapter adapter;
    private ArrayList<PointModel> itemArrayList = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.BASE + "/api/mentor/info" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        my_point_text.setText("내 포인트 : " + response.getString("point") + " pt");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_management);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        my_point_text = (TextView) findViewById(R.id.my_point_text);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        btnSave = (TextView) findViewById(R.id.saving);
        btnSave.setOnClickListener(this);

        btnRefund = (TextView) findViewById(R.id.refund);
        btnRefund.setOnClickListener(this);

        point_btn = (TextView) findViewById(R.id.point_btn);
        point_btn.setOnClickListener(this);

        no_point_ll = (LinearLayout) findViewById(R.id.no_point_ll);
        lvPoint = (ListView) findViewById(R.id.lv_my_point);
        adapter = new ListViewAdapter(MyPointActivity.this, itemArrayList);
        lvPoint.setAdapter(adapter);
        lvPoint.setVisibility(View.GONE);
        no_point_ll.setVisibility(View.VISIBLE);
        itemArrayList.clear();


        LoadFromServer();
        LoadSavingPointFromServer();

    }

    private void LoadSavingPointFromServer() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Log.e("jsonObject", jsonObject.toString());
            Log.e("Url", Url_define.mentor_savingpoint + Url_define.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson(Url_define.mentor_savingpoint + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getString("result").equals("success")) {
                        if (response.getString("num_of_save").equals("0")) {
                            no_point_ll.setVisibility(View.VISIBLE);
                            lvPoint.setVisibility(View.GONE);

                        } else {
                            no_point_ll.setVisibility(View.GONE);
                            lvPoint.setVisibility(View.VISIBLE);
                            JSONArray save_list = new JSONArray(response.getString("save_list"));
                            for (int i = 0; i < save_list.length(); i++) {

                                String point = null;
                                if (Integer.parseInt(save_list.getJSONObject(i).getString("addedPoint")) > 0) {
                                    point = "+" + save_list.getJSONObject(i).getString("addedPoint");
                                } else {
                                    point = save_list.getJSONObject(i).getString("addedPoint");
                                }

                                addItem("답변보상",
                                        save_list.getJSONObject(i).getString("date"),
                                        save_list.getJSONObject(i).getString("title"),
                                        "포인트 " + point,
                                        "잔액: " + save_list.getJSONObject(i).getInt("balance"));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                super.onFailure(statusCode, headers, t, res);
            }
        });
    }

    private void LoadRefundPointFromServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson(Url_define.mentor_refundpoint + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getString("result").equals("success")) {
                        if (response.getString("num_of_save").equals("0")) {
                            no_point_ll.setVisibility(View.VISIBLE);
                            lvPoint.setVisibility(View.GONE);

                        } else {
                            no_point_ll.setVisibility(View.GONE);
                            lvPoint.setVisibility(View.VISIBLE);
                            JSONArray save_list = new JSONArray(response.getString("refund_list"));
                            for (int i = 0; i < save_list.length(); i++) {
                                addItem("환급내역",
                                        save_list.getJSONObject(i).getString("date"),
                                        save_list.getJSONObject(i).getString("account"),
                                        "포인트 -" + save_list.getJSONObject(i).getInt("point"),
                                        "잔액: " + save_list.getJSONObject(i).getInt("balance"));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                super.onFailure(statusCode, headers, t, res);
            }
        });
    }

    public void addItem(String type, String date, String title, String pointChange, String balancePoint) {

        PointModel item = new PointModel();

        item.type = type;
        item.date = date;
        item.title = title;
        item.pointChange = pointChange;
        item.balancePoint = balancePoint;
        itemArrayList.add(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.saving:
                doSaving();
                break;

            case R.id.refund:
                doRefund();
                break;

            case R.id.point_btn:
                Intent i = new Intent(MyPointActivity.this, RefundPointActivity.class);
                startActivity(i);
                break;

        }
    }

    private void doRefund() {
        btnRefund.setBackgroundColor(Color.parseColor("#EAEAEA"));
        btnRefund.setTextColor(Color.parseColor("#4F5563"));
        btnSave.setBackgroundColor(Color.parseColor("#F7F7F7"));
        btnSave.setTextColor(Color.parseColor("#828282"));
        lvPoint.setVisibility(View.GONE);
        no_point_ll.setVisibility(View.VISIBLE);
        itemArrayList.clear();

        LoadRefundPointFromServer();
    }


    private void doSaving() {
        btnSave.setBackgroundColor(Color.parseColor("#EAEAEA"));
        btnSave.setTextColor(Color.parseColor("#4F5563"));
        btnRefund.setBackgroundColor(Color.parseColor("#F7F7F7"));
        btnRefund.setTextColor(Color.parseColor("#828282"));
        lvPoint.setVisibility(View.GONE);
        no_point_ll.setVisibility(View.VISIBLE);
        itemArrayList.clear();

        LoadSavingPointFromServer();
    }

    public class ViewHolder {
        public TextView type;
        public TextView date;
        public TextView title;
        public TextView pointChange;
        public TextView balancePoint;

    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<PointModel> mListData;

        public ListViewAdapter(Context mContext, ArrayList<PointModel> list) {
            this.mContext = mContext;
            this.mListData = list;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_point, null);

                holder.type = (TextView) convertView.findViewById(R.id.type);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.pointChange = (TextView) convertView.findViewById(R.id.pointChange);
                holder.balancePoint = (TextView) convertView.findViewById(R.id.balancePoint);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mListData != null && mListData.size() != 0) {
                PointModel mData = mListData.get(position);
                holder.type.setText(mData.type);
                holder.date.setText(mData.date);
                holder.title.setText(mData.title);
                holder.pointChange.setText(mData.pointChange);
                holder.balancePoint.setText(mData.balancePoint);
            }

            return convertView;
        }

    }
}
