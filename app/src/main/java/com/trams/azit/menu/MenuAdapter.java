package com.trams.azit.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.StudentMainActivity;

/**
 * Created by Administrator on 2015-09-22.
 */
public class MenuAdapter extends BaseAdapter {
    String [] result;
    String[] tags;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;

    public MenuAdapter(Context mContext, String[] tag, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mContext;
        tags = tag;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        LinearLayout row;
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.menu_item_row, null);
        holder.row = (LinearLayout)rowView.findViewById(R.id.menu_row);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.row.setTag(tags[position]);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);



        return rowView;
    }


}
