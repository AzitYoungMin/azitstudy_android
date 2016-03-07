package com.trams.azit.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.trams.azit.R;
import com.trams.azit.adapter.StudentPlusAdapter;
import com.trams.azit.model.Student;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 11/01/2016.
 */
public class PlusStudentDialog extends Dialog implements View.OnClickListener {

    public interface OnCompleteListener {
        public void onComple();
    }

    private OnCompleteListener onCompleteListener;

    public OnCompleteListener getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    private Button btnOk, btnCancel;
    private ArrayList<Student> students = new ArrayList<>();
    private StudentPlusAdapter studentPlusAdapter;
    private ListView lvStudent;
    SharedPreferences myPrefs;
    String secret, user_id;
    private int positionSelected;
    boolean isChoose = false;

    public PlusStudentDialog(Context context, ArrayList<Student> students) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.plus_student_dialog);

        this.students = students;

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        lvStudent = (ListView) findViewById(R.id.lv_plus_student);
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;
                isChoose = true;
                studentPlusAdapter.setPositonSelected(position);
                studentPlusAdapter.notifyDataSetChanged();

            }
        });

        studentPlusAdapter = new StudentPlusAdapter(getContext(), students);
        lvStudent.setAdapter(studentPlusAdapter);

        myPrefs = getContext().getSharedPreferences("Azit", Activity.MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

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
            case R.id.btn_cancel:
                hideView();
                break;
            case R.id.btn_ok:
                if (isChoose) {
                    doAddStudent();
                } else {
                    Toast.makeText(getContext(), "학생을 선택해 주세요", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void doAddStudent() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("student_id", students.get(positionSelected).getId() + "");

            Logger.e(secret);
            Logger.e(user_id);

            Log.d("jsonobject : ", jsonObject.toString() + "");
            Log.d("url : ", Url_define.Teacher_add_student + Url_define.KEY);
            NetworkHelper.requestJson(getContext(), Url_define.Teacher_add_student + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    onCompleteListener.onComple();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }
            });

//            NetworkHelper.requestJson(getContext(),Url_define.Teacher_add_student + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    Log.d("statusCode : ", statusCode + "");
//                    Log.d("response : ", response.toString());
//                    super.onSuccess(statusCode, headers, response);
//
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//                    Log.d("statusCode : ", statusCode + "");
//                    super.onFailure(statusCode, headers, res, t);
//                }
//            });

        } catch (JSONException e) {
            e.getStackTrace();
        }
    }
}
