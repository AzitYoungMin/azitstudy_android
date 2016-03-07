package com.trams.azit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by Administrator on 07/01/2016.
 */
public class TeacherSettingEducationDialog extends Dialog implements View.OnClickListener {

    private ImageView btnClose;
    private String graduate;

    private ImageView imgGoingOn, imgGraduate;
    private RelativeLayout layoutGoingOn, layoutGraduate;

    private TextView tvGoinOn, tvGraduate;

    private Button btnSubmit;

    private AutoCompleteTextView belong;
    private Hashtable<String, String> hashTable;
    private boolean isGraduate;

    public interface EducationSettingListener {
        public void onSuccess(String graduateResponse,String lastSchool);
    }

    private EducationSettingListener educationSettingListener;

    public EducationSettingListener getEducationSettingListener() {
        return educationSettingListener;
    }

    public void setEducationSettingListener(EducationSettingListener educationSettingListener) {
        this.educationSettingListener = educationSettingListener;
    }

    public TeacherSettingEducationDialog(Context context, String _graduate, String lastSchool) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_setting_education_dialog);

        this.graduate = _graduate;

        btnClose = (ImageView) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        imgGoingOn = (ImageView) findViewById(R.id.img_going_on_select);

        imgGraduate = (ImageView) findViewById(R.id.img_graduate_select);

        layoutGoingOn = (RelativeLayout) findViewById(R.id.layout_teacher_going_on);
        layoutGoingOn.setOnClickListener(this);

        layoutGraduate = (RelativeLayout) findViewById(R.id.layout_teacher_graduate);
        layoutGraduate.setOnClickListener(this);

        tvGoinOn = (TextView) findViewById(R.id.tv_going_on);

        tvGraduate = (TextView) findViewById(R.id.tv_graduate);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        belong = (AutoCompleteTextView) findViewById(R.id.belong);
        belong.setText(lastSchool);

        String[] arrayOfSchool = getContext().getResources().getStringArray(R.array.school_name);
        String[] arrayOfSchoolid = getContext().getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolid[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        belong.setAdapter(adapter);

        belong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        setEducation(_graduate);

    }

    /**
     * Show view
     */
    public void showView() {
        if (!this.isShowing()) {
            this.show();
        }
    }

    /**
     * hide view
     */
    public void hideView() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:
                onSubmit();
                break;

            case R.id.btn_close:
                hideView();
                break;

            case R.id.layout_teacher_going_on:
                setEducation("재학");
                isGraduate = false;
                break;

            case R.id.layout_teacher_graduate:
                isGraduate = true;
                setEducation("졸업");
                break;
        }

    }


    private void onSubmit() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(getContext()));
            jsonObject.put("user_id", PreferUtils.getUserId(getContext()));
            jsonObject.put("last_school", belong.getText().toString());
            jsonObject.put("is_graduated", isGraduate);

            NetworkHelper.requestJson(getContext(), Url_define.UPDATE_SCHOOL_TEACHER + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (educationSettingListener != null) {
                            educationSettingListener.onSuccess(graduate,belong.getText().toString());
                        }
                        hideView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }



    }

    private void setEducation(String graduate) {

        this.graduate = graduate;

        if (graduate.equals("재학")) {
            imgGoingOn.setVisibility(View.VISIBLE);
            imgGraduate.setVisibility(View.INVISIBLE);

            tvGoinOn.setTextColor(ContextCompat.getColor(getContext(), R.color.azit_green_bg));
            tvGraduate.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        } else {
            imgGoingOn.setVisibility(View.INVISIBLE);
            imgGraduate.setVisibility(View.VISIBLE);

            tvGoinOn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            tvGraduate.setTextColor(ContextCompat.getColor(getContext(), R.color.azit_green_bg));
        }
    }

}
