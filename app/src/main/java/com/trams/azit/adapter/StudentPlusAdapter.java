package com.trams.azit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.Student;

import java.util.List;

/**
 * Created by Administrator on 11/01/2016.
 */
public class StudentPlusAdapter extends ArrayAdapter<Student> {
    private static final String TAG = StudentPlusAdapter.class.getName();
    private Context context;
    private int positonSelected = -1;

    public int getPositonSelected() {
        return positonSelected;
    }

    public void setPositonSelected(int positonSelected) {
        this.positonSelected = positonSelected;
    }


    public StudentPlusAdapter(Context _context, List<Student> students) {
        super(_context, R.layout.item_plus_student, students);
        context = _context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Student item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_plus_student, parent, false);
            holder = new ViewHolder();
            holder.cbItemSelected = (CheckBox) convertView.findViewById(R.id.cb_item_plus_student);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item_plus_student);
            holder.tvSchool = (TextView) convertView.findViewById(R.id.tv_school_item_plus_student);
            holder.tvEmail = (TextView) convertView.findViewById(R.id.tv_email_item_plus_student);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.cbItemSelected.setSelected(item.isSelected());

        holder.tvName.setText(item.getName());
        holder.tvSchool.setText(item.getSchool());
        holder.tvEmail.setText(item.getEmail());

        if (positonSelected == position) {
            holder.cbItemSelected.setChecked(true);
        }else{
            holder.cbItemSelected.setChecked(false);
        }

     /*   holder.cbItemSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPositonSelected(position);
//                holder.cbItemSelected.setChecked(isChecked);
                notifyDataSetChanged();
            }
        });
*/
        return convertView;
    }

    public static class ViewHolder {
        CheckBox cbItemSelected;
        TextView tvName;
        TextView tvSchool;
        TextView tvEmail;
    }

}
