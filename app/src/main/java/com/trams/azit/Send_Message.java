package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-09-23.
 */
public class Send_Message extends Activity {

    ImageView back, confirm;
    TextView checkall, dismissall;
    ListView message_list;
    private ListViewAdapter messageAdapter;
    private ArrayList<MessageListData> mArrayList = new ArrayList<MessageListData>();
    Boolean Allcheck = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        back = (ImageView) findViewById(R.id.back_btn);
        message_list = (ListView) findViewById(R.id.message_list);
        checkall = (TextView) findViewById(R.id.checkAll);
        dismissall = (TextView) findViewById(R.id.dismissAll);
        confirm = (ImageView) findViewById(R.id.confirm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Allcheck = true;
                messageAdapter.setAllChecked();
                messageAdapter.notifyDataSetChanged();
            }
        });
        dismissall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Allcheck = false;
                messageAdapter.setDismiss();
                messageAdapter.notifyDataSetChanged();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Send_Message.this, Send_Message1.class);
                startActivity(i);
            }
        });

        setListView();

    }

    private void setListView() {

        addItem(R.drawable.profile_basic_icon, "아지트", "사회문화");
        addItem(R.drawable.profile_basic_icon, "전민재", "수리");
        addItem(R.drawable.profile_basic_icon, "김민삼", "외국어");
        addItem(R.drawable.profile_basic_icon, "강세미", "언어/수리");
        messageAdapter = new ListViewAdapter(this, mArrayList);
        message_list.setAdapter(messageAdapter);
    }

    public void addItem(int photo, String name, String major) {
        MessageListData addInfo = null;
        addInfo = new MessageListData();
        addInfo.photo = photo;
        addInfo.name = name;
        addInfo.major = major;
        mArrayList.add(addInfo);
    }

    private class ViewHolder {
        public RelativeLayout rows;
        public CheckBox Checkbox;
        public ImageView image;
        public TextView Name, major;
    }

    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<MessageListData> mListData;

        public ListViewAdapter(Context mContext, ArrayList<MessageListData> mList) {
            super();
            this.mContext = mContext;
            this.mListData = mList;
        }

        public void setAllChecked() {
            int tempSize = mListData.size();
            for (int i = 0; i < tempSize; i++) {
                mListData.get(i).setChecked(true);
            }
        }

        public void setDismiss() {
            int tempSize = mListData.size();
            for (int i = 0; i < tempSize; i++) {
                mListData.get(i).setChecked(false);
            }
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
                convertView = inflater.inflate(R.layout.message_list_item, null);

                holder.rows = (RelativeLayout) convertView.findViewById(R.id.row);
                holder.Checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.image = (ImageView) convertView.findViewById(R.id.picture);
                holder.Name = (TextView) convertView.findViewById(R.id.name);
                holder.major = (TextView) convertView.findViewById(R.id.major);

                convertView.setTag(holder);
                convertView.setTag(R.id.cb_student, holder.Checkbox);
                holder.Checkbox.setTag(position);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.Checkbox.setOnCheckedChangeListener(null);
            }

            final MessageListData mData = mListData.get(position);

            holder.Checkbox.setChecked(mData.isChecked());
            if (mData.isChecked()) {
                holder.rows.setBackgroundColor(Color.parseColor("#f4f4f4"));
            } else {
                holder.rows.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            holder.Checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!mData.isChecked()) {
                        holder.rows.setBackgroundColor(Color.parseColor("#f4f4f4"));
                    } else {
                        holder.rows.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    mData.setChecked(buttonView.isChecked());
                }
            });

            holder.image.setBackgroundResource(mData.photo);
            holder.Name.setText(mData.name);
            holder.major.setText(mData.major);

            return convertView;
        }
    }

}
