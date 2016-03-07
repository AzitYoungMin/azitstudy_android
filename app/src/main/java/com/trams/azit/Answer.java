package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-09-23.
 */
public class Answer extends Activity {

    ListView answer_list;
    private ListViewAdapter answerAdapter;
    private ArrayList<AnswerListData> mArrayList = new ArrayList<AnswerListData>();
    ImageView back;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        back = (ImageView) findViewById(R.id.back_btn);
        answer_list = (ListView) findViewById(R.id.answer_list);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setListView();
    }

    private void setListView() {

        addItem("공부전략좀 알려주세요~!", "아지트", "2015-08-12 15:30:10", "1");
        addItem("어디로 가야 할까요...", "아지트", "2015-08-12 15:30:10","2");
        addItem("어디로 가야 할까요...어디로 가야 할까요...어디로 가야 할까요...", "아지트", "2015-08-12 15:30:10","3");
        answerAdapter = new ListViewAdapter(this, mArrayList);
        answer_list.setAdapter(answerAdapter);

        answer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Answer.this,mArrayList.get(position).Title,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addItem(String title, String name, String time, String comment) {
        AnswerListData addInfo = null;
        addInfo = new AnswerListData();
        addInfo.Title = title;
        addInfo.Name = name;
        addInfo.Time = time;
        addInfo.Comment = comment;
        mArrayList.add(addInfo);
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
                convertView = inflater.inflate(R.layout.answer_list_item, null);

                holder.Title = (TextView)convertView.findViewById(R.id.title);
                holder.Name = (TextView) convertView.findViewById(R.id.name);
                holder.Time = (TextView) convertView.findViewById(R.id.time);
                holder.Comment = (TextView) convertView.findViewById(R.id.comment);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final AnswerListData mData = mListData.get(position);

            holder.Title.setText(mData.Title);
            holder.Name.setText(mData.Name);
            holder.Time.setText(mData.Time);
            holder.Comment.setText(mData.Comment);

            return convertView;
        }
    }
}
