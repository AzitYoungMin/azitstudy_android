package com.trams.azit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.ScheduleTypeModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 15/01/2016.
 */
public class ScheduleTypeAdapter extends ArrayAdapter<ScheduleTypeModel> {


    public interface ScheduleDeleteListener {
        public void onDelete(int position);
    }

    private ScheduleDeleteListener scheduleDeleteListener;

    public ScheduleDeleteListener getScheduleDeleteListener() {
        return scheduleDeleteListener;
    }

    public void setScheduleDeleteListener(ScheduleDeleteListener scheduleDeleteListener) {
        this.scheduleDeleteListener = scheduleDeleteListener;
    }

    private static final String TAG = ScheduleSubjectAdapter.class.getName();
    private int itemSelected = 0;

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public ScheduleTypeAdapter(Context context, ArrayList<ScheduleTypeModel> listType) {
        super(context, R.layout.item_schedule_type, listType);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ScheduleTypeModel item = getItem(position);

//        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_schedule_type, parent, false);

            holder = new ViewHolder();
            holder.layoutRoot = (RelativeLayout) convertView.findViewById(R.id.layout_item_schedule_type);
            holder.layoutContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
            holder.layoutIcon = (RelativeLayout) convertView.findViewById(R.id.layout_icon_item);

            holder.imgTypeIcon = (ImageView) convertView.findViewById(R.id.img_type_icon);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);

            holder.imgAdd = (ImageView) convertView.findViewById(R.id.img_plus);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.img_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set name
        holder.tvTypeName.setText(item.getName());

        if (item.getActivityType() == 0) {
            holder.imgTypeIcon.setImageResource(0);
//            holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//            holder.tvTypeName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getActivityType() == 1 || item.getActivityType() == 2) {
            //set icon
            switch (item.getId()) {
                case 1:
                    holder.imgTypeIcon.setImageResource(R.drawable.study_icon);
                    break;
                case 2:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_type_2);
                    break;
                case 3:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_type_3);
                    break;
                case 4:
                    holder.imgTypeIcon.setImageResource(R.drawable.class_icon);
                    break;
                case 5:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_type_5);
                    break;
                case 6:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_type_6);
                    break;
                case 7:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_type_7);
                    break;
                case 8:
                    holder.imgTypeIcon.setImageResource(R.drawable.reading_icon);
                    break;
                case 9:
                    holder.imgTypeIcon.setImageResource(R.drawable.sleep_icon);
                    break;
                case 10:
                    holder.imgTypeIcon.setImageResource(R.drawable.leisure_icon);
                    break;
                case 11:
                    holder.imgTypeIcon.setImageResource(R.drawable.rest_icon);
                    break;
                case 12:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_type_12);
                    break;
                case 13:
                    holder.imgTypeIcon.setImageResource(R.drawable.exercise_icon);
                    break;
            }
        } else if (item.getActivityType() == 3) {
            //set icon
            switch (item.getIcon_id()) {

                case 21:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_1);
                    break;
                case 22:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_2);
                    break;
                case 23:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_3);
                    break;
                case 24:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_4);
                    break;
                case 25:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_5);
                    break;
                case 26:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_6);
                    break;
                case 27:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_7);
                    break;
                case 28:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_8);
                    break;
                case 29:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_9);
                    break;
                case 30:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_10);
                    break;

                case 31:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_11);
                    break;
                case 32:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_12);
                    break;
                case 33:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_13);
                    break;
                case 34:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_14);
                    break;
                case 35:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_15);
                    break;
                case 36:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_16);
                    break;
                case 37:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_17);
                    break;
                case 38:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_18);
                    break;
                case 39:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_19);
                    break;
                case 40:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_20);
                    break;

                case 41:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_21);
                    break;
                case 42:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_22);
                    break;
                case 43:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_23);
                    break;
                case 44:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_24);
                    break;
                case 45:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_25);
                    break;
                case 46:
                    holder.imgTypeIcon.setImageResource(R.drawable.schedule_create_26);
                    break;
            }
        }

        if (item.isAddIcon()) {
            holder.imgAdd.setVisibility(View.VISIBLE);
        } else {
            holder.imgAdd.setVisibility(View.INVISIBLE);
        }

        if (item.isIdDelete()) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgDelete.setVisibility(View.INVISIBLE);
        }

//        if (item.isAddIcon()) {
//            holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//        } else {
//            holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_color));
//        }

        if (position == itemSelected) {
            holder.layoutIcon.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_item_selected_bg));
            holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_item_selected_bg));
            holder.tvTypeName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            holder.layoutIcon.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            holder.tvTypeName.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

//            holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_color_gray));

            if (item.isAddIcon()) {
                holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            } else {
                holder.tvTypeName.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_color));
            }

        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(TAG, "holder imgDelete delete start , position : " + position);
                if (scheduleDeleteListener != null) scheduleDeleteListener.onDelete(position);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        RelativeLayout layoutRoot;
        RelativeLayout layoutContent;
        RelativeLayout layoutIcon;
        ImageView imgTypeIcon;
        TextView tvTypeName;

        ImageView imgDelete;
        ImageView imgAdd;
    }
}
