package com.trams.azit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.model.ExamModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 11/01/2016.
 */
public class MonthAdapter extends BaseAdapter {
    private static final String TAG = MonthAdapter.class.getName();

    private ArrayList<ExamModel> examModels;
    private Context context;

    public MonthAdapter(Context context, ArrayList<ExamModel> examModels) {
        super();
        this.context = context;
        this.examModels = examModels;
    }

    @Override
    public int getCount() {
        return examModels.size();
    }

    @Override
    public Object getItem(int position) {
        return examModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_month_spinner, parent, false);
            holder = new ViewHolder();
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tv_item_month);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMonth.setText(examModels.get(position).getTitle());

        return convertView;
    }


    public static class ViewHolder {
        TextView tvMonth;
    }
}
