package com.trams.azit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.ScheduleSubjectModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 15/01/2016.
 */
public class ScheduleSubjectAdapter extends ArrayAdapter<ScheduleSubjectModel> {

    private static final String TAG = ScheduleSubjectAdapter.class.getName();
    private int itemSelected = 0;

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }


    public ScheduleSubjectAdapter(Context context, ArrayList<ScheduleSubjectModel> listSubject) {
        super(context, R.layout.item_schedule_subject, listSubject);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ScheduleSubjectModel item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_schedule_subject, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (itemSelected == position) {
            holder.tvName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_item_selected_bg));
            holder.tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        } else {
            holder.tvName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_color_gray));
            holder.tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        holder.tvName.setText(item.getName());

        return convertView;
    }

    public static class ViewHolder {
        TextView tvName;
    }
}