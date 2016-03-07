package com.trams.azit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.ScheduleTypeModel;

import java.util.ArrayList;

/**
 * Created by ADMIN on 1/15/2016.
 */
public class ScheduleCreateAdapter extends ArrayAdapter<ScheduleTypeModel> {

    private static final String TAG = ScheduleCreateAdapter.class.getName();

    private int positionSelected = -1;

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    public ScheduleCreateAdapter(Context context, ArrayList<ScheduleTypeModel> listType) {
        super(context, R.layout.item_schedule_create, listType);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ScheduleTypeModel item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_schedule_create, parent, false);
            holder = new ViewHolder();
            holder.layoutContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_schedule);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (positionSelected == position) {
            holder.layoutContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azit_green_bg));
        } else {
            holder.layoutContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        //set icon
        switch (item.getIcon_id()) {
            case 21:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_1);
                break;
            case 22:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_2);
                break;
            case 23:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_3);
                break;
            case 24:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_4);
                break;
            case 25:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_5);
                break;
            case 26:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_6);
                break;
            case 27:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_7);
                break;
            case 28:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_8);
                break;
            case 29:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_9);
                break;
            case 30:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_10);
                break;

            case 31:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_11);
                break;
            case 32:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_12);
                break;
            case 33:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_13);
                break;
            case 34:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_14);
                break;
            case 35:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_15);
                break;
            case 36:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_16);
                break;
            case 37:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_17);
                break;
            case 38:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_18);
                break;
            case 39:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_19);
                break;
            case 40:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_20);
                break;

            case 41:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_21);
                break;
            case 42:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_22);
                break;
            case 43:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_23);
                break;
            case 44:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_24);
                break;
            case 45:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_25);
                break;
            case 46:
                holder.imgIcon.setImageResource(R.drawable.schedule_create_26);
                break;

        }

        return convertView;
    }

    public static class ViewHolder {
        RelativeLayout layoutContent;
        ImageView imgIcon;
    }
}

