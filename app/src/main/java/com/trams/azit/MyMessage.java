package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyMessage extends ConnActivity {

    ImageView back;
    SharedPreferences myPrefs;
    String secret, user_id;
    private ArrayList<AnswerListData> mAnswerList = new ArrayList<AnswerListData>();
    private ListViewAdapter message_Adapter;
    private SwipeMenuListView message_List;
    private JSONObject jsonObject;
    private SharedPreferences mPref;
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(MyMessage.this);
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF3E3E")));
            // set item width
            deleteItem.setWidth(dp2px(60));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messsage);

        myPrefs = getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        init();
        LoadFromServer();

    }

    private void init() {
        back = (ImageView) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPref = getApplicationContext().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", mPref.getString("secret", ""));
            jsonObject.put("user_id", mPref.getString("user_id", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LoadFromServer() {
        requestJson(Url_define.BASE + "/api/message/list" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", res.toString());
                jsonObject = res;
                //서버의 데이터 수신이 성공하면
                setListView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                Log.d("statusCode", statusCode + "");
                Log.d("throwable", String.valueOf(t.getMessage()) == null ? String.valueOf(t.getMessage()) : "");
                Log.d("onFailure res", res.toString());
                Log.d("jsonObject : ", jsonObject.toString());
                Toast.makeText(getApplicationContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setListView() {
        Log.d("setListView", jsonObject.toString());
        message_List = (SwipeMenuListView) findViewById(R.id.message_listdata);
        message_List.setMenuCreator(creator);
        message_List.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        try {
            JSONArray datas = new JSONArray(jsonObject.getString("messages"));

            for (int i = 0; i < datas.length(); i++) {

                JSONObject resJson = datas.getJSONObject(i);
                addItem(resJson.getString("title"), resJson.getString("name"), resJson.getString("created_at"), resJson.getInt("message_id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        message_Adapter = new ListViewAdapter(MyMessage.this, mAnswerList);
        message_List.setAdapter(message_Adapter);

        message_List.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int i1) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("message_id", mAnswerList.get(i).idx);

                    requestJson(Url_define.Common_Delete_message + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            Toast.makeText(MyMessage.this, "메세지가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

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

                mAnswerList.remove(i);
                message_Adapter.notifyDataSetChanged();
                return false;
            }
        });
        message_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyMessage.this, My_Message_Detail.class);
                i.putExtra("message_id", mAnswerList.get(position).idx);
                startActivity(i);

            }
        });
    }

    public void addItem(String title, String name, String time, int idx) {
        AnswerListData addInfo = null;
        addInfo = new AnswerListData();
        addInfo.Title = title;
        addInfo.Name = name;
        addInfo.Time = time;
        addInfo.idx = idx;
        mAnswerList.add(addInfo);
    }

    private class ViewHolder {
        public TextView Title, Name, Time, Comment;
    }

    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<AnswerListData> mListData;

        public ListViewAdapter(Context mContext, ArrayList<AnswerListData> mList) {
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
                convertView = inflater.inflate(R.layout.answer_message_list_item, null);

                holder.Title = (TextView) convertView.findViewById(R.id.message_title);
                holder.Name = (TextView) convertView.findViewById(R.id.message_name);
                holder.Time = (TextView) convertView.findViewById(R.id.message_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AnswerListData mData = mListData.get(position);

            holder.Title.setText(mData.Title);
            holder.Name.setText(mData.Name);
            holder.Time.setText(mData.Time);

            return convertView;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
