package com.trams.azit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.model.University;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnv on 1/15/2016.
 */
public class UniversityAdapter extends ArrayAdapter<University> {
    private ArrayList<University> arrayListUniversity;
    private LayoutInflater inflater;

    public UniversityAdapter(Context context, int resource, ArrayList<University> objects) {
        super(context, resource, objects);
        this.arrayListUniversity = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_university_save, parent, false);
            holder.tvUniversity = (TextView) convertView.findViewById(R.id.tvUniversity);
            holder.tvDepartment = (TextView) convertView.findViewById(R.id.tvDepartment);
            holder.tvOptional = (TextView) convertView.findViewById(R.id.tvOptional);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        University university = arrayListUniversity.get(position);
        holder.tvUniversity.setText(university.getUniversity());
        holder.tvDepartment.setText(university.getDepartment());
        if (university.getOptional() == 0) {
            holder.tvOptional.setText("(언수외 반영)");
        } else if (university.getOptional() == 1) {
            holder.tvOptional.setText("(언수외탐1 반영)");
        } else {
            holder.tvOptional.setText("(언수외탐2 반영)");
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tvUniversity;
        public TextView tvDepartment;
        public TextView tvOptional;

    }
}
