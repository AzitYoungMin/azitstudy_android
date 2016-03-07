package com.trams.azit.activity;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.model.NoticeObj;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 07/01/2016.
 */
@EActivity(R.layout.activity_notice)
public class NoticeActivity extends ConnActivity {

    @ViewById
    ExpandableListView lvNotice;

    @ViewById
    ImageView img_back_teacher_setting;

    List<NoticeObj> listDataHeader;
    HashMap<NoticeObj, List<String>> listDataChild;

    private ExpandableListAdapter noticeAdapter;
    private static final String TAG = NoticeActivity.class.getName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void addItem(int id, String title, String date) {
        NoticeObj addInfo = null;
        addInfo = new NoticeObj();
        addInfo.id = id;
        addInfo.Title = title;
        addInfo.Date = date;

        listDataHeader.add(addInfo);
    }

    public void addChildItem(int position, String body) {
        List<String> str_body = null;
        str_body = new ArrayList<String>();
        str_body.add(body);

        listDataChild.put(listDataHeader.get(position), str_body);
    }

    @AfterViews
    protected void init() {

        // update notice adapter
        listDataHeader = new ArrayList<NoticeObj>();
        listDataChild = new HashMap<NoticeObj, List<String>>();
        lvNotice = (ExpandableListView) findViewById(R.id.lvNotice);

        LoadFromServer();


        noticeAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        lvNotice.setAdapter(noticeAdapter);
        noticeAdapter.notifyDataSetChanged();

    }

    private void LoadFromServer() {
        Log.e("test", "1");
        JSONObject jsonObject = new JSONObject();
        Log.e("jsonObject", jsonObject.toString());
        requestJson(Url_define.Notice + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("statusCode : ", statusCode + "");
                Log.d("response : ", response.toString());
                super.onSuccess(statusCode, headers, response);

                try {
                    if (response.getString("result").equals("success")) {
                        JSONArray jsonArray = new JSONArray(response.getString("notices"));
                        Log.e("jsonArray", jsonArray.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            addItem(i, jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("createdAt"));
                            addChildItem(i, jsonArray.getJSONObject(i).getString("title"));

                        }
                        noticeAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d("statusCode : ", statusCode + "");
                super.onFailure(statusCode, headers, res, t);
            }
        });
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<NoticeObj> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<NoticeObj, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<NoticeObj> listDataHeader,
                                     HashMap<NoticeObj, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_notice_list_child, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.listchild);

            txtListChild.setText(Html.fromHtml(childText).toString());
            Log.e("childText", childText);

            convertView.setPadding(20, 0, 20, 0);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        public Object getTitle(int groupPosition) {
            return this._listDataHeader.get(groupPosition).Title;
        }

        public Object getDate(int groupPosition) {
            return this._listDataHeader.get(groupPosition).Date;
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getTitle(groupPosition);
            String headerDate = (String) getDate(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_notice_list, null);
            }

            TextView lblListTitle = (TextView) convertView.findViewById(R.id.title);
            TextView lblListDate = (TextView) convertView.findViewById(R.id.date);
            ImageView arrow_down = (ImageView) convertView.findViewById(R.id.arrow_down);
            lblListTitle.setText(headerTitle);
            lblListDate.setText(headerDate);


            if (isExpanded) {
                arrow_down.setBackgroundResource(R.drawable.img_notice_open);
            } else {
                arrow_down.setBackgroundResource(R.drawable.img_notice_close);
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    @Click(R.id.img_back_notice)
    protected void onBack() {
        finish();
    }

}
