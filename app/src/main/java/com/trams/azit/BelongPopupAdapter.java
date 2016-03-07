package com.trams.azit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 05/01/2016.
 */
public class BelongPopupAdapter extends ArrayAdapter<BelongModel>{

    private ArrayList<BelongModel> belongModels;
    private int selectedIdx = -1;

    public int getSelectedIdx() {
        return selectedIdx;
    }

    public void setSelectedIdx(int selectedIdx) {
        this.selectedIdx = selectedIdx;
    }


    public BelongPopupAdapter(Context context, ArrayList<BelongModel> belongModels) {
        super(context, R.layout.item_belong_layout, belongModels);
        this.belongModels = belongModels;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BelongModel item = getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_belong_layout, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item_belong);
            holder.imgSelected = (ImageView) convertView.findViewById(R.id.img_selected_item_belong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == selectedIdx) {
            holder.imgSelected.setVisibility(View.VISIBLE);
            holder.tvName.setTextColor(Color.parseColor("#1fb4ff"));
        } else {
            holder.imgSelected.setVisibility(View.INVISIBLE);
            holder.tvName.setTextColor(Color.parseColor("#000000"));
        }

        holder.tvName.setText(item.getName());

        return convertView;
    }

    public static class ViewHolder {
        TextView tvName;
        ImageView imgSelected;
    }


}
