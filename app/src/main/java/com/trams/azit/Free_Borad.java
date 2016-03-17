package com.trams.azit;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015-09-24.
 */
public class Free_Borad extends ConnFragment {

    private ArrayList<AnswerListData> mArrayList = new ArrayList<AnswerListData>();
    private ListViewAdapter meet_mentor_Adapter;
    ListView meet_mentor_list;
    private int noOfBtns, CurrentPage = 0;
    private boolean hasFooter = false;
    public String image_src = null;
    ImageView num_left, num_right;
    public int NUM_ITEMS_PAGE = 9;
    LinearLayout ll;
    TextView[] btns;
    Typeface tf;
    SharedPreferences myPrefs;
    String secret, user_id;
    JSONObject jo = null;
    @Override
    public void onResume() {
        super.onResume();
        mArrayList.clear();
        LoadFromServer(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meet_mentor, null);

        myPrefs = this.getActivity().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        meet_mentor_list = (ListView) view.findViewById(R.id.meet_mentor_list);
        ll = (LinearLayout) view.findViewById(R.id.btnLay);
        num_left = (ImageView) view.findViewById(R.id.num_left);
        num_right = (ImageView) view.findViewById(R.id.num_right);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "NanumGothic.ttf");

        num_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentPage == 0) {
                    Toast.makeText(getContext(), "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    LoadFromServer(CurrentPage - 1);
                    CheckBtnBackGroud(CurrentPage - 1);
                    CurrentPage = CurrentPage - 1;
                }
            }
        });
        num_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentPage == noOfBtns - 1) {
                    Toast.makeText(getContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    LoadFromServer(CurrentPage + 1);
                    CheckBtnBackGroud(CurrentPage + 1);
                    CurrentPage = CurrentPage + 1;
                }
            }
        });

        setListView();

        return view;
    }

    private void LoadFromServer(int num) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);


            requestJson("http://192.168.1.21:2000/FreeBorad/", jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            int is_answered = 0;
                            addItem(response.getJSONObject(i).getInt("Posting_id"), response.getJSONObject(i).getString("Title"), response.getJSONObject(i).getString("Content"), response.getJSONObject(i).getString("Text"), response.getJSONObject(i).getString("Image") ,response.getJSONObject(i).getString("Created_at"),response.getJSONObject(i).getString("User_image"), String.valueOf(response.getJSONObject(i).getInt("Num_of_reply")));
                        }

                       /* String noOfBtn = response.getString("last_page_number");
                        if (!hasFooter) {
                            Btnfooter(Integer.parseInt(noOfBtn));
                        }
                        */
                        meet_mentor_Adapter.notifyDataSetChanged();

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

//        loadList(0);
        meet_mentor_Adapter = new ListViewAdapter(getActivity(), mArrayList);
        meet_mentor_list.setAdapter(meet_mentor_Adapter);

        meet_mentor_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent((StudentMainActivity) getActivity(), BoardView.class);
                i.putExtra("where", 1);
                i.putExtra("posting_id", String.valueOf(mArrayList.get(position).idx));
//                Log.e("posting_id", mArrayList.get(position).idx)
                startActivity(i);
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void Btnfooter(int num) {
        hasFooter = true;
        noOfBtns = num;
        btns = new TextView[num];
        for (int i = 0; i < num; i++) {
            btns[i] = new TextView(getContext());
            btns[i].setHeight(dp2px(26));
            btns[i].setGravity(Gravity.CENTER);
            btns[i].setWidth(dp2px(26));
            btns[i].setTypeface(tf);

            btns[i].setText("" + (i + 1));
            if (btns[i].getText().toString().equals("1")) {
                btns[i].setBackgroundColor(Color.parseColor("#52a5e8"));
                btns[i].setTextColor(Color.parseColor("#ffffff"));

            } else {
                btns[i].setBackgroundColor(Color.parseColor("#ffffff"));
                btns[i].setTextColor(Color.parseColor("#878787"));
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(25);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    CurrentPage = j;
                    mArrayList.clear();
                    meet_mentor_Adapter.notifyDataSetChanged();
                    LoadFromServer(j + 1);
                    meet_mentor_Adapter.notifyDataSetChanged();
//                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }
    }


    private void CheckBtnBackGroud(int index) {
        for (int i = 0; i < noOfBtns; i++) {
            if (i == index) {
                btns[index].setBackgroundColor(Color.parseColor("#52a5e8"));
                btns[i].setTextColor(Color.parseColor("#ffffff"));

            } else {
                btns[i].setBackgroundColor(Color.parseColor("#ffffff"));
                btns[i].setTextColor(Color.parseColor("#878787"));
            }
        }
    }

    public void addItem(int idx, String title, String name,String text, String image ,String time, String user_image, String comment) {
        AnswerListData addInfo = null;
        addInfo = new AnswerListData();
        addInfo.idx = idx;
        addInfo.Title = title;
        addInfo.Name = name;
        addInfo.Text = text;
        addInfo.Image = image;
        addInfo.Time = time;
        addInfo.User_Image = user_image;
        addInfo.Comment = comment;
        mArrayList.add(addInfo);
    }

    private class ViewHolder {
        public TextView Title, Name,Text, Time, Comment;
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
                convertView = inflater.inflate(R.layout.freeboard_ltem_list, null);

                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.Name = (TextView) convertView.findViewById(R.id.name);
                holder.Text = (TextView) convertView.findViewById(R.id.context121);
                holder.Time = (TextView) convertView.findViewById(R.id.time);
                holder.Comment = (TextView) convertView.findViewById(R.id.comment);



                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AnswerListData mData = mListData.get(position);
           /* long now = System.currentTimeMillis();
            Date date = new Date(now);

            String start = ;

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd" , Locale.KOREA);
                String end = formatter.format(date);
                Date beginDate = formatter.parse(start);
                Date endDate = formatter.parse(end);

                // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
                long diff = endDate.getTime() - beginDate.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                String gg11 = String.valueOf(diffDays);




                if(diffDays != 0) {
                    holder.Time.setText(gg11 + "일 전");
                } else {
                    //분 and 시간 으로 계산하는거 나중에 구해야함... 일단은 이정도..
                    holder.Time.setText(mData.Time);

                }



            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            //holder.Title.setText(mData.Title);
            holder.Name.setText(mData.Name);
            holder.Text.setText(mData.Text);
            holder.Time.setText(mData.Time);
            holder.Comment.setText(mData.Comment);

            ImageView img = (ImageView) convertView.findViewById(R.id.user_img);
            ImageView ims = (ImageView) convertView.findViewById(R.id.image_ser);

            if (mData.Image.equals("none")) {
               // Picasso.with(convertView.getContext()).load("http://192.168.1.21:2000/5577006791947779410.jpg").into(ims);
                ims.setVisibility(View.GONE);
            } else {
                ims.setVisibility(View.VISIBLE);
                Picasso.with(convertView.getContext()).load("http://192.168.1.21:2000/" + mData.Image).into(ims);

            }


             Picasso.with(convertView.getContext()).load(Url_define.BASE_Image + mData.User_Image).placeholder(R.drawable.profile_basic_icon).error(R.drawable.profile_basic_icon).fit().into(img);



            return convertView;
        }
    }
}
