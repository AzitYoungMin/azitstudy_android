package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.MyProgressDialog;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-09-24.
 */
public class MyActPageFragment extends ConnFragment {

    public static final String ARG_PAGES = "ARG_PAGES";
    private ArrayList<AnswerListData> mAnswerList = new ArrayList<>();
    private ArrayList<ClinicData> mClinicList = new ArrayList<>();
    private ListViewAdapter my_act_Adapter;
    private ClinicListViewAdapter tab_clinic_Adapter;
    private SwipeMenuListView my_act_List;
    private int mPage;
    ListView tab_clinic_list;
    private JSONObject jsonObject;
    private SharedPreferences mPref;
    private static final String TAG = MyActPageFragment.class.getName();
    private MyProgressDialog pd;
    TextView notext;

    public static MyActPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGES, page);
        MyActPageFragment fragment = new MyActPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGES);
        mPref = getActivity().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        jsonObject = new JSONObject();
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getActivity());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#1fb4ff")));
            // set item width
            deleteItem.setWidth(dp2px(60));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            jsonObject.put("secret", mPref.getString("secret", ""));
            jsonObject.put("user_id", mPref.getString("user_id", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mPage == 0) {//답변 내역
            view = inflater.inflate(R.layout.tab_meet_mentoring, container, false);
            //notext = (TextView)view.findViewById(R.id.notext);

            my_act_List = (SwipeMenuListView) view.findViewById(R.id.tab_meet_mentoring_list);
            my_act_List.setMenuCreator(creator);
            my_act_List.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

            LoadFromServerMentoAnswer();
        } else if (mPage == 1) {//아지트클리닉
            view = inflater.inflate(R.layout.tab_clinic, container, false);
            //notext = (TextView)view.findViewById(R.id.notext);

            tab_clinic_list = (ListView) view.findViewById(R.id.tab_clinic_list);
            LoadFromServerClinic();
        }

        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void LoadFromServerClinic() {
        pd = MyProgressDialog.show(getActivity(), "", "", true, false, null);
        try {
            jsonObject.put("posting_type", "20");
            Log.d("LoadFromServerClinic", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestJson(Url_define.BASE + "/api/student/posting/list" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if (pd != null) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                        Log.d("statusCode", statusCode + "");
                        Log.d("onSuccess res", res.toString());
                        super.onSuccess(statusCode, headers, res);
                        try {
                            if (res.getString("result").equals("success")) {

                                jsonObject = res;
                                //서버의 데이터 수신이 성공하면
                                Log.d("resres", jsonObject.toString());
                                setClnicListView();
                            }else{
                               // notext.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject
                            res) {
                        if (pd != null) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                        Log.d("statusCode", statusCode + "");
                        Log.d("throwable", String.valueOf(t.getMessage()) == null ? String.valueOf(t.getMessage()) : "");
                        Log.d("onFailure res", res.toString());
                        Log.d("jsonObject : ", jsonObject.toString());
                        super.onFailure(statusCode, headers, t, res);
                        Toast.makeText(getContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }

    private void LoadFromServerMentoAnswer() {
        try {
            jsonObject.put("posting_type", "10");

            Log.d("MentoAnswer", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestJson(Url_define.BASE + "/api/student/posting/list" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                super.onSuccess(statusCode, headers, res);
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", res.toString());

                try {
                    if (res.getString("result").equals("success")) {

                        jsonObject = res;
                        //서버의 데이터 수신이 성공하면
                        setListView();
                    }else{
                        //notext.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                super.onFailure(statusCode, headers, t, res);
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                Log.d("statusCode", statusCode + "");
                Log.d("throwable", String.valueOf(t.getMessage()) == null ? String.valueOf(t.getMessage()) : "");
                Log.d("onFailure res", res.toString());
                Log.d("jsonObject : ", jsonObject.toString());
                Toast.makeText(getContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClnicListView() {
        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getString("posting_list"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject resJson = (JSONObject) jsonArray.get(i);
                JSONArray imageArray = new JSONArray(resJson.getString("images"));
                JSONObject firstImgUrl = new JSONObject();
                  if (imageArray.length() > 0) {
                    firstImgUrl = (JSONObject) imageArray.get(0);
                }
                Log.d("posting_id", resJson.getString("posting_id"));

                addItems(!firstImgUrl.has("image_url") ? "" : firstImgUrl.getString("image_url"),
                        resJson.getString("article"),
                        !resJson.has("profile_image") ? "" : resJson.getString("profile_image"),
                        resJson.isNull("title") ? "" : resJson.getString("title"),
                        resJson.getString("textbook"),
                        resJson.getBoolean("is_answered"),
                        resJson.getString("writer"),
                        resJson.getString("created_at"),
                        resJson.getString("num_of_reply"),
                        resJson.getString("posting_id"),
                        resJson.getString("year"),
                        resJson.getString("contents"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tab_clinic_Adapter = new ClinicListViewAdapter(getActivity(), mClinicList);
        tab_clinic_list.setAdapter(tab_clinic_Adapter);

        tab_clinic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //posting id를 넘겨야하는데...

                Intent i = new Intent(getActivity(), Mento_answer.class);
                i.putExtra("where", 2);
                i.putExtra("posting_id", mClinicList.get(position).posting_id);
                startActivity(i);
            }
        });
    }

    public void addItems(String pic, String text, String user_pic, String title, String textbook, Boolean status, String name, String time, String comment, String posting_id, String year, String content) {
        ClinicData addInfo = null;
        addInfo = new ClinicData();
        addInfo.pic = pic;
        addInfo.text = text;
        addInfo.user_pic = user_pic;
        addInfo.title = title;
        addInfo.textbook = textbook;
        addInfo.status = status;
        addInfo.name = name;
        addInfo.time = time;
        addInfo.comment = comment;
        addInfo.posting_id = posting_id;
        addInfo.year = year;
        addInfo.content = content;
        mClinicList.add(addInfo);
    }


    private class ClinicViewHolder {
        public TextView Clinic_text, Title, Textbook, Name, Time, Comment;
        public ImageView status, Clinic_pic, person_pic, trash;
    }

    private class ClinicListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<ClinicData> mListData;

        public ClinicListViewAdapter(Context mContext, ArrayList<ClinicData> mList) {
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
            final ClinicViewHolder holder;
            if (convertView == null) {
                holder = new ClinicViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.tab_clinic_item, null);

                holder.trash = (ImageView) convertView.findViewById(R.id.trash);
                holder.Clinic_text = (TextView) convertView.findViewById(R.id.tab_clinic_text);
                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.Textbook = (TextView) convertView.findViewById(R.id.textbook);
                holder.Name = (TextView) convertView.findViewById(R.id.name);
                holder.Time = (TextView) convertView.findViewById(R.id.time);
                holder.Comment = (TextView) convertView.findViewById(R.id.comment);
                holder.status = (ImageView) convertView.findViewById(R.id.status);
                holder.Clinic_pic = (ImageView) convertView.findViewById(R.id.tab_clinic_pic);
                holder.person_pic = (ImageView) convertView.findViewById(R.id.person_pic);

                convertView.setTag(holder);
            } else {
                holder = (ClinicViewHolder) convertView.getTag();
            }

            holder.trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DialogUtils.showConfirmAndCancelAlertDialog(getContext(), "삭제하시겠습니까?", new DialogUtils.ConfirmDialogOkCancelListener() {
                        @Override
                        public void onOkClick() {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("secret", mPref.getString("secret", ""));
                                obj.put("user_id", mPref.getString("user_id", ""));
                                obj.put("posting_id", Integer.parseInt(mClinicList.get(position).posting_id));
                                Log.e("obj", obj.toString());

                                requestJson(Url_define.posting_delete + Url_define.KEY, obj, new ConnHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            if (response.getString("result").equals("success")) {
                                                mListData.remove(position);
                                                tab_clinic_Adapter.notifyDataSetChanged();
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });


                }
            });

            final ClinicData mData = mListData.get(position);

            if (mData.status) {
                holder.status.setImageResource(R.drawable.answer_complete);
            } else {
                holder.status.setImageResource(R.drawable.img_new);
            }

            if (mData.pic.equals("")) {
                holder.Clinic_pic.setImageResource(0);
                holder.Clinic_pic.setVisibility(View.GONE);
            } else {
                holder.Clinic_pic.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(Url_define.BASE_Image + mData.pic).resize(300, 300).into(holder.Clinic_pic);
                Log.e("mData.pic", mData.pic);
            }

            if (mData.user_pic.equals("")) {
                holder.person_pic.setImageResource(R.drawable.profile_basic_icon);
            } else {
                Picasso.with(getContext()).load(Url_define.BASE_Image + mData.user_pic).into(holder.person_pic);
            }

            String sourceString = "<b>" + mData.name + "(고" + mData.year + ") : " + "</b> " + mData.content;
            holder.Textbook.setText(Html.fromHtml(sourceString));
            holder.Title.setText(mData.textbook);
            holder.Name.setText(mData.name);
            holder.Time.setText(mData.time);
            holder.Comment.setText(mData.comment);

            return convertView;
        }
    }

    private void setListView() {
        try {
            int jsonLength = Integer.parseInt(jsonObject.getString("num_of_posting"));
            for (int i = 0; i < jsonLength; i++) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("posting_list"));
                JSONObject resJson = (JSONObject) jsonArray.get(i);
                addItem(resJson.isNull("title") ? "" : resJson.getString("title"),
                        resJson.getString("writer"),
                        resJson.getString("created_at"),
                        resJson.getString("num_of_reply"),
                        resJson.getString("posting_id"),
                        resJson.getBoolean("is_answered"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        my_act_Adapter = new ListViewAdapter(getActivity(), mAnswerList);
        my_act_List.setAdapter(my_act_Adapter);

        my_act_List.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int i, SwipeMenu swipeMenu, int i1) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("secret", mPref.getString("secret", ""));
                    obj.put("user_id", mPref.getString("user_id", ""));
                    obj.put("posting_id", Integer.parseInt(mAnswerList.get(i).posting_id));
                    Log.e("obj", obj.toString());

                    requestJson(Url_define.posting_delete + Url_define.KEY, obj, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (response.getString("result").equals("success")) {
                                    mAnswerList.remove(i);
                                    my_act_Adapter.notifyDataSetChanged();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
        my_act_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Mento_answer.class);
                i.putExtra("where", 1);
                i.putExtra("posting_id", mAnswerList.get(position).posting_id);
                startActivity(i);
            }
        });
    }

    public void addItem(String title, String name, String time, String comment, String posting_id, Boolean is_answered) {
        AnswerListData addInfo = null;
        addInfo = new AnswerListData();
        addInfo.Title = title;
        addInfo.Name = name;
        addInfo.Time = time;
        addInfo.Comment = comment;
        addInfo.posting_id = posting_id;
        addInfo.is_answered = is_answered;
        mAnswerList.add(addInfo);
    }

    private class ViewHolder {
        public TextView Title, Name, Time, Comment;
        public ImageView Status;
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
                convertView = inflater.inflate(R.layout.answer_list_item, null);

                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.Name = (TextView) convertView.findViewById(R.id.name);
                holder.Time = (TextView) convertView.findViewById(R.id.time);
                holder.Comment = (TextView) convertView.findViewById(R.id.comment);
                holder.Status = (ImageView) convertView.findViewById(R.id.answer_complete);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AnswerListData mData = mListData.get(position);

            holder.Title.setText(mData.Title);
            holder.Name.setText(mData.Name);
            holder.Time.setText(mData.Time);
            holder.Comment.setText(mData.Comment);

            if (mData.is_answered) {
                holder.Status.setBackgroundResource(R.drawable.answer_complete);
            } else {
                holder.Status.setBackgroundResource(R.drawable.new_commet);
            }

            return convertView;
        }
    }
}
