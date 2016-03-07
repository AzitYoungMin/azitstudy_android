package com.trams.azit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.ScheduleTextBookModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 15/01/2016.
 */
public class TextbookSearchAdapter extends ArrayAdapter<ScheduleTextBookModel> {

    private static final String TAG = TextbookSearchAdapter.class.getName();

    private int itemSelected = -1;

    public int getItemSelected() {
        return itemSelected;
    }
    public int selectedItemId = 0;

    public boolean selectedItemIsCustom = false;

    public boolean getSelectedItemIsCustom() {
        return selectedItemIsCustom;
    }

    public void setSelectedItemIsCustom(boolean selectedItemIsCustom) {
        this.selectedItemIsCustom = selectedItemIsCustom;
    }
    public int getSelectedItemId(){
        return selectedItemId;
    }

    public void setSelectedItemdId(int itemId){
        this.selectedItemId = itemId;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public TextbookSearchAdapter(Context context, ArrayList<ScheduleTextBookModel> listTextBook) {
        super(context, R.layout.item_textbook_search, listTextBook);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ScheduleTextBookModel item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_textbook_search, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_textbook_search_item);
            holder.imgSelected = (ImageView) convertView.findViewById(R.id.img_selected_textbook_search_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getName());


        if (itemSelected == position) {
            holder.imgSelected.setVisibility(View.VISIBLE);
        } else {
            holder.imgSelected.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView tvName;
        ImageView imgSelected;
    }

}

