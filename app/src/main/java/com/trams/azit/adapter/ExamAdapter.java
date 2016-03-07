package com.trams.azit.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.model.Exam;
import com.trams.azit.model.Item;
import com.trams.azit.model.SectionItemExam;
import com.trams.azit.model.Subject;
import com.trams.azit.model.SubjectItemExam;
import com.trams.azit.util.InputFilterMinMax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnv on 1/15/2016.
 */
public class ExamAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> arrayListExam;
    private LayoutInflater inflater;
    private Context contextExamAdapter;

    public ExamAdapter(Context context, ArrayList<Item> objects) {
        super(context, 0, objects);

        this.arrayListExam = objects;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contextExamAdapter = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewItem = convertView;

        final Item item = arrayListExam.get(position);
        if (item != null) {
            if(item.isSection()){
                SectionItemExam si = (SectionItemExam)item;
                viewItem = inflater.inflate(R.layout.exam_item_section, null);

                viewItem.setOnClickListener(null);
                viewItem.setOnLongClickListener(null);
                viewItem.setLongClickable(false);

                final TextView sectionView = (TextView) viewItem.findViewById(R.id.tvExamItemSection);
                sectionView.setText(si.getTitle());

            }else{
                final Subject subject = (Subject)item;
                viewItem = inflater.inflate(R.layout.exam_item_entry, null);
                final TextView tvTitleExam = (TextView)viewItem.findViewById(R.id.tvTitleExam);
                final TextView tvUnitExam = (TextView)viewItem.findViewById(R.id.tvUnitExam);
                final EditText edDataExam = (EditText)viewItem.findViewById(R.id.edDataExam);
                if (subject.getClassUnit() ==1){
                    edDataExam.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
                }else {
                    edDataExam.setFilters(new InputFilter[]{new InputFilterMinMax("0", "200")});
                }
                tvTitleExam.setText(subject.getTitle());
                tvUnitExam.setText(subject.getUnit());

                //Fill EditText with the value you have in data source
                edDataExam.setText(subject.getData());
                edDataExam.setId(position);

                //we need to update adapter once we finish with editing
                edDataExam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int position = v.getId();
                            final EditText Caption = (EditText) v;
                            subject.setData(Caption.getText().toString());
                        }
                    }
                });

            }
        }

        return viewItem;
    }

}
