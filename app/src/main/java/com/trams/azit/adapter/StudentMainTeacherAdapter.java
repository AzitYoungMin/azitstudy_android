package com.trams.azit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.model.Student;
import com.trams.azit.util.Url_define;

import java.util.List;

/**
 * Created by Administrator on 11/01/2016.
 */
public class StudentMainTeacherAdapter extends ArrayAdapter<Student> {

    private static final String TAG = StudentMainTeacherAdapter.class.getName();
    private Context context;
    private int examId;

    public StudentMainTeacherAdapter(Context _context, List<Student> students) {
        super(_context, R.layout.item_student_main_teacher, students);
        context = _context;
        this.examId = examId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Student item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_student_main_teacher, parent, false);
            holder = new ViewHolder();
            holder.cbItemSelected = (CheckBox) convertView.findViewById(R.id.cb_item_student);
            holder.imgPresent = (ImageView) convertView.findViewById(R.id.img_present_item_student);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item_student);
            holder.tvTestResult = (TextView) convertView.findViewById(R.id.tv_test_result_item_student);
            holder.imgDetail = (ImageView) convertView.findViewById(R.id.img_present_item_student);


            holder.mon = (TextView) convertView.findViewById(R.id.mon_image);
            holder.tue = (TextView) convertView.findViewById(R.id.tue_image);
            holder.wed = (TextView) convertView.findViewById(R.id.wed_image);
            holder.thu = (TextView) convertView.findViewById(R.id.thu_image);
            holder.fri = (TextView) convertView.findViewById(R.id.fri_image);
            holder.sat = (TextView) convertView.findViewById(R.id.sat_image);
            holder.sun = (TextView) convertView.findViewById(R.id.sun_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getName());

        if (item.getPhoto().equals("")){
            holder.imgPresent.setBackgroundResource(R.drawable.profile_basic_icon);
        }else{
            Picasso.with(context).load(Url_define.BASE_Image + item.getPhoto()).into(holder.imgPresent);
        }


        holder.tvTestResult.setSingleLine(false);
        holder.tvTestResult.setText(item.getTestDescription());

        if (item.getMockTest().equals("1")) {
            holder.tvTestResult.setBackgroundResource(R.color.student_mock_test);
            holder.tvTestResult.setText("입력완료");
        } else if (item.getMockTest().equals("2")) {
            holder.tvTestResult.setBackgroundResource(R.color.student_mock_not_test);
            holder.tvTestResult.setText("미입력");
        }

        holder.cbItemSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.d(TAG," onCheckedChanged , isChecked : " + isChecked);
                item.setIsSelected(isChecked);
            }
        });
        holder.cbItemSelected.setChecked(item.isSelected());

        if (item.getMon() == 1) {
            holder.mon.setBackgroundResource(R.drawable.circle_gray);
            holder.mon.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getMon() == 2) {
            holder.mon.setBackgroundResource(R.drawable.circle_yellow);
            holder.mon.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getMon() == 0) {
            holder.mon.setBackgroundResource(0);
            holder.mon.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (item.getTue() == 1) {
            holder.tue.setBackgroundResource(R.drawable.circle_gray);
            holder.tue.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getTue() == 2) {
            holder.tue.setBackgroundResource(R.drawable.circle_yellow);
            holder.tue.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getTue() == 0) {
            holder.tue.setBackgroundResource(0);
            holder.tue.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (item.getWed() == 1) {
            holder.wed.setBackgroundResource(R.drawable.circle_gray);
            holder.wed.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getWed() == 2) {
            holder.wed.setBackgroundResource(R.drawable.circle_yellow);
            holder.wed.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getWed() == 0) {
            holder.wed.setBackgroundResource(0);
            holder.wed.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (item.getThu() == 1) {
            holder.thu.setBackgroundResource(R.drawable.circle_gray);
            holder.thu.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getThu() == 2) {
            holder.thu.setBackgroundResource(R.drawable.circle_yellow);
            holder.thu.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getThu() == 0) {
            holder.thu.setBackgroundResource(0);
            holder.thu.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (item.getFri() == 1) {
            holder.fri.setBackgroundResource(R.drawable.circle_gray);
            holder.fri.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getFri() == 2) {
            holder.fri.setBackgroundResource(R.drawable.circle_yellow);
            holder.fri.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getFri() == 0) {
            holder.fri.setBackgroundResource(0);
            holder.fri.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (item.getSat() == 1) {
            holder.sat.setBackgroundResource(R.drawable.circle_gray);
            holder.sat.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getSat() == 2) {
            holder.sat.setBackgroundResource(R.drawable.circle_yellow);
            holder.sat.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        } else if (item.getSat() == 0) {
            holder.sat.setBackgroundResource(0);
            holder.sat.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (item.getSun() == 1) {
            holder.sun.setBackgroundResource(R.drawable.circle_gray);
            holder.sun.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getSun() == 2) {
            holder.sun.setBackgroundResource(R.drawable.circle_yellow);
            holder.sun.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (item.getSun() == 0) {
            holder.sun.setBackgroundResource(0);
            holder.sun.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "StudentMainTeacherAdapter , view setOnClickListener");
//                Intent i = new Intent(getContext(), Record_Student_Study.class);
//                i.putExtra("id", item.getId());
//                i.putExtra("image", item.getPhoto());
//                i.putExtra("name", item.getName());
//                i.putExtra("mon", item.getMon());
//                i.putExtra("tue", item.getTue());
//                i.putExtra("wed", item.getWed());
//                i.putExtra("thu", item.getThu());
//                i.putExtra("fri", item.getFri());
//                i.putExtra("sat", item.getSat());
//                i.putExtra("sun", item.getSun());
//                i.putExtra("mock", "1");
//                getContext().startActivity(i);
//            }
//        });

        return convertView;
    }

    public static class ViewHolder {
        CheckBox cbItemSelected;
        ImageView imgPresent;
        TextView tvName;
        TextView tvTestResult;
        ImageView imgDetail;
        public TextView mon, tue, wed, thu, fri, sat, sun;

    }

}
