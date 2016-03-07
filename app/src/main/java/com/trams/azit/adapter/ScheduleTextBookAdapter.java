package com.trams.azit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.ScheduleTextBookModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 15/01/2016.
 */
public class ScheduleTextBookAdapter extends ArrayAdapter<ScheduleTextBookModel> {

    private static final String TAG = ScheduleTextBookAdapter.class.getName();

    private int itemSelected = -1;

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public ScheduleTextBookAdapter(Context context, ArrayList<ScheduleTextBookModel> listTextBook) {
        super(context, R.layout.item_schedule_textbook, listTextBook);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ScheduleTextBookModel item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_schedule_textbook, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_textbook_name_item);
            holder.imgPlus = (ImageView) convertView.findViewById(R.id.img_plus_textbook);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //if (item.getName().length() > 5) {
        //    holder.tvName.setText(item.getName().substring(0, 5));
        //} else {
            holder.tvName.setText(item.getName());
        holder.tvName.setHeight(30);
        //}

        if (item.isImgPlus()) {
            holder.imgPlus.setVisibility(View.VISIBLE);
        } else {
            holder.imgPlus.setVisibility(View.INVISIBLE);
        }

        if (itemSelected == position) {
            holder.tvName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_item_selected_bg));
            holder.tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            holder.tvName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_color_gray));
            holder.tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView tvName;
        ImageView imgPlus;
    }

}
