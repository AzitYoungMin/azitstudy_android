package com.trams.azit;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.trams.azit.preference.PreferUtils;
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
public class ClinicPageFragment extends ConnFragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int index;
    private int noOfBtns, CurrentPage = 1;
    private TextView[] btns;
    ListView tab_clinic_list;
    LinearLayout ll;
    TextView notext;
    private ArrayList<ClinicData> mArrayList = new ArrayList<ClinicData>();
    private ListViewAdapter tab_clinic_Adapter;
    public int NUM_ITEMS_PAGE = 7;
    ImageView num_left, num_right;
    Typeface tf;
    private JSONObject jsonObject;
    private SharedPreferences mPref;
    private static final String TAG = ClinicPageFragment.class.getName();
    private MyProgressDialog pd;

    public static ClinicPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ClinicPageFragment fragment = new ClinicPageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        index = data.getInt(ARG_PAGE);
        mPref = getActivity().getSharedPreferences("Azit", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        jsonObject = new JSONObject();

        if (index == 0) {//국어
            v = inflater.inflate(R.layout.tab_clinic, container, false);
            setPaging(savedInstanceState, v);
            LoadFromServer("10000");
        } else if (index == 1) {
            v = inflater.inflate(R.layout.tab_clinic, container, false);
            setPaging(savedInstanceState, v);
            LoadFromServer("30000");
        } else if (index == 2) {
            v = inflater.inflate(R.layout.tab_clinic, container, false);
            setPaging(savedInstanceState, v);
            LoadFromServer("50000");
        } else if (index == 3) {
            v = inflater.inflate(R.layout.tab_clinic, container, false);
            setPaging(savedInstanceState, v);
            LoadFromServer("70000");
        } else if (index == 4) {
            v = inflater.inflate(R.layout.tab_clinic, container, false);
            setPaging(savedInstanceState, v);
            LoadFromServer("130000");
        }
        return v;
    }

    private void setPaging(Bundle savedInstanceState, View v) {
        tab_clinic_list = (ListView) v.findViewById(R.id.tab_clinic_list);
        notext = (TextView)v.findViewById(R.id.notext);
        View footer = getLayoutInflater(savedInstanceState).inflate(R.layout.footer, null, false);
        tab_clinic_list.addFooterView(footer);
        ll = (LinearLayout) footer.findViewById(R.id.btnLay);
        num_left = (ImageView) footer.findViewById(R.id.num_left);
        num_right = (ImageView) footer.findViewById(R.id.num_right);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "NanumGothic.ttf");

        num_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentPage == 1) {//1부터시작!
                    Toast.makeText(getContext(), "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    loadList(CurrentPage - 1);
                    CheckBtnBackGroud(CurrentPage - 1);
                    CurrentPage = CurrentPage - 1;
                }
            }
        });
        num_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CurrentPage", CurrentPage+"");
                Log.e("noOfBtns", noOfBtns+"");
                if (CurrentPage == noOfBtns ) {
                    Toast.makeText(getContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    loadList(CurrentPage + 1);
                    CheckBtnBackGroud(CurrentPage + 1);
                    CurrentPage = CurrentPage + 1;
                }
            }
        });
    }

    private void LoadFromServer(String subject_id) {
        pd = MyProgressDialog.show(getActivity(), "", "", true, false, null);

        try {
            jsonObject.put("secret", PreferUtils.getSecret(getContext()));
            jsonObject.put("posting_type", "20");
            jsonObject.put("subject_id", subject_id);
            jsonObject.put("page_number", CurrentPage);
            jsonObject.put("page_size", "10");
            Log.d("requestjson", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson(Url_define.BASE + "/api/posting/list" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                super.onSuccess(statusCode, headers, res);
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", res.toString());
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                try {
                    if (res.getString("result").equals("success")){
                        jsonObject = res;
                        //서버의 데이터 수신이 성공하면
                        mArrayList.clear();
                        setListView();

                    }else{
                        notext.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if (tab_clinic_Adapter != null)
//                    tab_clinic_Adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                super.onFailure(statusCode, headers, t, res);
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                Log.d("statusCode", statusCode + "");

                t.printStackTrace();
                Log.d("jsonObject : ", jsonObject.toString());
                Toast.makeText(getContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setListView() {

        try {
            if (!jsonObject.has("posting_list")) return;

            JSONArray jsonArray = new JSONArray(jsonObject.getString("posting_list"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject resJson = (JSONObject) jsonArray.get(i);
                JSONObject firstImgUrl = new JSONObject();

                JSONArray imageArray = new JSONArray(resJson.getString("images"));
                if (imageArray.length() > 0) {
                    firstImgUrl = (JSONObject) imageArray.get(0);
                }

                addItem(!firstImgUrl.has("image_url") ? "" : firstImgUrl.getString("image_url"),
                        resJson.getString("article")
                        , !resJson.has("profile_image") ? "" : resJson.getString("profile_image")
                        , resJson.isNull("title") ? "" : resJson.getString("title")
                        , resJson.getString("textbook"), resJson.getBoolean("is_answered"), resJson.getString("writer")
                        , resJson.getString("created_at"), resJson.getString("num_of_reply"), resJson.getString("posting_id"), resJson.getString("year"), resJson.getString("contents"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadList(0);
        Btnfooter();

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void Btnfooter() {
        int val = mArrayList.size() % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = mArrayList.size() / NUM_ITEMS_PAGE + val;
        btns = new TextView[noOfBtns];
        for (int i = 0; i < noOfBtns; i++) {
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
            lp.setMargins(20, 0, 20, 0);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    CurrentPage = j;
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }
    }

    private void loadList(int number) {
        ArrayList<ClinicData> sort = new ArrayList<ClinicData>();

        int start = number * NUM_ITEMS_PAGE;
        for (int i = start; i < (start) + NUM_ITEMS_PAGE; i++) {
            if (i < mArrayList.size()) {
                sort.add(mArrayList.get(i));
            } else {
                break;
            }
        }
        tab_clinic_Adapter = new ListViewAdapter(getActivity(), sort);
        tab_clinic_list.setAdapter(tab_clinic_Adapter);
        tab_clinic_Adapter.notifyDataSetChanged();
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

    public void addItem(String pic, String text, String user_pic, String title, String textbook, Boolean status, String name, String time, String comment, String posting_id, String year, String content) {
        ClinicData addInfo = new ClinicData();
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
        mArrayList.add(addInfo);
    }

    private class ViewHolder {
        public TextView Clinic_text, Title, Textbook, Name, Time, Comment;
        public ImageView status, Clinic_pic, person_pic, trash;
        public LinearLayout layout_root;
    }

    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<ClinicData> mListData;

        public ListViewAdapter(Context mContext, ArrayList<ClinicData> mList) {
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
                holder.layout_root = (LinearLayout) convertView.findViewById(R.id.layout_item_clinic_root);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.trash.setVisibility(View.GONE);

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
               //holder.person_pic.setImageResource(R.drawable.profile_basic_icon);
                Picasso.with(getContext()).load(R.drawable.profile_basic_icon).into(holder.person_pic);
            } else {
                Picasso.with(getContext()).load(Url_define.BASE_Image + mData.user_pic).into(holder.person_pic);
            }

            String sourceString = "<b>" + mData.name + "(고" + mData.year + ") : " + "</b> " + mData.content;
            holder.Textbook.setText(Html.fromHtml(sourceString));

            holder.Clinic_text.setText(mData.text);
            holder.Title.setText(mData.textbook);
            holder.Name.setText(mData.name);
            holder.Time.setText(mData.time);
            holder.Comment.setText(mData.comment);

            holder.layout_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), Mento_answer.class);
                    i.putExtra("where", 2);
                    i.putExtra("posting_id", mData.posting_id);
                    startActivity(i);
                }
            });

            return convertView;
        }
    }
}
