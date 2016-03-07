package com.trams.azit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.adapter.ExamAdapter;
import com.trams.azit.adapter.UniversityAdapter;
import com.trams.azit.model.Exam;
import com.trams.azit.model.Item;
import com.trams.azit.model.SectionItemExam;
import com.trams.azit.model.Subject;
import com.trams.azit.model.University;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Administrator on 2015-09-07.
 */
@EActivity(R.layout.activity_student_edit)
public class Edit_Student_Profile extends ConnActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    SharedPreferences myPrefs;
    String secret, user_id, gender, imageURI;
    Bitmap photo_bit;
    private Uri mImageCaptureUri;
    Bitmap photo;
    boolean photochange = false;

    @ViewById
    ImageView main_image;
    @ViewById
    TextView man, woman, my_grade, name, email;
    @ViewById
    EditText et_nick, et_intro, et_target_major;

    private AutoCompleteTextView et_target_university;
    private Hashtable<String, String> hashTable;

    private Dialog dialog;
    private TextView tvTitleStep1, tvTitleStep2,tvTitleStep3,tvTitleMonth;
    private ScrollView includeInputGrade1;
    private LinearLayout includeInputGrade2;
    private RelativeLayout includeInputGrade3;
    private AutoCompleteTextView edUniversity;
    private int idKorean, idMath, idEnglish, idSocial, idScience, idMonth;
    private ArrayList<Exam> arrayExam;
    private ArrayList<University> arrayUniversity;
    private ArrayList<String> arrayIdExam;
    private ArrayList<Item> arrayExamSubject;
    private ExamAdapter examAdapter;
    UniversityAdapter universityAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {
        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        initData();

        et_target_university = (AutoCompleteTextView)findViewById(R.id.et_target_university);

        getMyProfile();

        String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        final String[] arrayOfSchoolId = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolId[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        et_target_university.setAdapter(adapter);



        getListExam();

    }

    private void initData(){
        arrayUniversity = new ArrayList<>();
        arrayExam = new ArrayList<>();
        arrayIdExam = new ArrayList<>();
        arrayExamSubject = new ArrayList<>();
    }
    public void showGradeInputDialog(final Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_inputgrade_lv1);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        //for header
        tvTitleStep1 = (TextView)dialog.findViewById(R.id.tvTitleStep1);
        tvTitleStep2 = (TextView)dialog.findViewById(R.id.tvTitleStep2);
        tvTitleStep3 = (TextView)dialog.findViewById(R.id.tvTitleStep3);
        tvTitleStep1.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        includeInputGrade1 = (ScrollView)dialog.findViewById(R.id.includeInputGrade1);
        includeInputGrade2 = (LinearLayout)dialog.findViewById(R.id.includeInputGrade2);
        includeInputGrade3 = (RelativeLayout)dialog.findViewById(R.id.includeInputGrade3);
        includeInputGrade1.setVisibility(View.VISIBLE);
        includeInputGrade2.setVisibility(View.GONE);
        includeInputGrade3.setVisibility(View.GONE);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btNext1:
                        saveSchoolRecord();
                        break;
                    case R.id.btNext2:
                        if (!checkInputDataExam()){
                            saveExam();
                        }else {
                            Toast.makeText(Edit_Student_Profile.this, "입력하신 정보를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.btDone:
                        if (edUniversity.getText().toString().equals("")){
                            Toast.makeText(Edit_Student_Profile.this, "목표하는 대학을 입력하세요.", Toast.LENGTH_LONG).show();
                        }else{
                            saveTargetUniversity();
                        }

                        break;
                    case R.id.imgCloseRecordStudent:
                        dialog.dismiss();
                        break;
                }

            }
        };
        //for header
        ImageView imgCloseRecordStudent = (ImageView)dialog.findViewById(R.id.imgCloseRecordStudent);
        imgCloseRecordStudent.setOnClickListener(listener);


        // start for step1 ===================
        Button btNext1 = (Button)dialog.findViewById(R.id.btNext1);
        btNext1.setOnClickListener(listener);

        final Spinner spin_korean = (Spinner)dialog.findViewById(R.id.spin_korean);
        final Spinner spin_math = (Spinner)dialog.findViewById(R.id.spin_math);
        final Spinner spin_english = (Spinner)dialog.findViewById(R.id.spin_english);
        final Spinner spin_social = (Spinner)dialog.findViewById(R.id.spin_social);
        final Spinner spin_science = (Spinner)dialog.findViewById(R.id.spin_science);
        final Spinner spMonth = (Spinner)dialog.findViewById(R.id.spMonth);
        final TextView tvTitleMonth = (TextView)dialog.findViewById(R.id.tvTitleMonth);

        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent == spin_korean){
                    idKorean = position+1;
                }
                if (parent == spin_math){
                    idMath = position+1;
                }
                if (parent == spin_english){
                    idEnglish = position+1;
                }
                if (parent == spin_social){
                    idSocial = position+1;
                }
                if (parent == spin_science){
                    idScience = position+1;
                }
                if (parent == spMonth){
                    idMonth = position;
                    Log.e("idMonth", arrayExam.get(idMonth).getTitle());
                    tvTitleMonth.setText(arrayExam.get(idMonth).getTitle());
                    arrayExamSubject.clear();
                    arrayExamSubject.addAll(arrayExam.get(idMonth).getExamSubject());
                    examAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spin_korean.setOnItemSelectedListener(selectedListener);
        spin_math.setOnItemSelectedListener(selectedListener);
        spin_english.setOnItemSelectedListener(selectedListener);
        spin_social.setOnItemSelectedListener(selectedListener);
        spin_science.setOnItemSelectedListener(selectedListener);
        // end for step1 ===================

        // start for step2 ===================
        //Spinner spMonth = (Spinner)dialog.findViewById(R.id.spMonth);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(Edit_Student_Profile.this, R.layout.spmonth_spinner_item, arrayIdExam);
        spMonth.setAdapter(adapterMonth);
        spMonth.setOnItemSelectedListener(selectedListener);

        ListView lvExam = (ListView)dialog.findViewById(R.id.lvExam);
        idMonth = arrayExam.size() -1;
        examAdapter = new ExamAdapter(Edit_Student_Profile.this, arrayExamSubject);
        lvExam.setAdapter(examAdapter);
        spMonth.setSelection(idMonth);

        Button btNext2 = (Button)dialog.findViewById(R.id.btNext2);
        btNext2.setOnClickListener(listener);
        // end for step2 ===================

        // start for step3 ===================
        Button btDone = (Button)dialog.findViewById(R.id.btDone);
        btDone.setOnClickListener(listener);

        ListView listViewUniversity = (ListView)dialog.findViewById(R.id.lvUniversity);
        universityAdapter = new UniversityAdapter(Edit_Student_Profile.this, R.layout.item_university_save, arrayUniversity);
        listViewUniversity.setAdapter(universityAdapter);

        edUniversity = (AutoCompleteTextView)dialog.findViewById(R.id.edTargetUniversity);

        String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        final String[] arrayOfSchoolId = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolId[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        edUniversity.setAdapter(adapter);
        // end for step3 ===================

        if (dialog != null) {
            dialog.show();
        }

        dialog.getWindow().setAttributes(lp);
    }

    private void saveSchoolRecord() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            jsonObject.put("korean", idKorean);
            jsonObject.put("math", idMath);
            jsonObject.put("english", idEnglish);
            jsonObject.put("social", idSocial);
            jsonObject.put("science", idScience);

            requestJson(Url_define.STUDENT_SCHOOL_SAVE_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.get("result").toString().equals("success")) {
                            //next step
                            includeInputGrade1.setVisibility(View.GONE);
                            includeInputGrade2.setVisibility(View.VISIBLE);
                            tvTitleStep1.setTextColor(getResources().getColor(android.R.color.black));
                            tvTitleStep2.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        }
                    } catch (JSONException e) {
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

    private void getListUniversityRecommend() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            requestJson(Url_define.UNIVERSITY_RECOMMEND_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.get("result").toString().equals("success")) {

                            JSONArray universityJsonArray = new JSONArray(response.getString("university_list"));

                            int numberUniversity = universityJsonArray.length();
                            for (int i =0; i< numberUniversity; i++){
                                JSONObject jsonUniver = (JSONObject)universityJsonArray.get(i);
                                University object = new University(jsonUniver.getString("university"),jsonUniver.getString("department"), jsonUniver.getInt("optional") );
                                arrayUniversity.add(object);
                            }

                            universityAdapter.notifyDataSetChanged();

                            Log.d("onSuccess", "arrayUniversity: " + arrayUniversity.size());
                        }
                    } catch (JSONException e) {
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

    private void getListExam() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            requestJson(Url_define.LIST_EXAM_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    parserListExam(response);
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

    private void parserListExam(JSONObject response){
        try {
            if (response.get("result").toString().equals("success")) {
                JSONArray jsonArrayExam = new JSONArray(response.getString("exam_list"));

                int numberExam = jsonArrayExam.length();
                for (int i =0; i< numberExam; i++){
                    JSONObject jsonExam = (JSONObject)jsonArrayExam.get(i);
                    Exam exam = new Exam();
                    exam.setId(jsonExam.getString("id"));
                    exam.setTitle(jsonExam.getString("title"));
                    ArrayList<Item> arrayExamSubject = new ArrayList<>();

                    //get array score
                    JSONArray arrayScore = new JSONArray(jsonExam.getString("exam_subject_score"));
                    ArrayList<Subject> arrayListScore = new ArrayList<>();
                    arrayExamSubject.add(new SectionItemExam(jsonExam.getString("title") + " 점수"));
                    for (int j =0; j< arrayScore.length(); j++){
                        JSONObject jsonSubject = (JSONObject)arrayScore.get(j);
                        Subject subject = new Subject(jsonSubject.getString("id"), jsonSubject.getString("title"), 1,"점", 1);
                        arrayListScore.add(subject);
                        arrayExamSubject.add(subject);
                    }
                    exam.setExam_subject_score(arrayListScore);

                    //get array percentile
                    JSONArray arrayPercentile = new JSONArray(jsonExam.getString("exam_subject_percentile"));
                    ArrayList<Subject> arrayListPercentile = new ArrayList<>();
                    arrayExamSubject.add(new SectionItemExam(jsonExam.getString("title") + " 백분위"));
                    for (int j =0; j< arrayPercentile.length(); j++){
                        JSONObject jsonSubject = (JSONObject)arrayPercentile.get(j);
                        Subject subject = new Subject(jsonSubject.getString("id"), jsonSubject.getString("title"),2,"%", 1);
                        arrayListPercentile.add(subject);
                        arrayExamSubject.add(subject);
                    }
                    exam.setExam_subject_score(arrayListPercentile);

                    //get array standard
                    JSONArray arrayStandard = new JSONArray(jsonExam.getString("exam_subject_standard"));
                    ArrayList<Subject> arrayListStandard = new ArrayList<>();
                    arrayExamSubject.add(new SectionItemExam(jsonExam.getString("title") + " 표준점수"));
                    for (int j =0; j< arrayStandard.length(); j++){
                        JSONObject jsonSubject = (JSONObject)arrayStandard.get(j);
                        Subject subject = new Subject(jsonSubject.getString("id"), jsonSubject.getString("title"),3,"점", 2);
                        arrayListStandard.add(subject);
                        arrayExamSubject.add(subject);
                    }
                    exam.setExam_subject_score(arrayListStandard);
                    exam.setExamSubject(arrayExamSubject);

                    arrayExam.add(exam);
                }

                getListExamId();
                Log.d("onSuccess", "arrayExam: " + arrayExam.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getListExamId(){
        if (arrayExam!=null && arrayExam.size()>0){
            for (int i=0; i< arrayExam.size(); i++){
                //arrayIdExam.add(arrayExam.get(i).getId());
                Log.e("arrayExam.get(i)", arrayExam.get(i).getTitle());
                arrayIdExam.add(arrayExam.get(i).getTitle());
            }
        }
    }

    private JSONObject createJSONObjectSaveExam(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("exam_id", Integer.valueOf(arrayExam.get(idMonth).getId()));

            JSONArray jsonArrayScore = new JSONArray();
            JSONArray jsonArrayPercentile = new JSONArray();
            JSONArray jsonArrayStandard = new JSONArray();
            for (int i=0; i< arrayExamSubject.size(); i++){
                if (!arrayExamSubject.get(i).isSection()){
                    Subject subject = (Subject)arrayExamSubject.get(i);
                    if (subject.getClassificationSubject()==1){
                        JSONObject object = new JSONObject();
                        object.put("id" , Integer.valueOf(subject.getId()));
                        object.put("score" , Integer.valueOf(subject.getData()));
                        jsonArrayScore.put(object);
                    }

                    if (subject.getClassificationSubject()==2){
                        JSONObject object = new JSONObject();
                        object.put("id" , Integer.valueOf(subject.getId()));
                        object.put("score" , Integer.valueOf(subject.getData()));
                        jsonArrayPercentile.put(object);
                    }

                    if (subject.getClassificationSubject()==3){
                        JSONObject object = new JSONObject();
                        object.put("id" , Integer.valueOf(subject.getId()));
                        object.put("score" , Integer.valueOf(subject.getData()));
                        jsonArrayStandard.put(object);
                    }
                }

            }

            jsonObject.put("score",jsonArrayScore);
            jsonObject.put("percentile",jsonArrayPercentile);
            jsonObject.put("standard", jsonArrayStandard);


        }catch (JSONException e) {
            e.getStackTrace();
        }

        return jsonObject;

    }

    private void saveExam() {
        JSONObject jsonObject = createJSONObjectSaveExam();

        requestJson(Url_define.SAVE_EXAM_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                includeInputGrade1.setVisibility(View.GONE);
                includeInputGrade2.setVisibility(View.GONE);
                includeInputGrade3.setVisibility(View.VISIBLE);

                tvTitleStep1.setTextColor(getResources().getColor(android.R.color.black));
                tvTitleStep2.setTextColor(getResources().getColor(android.R.color.black));
                tvTitleStep3.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                getListUniversityRecommend();
                Log.d("onSuccess", "saveExam successfully: ");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
            }
        });

    }

    private void saveTargetUniversity() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("target_university", edUniversity.getText());


            requestJson(Url_define.SAVE_UNIVERSITY_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (dialog != null && dialog.isShowing()) {
                        Toast.makeText(Edit_Student_Profile.this, "정보가 입력되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getMyProfile();
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

    private boolean checkInputDataExam() {
        for (int i = 0; i < arrayExamSubject.size(); i++) {
            Item item = arrayExamSubject.get(i);
            if (!item.isSection()) {
                Subject subject = (Subject) item;
                if (subject.getData().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }


    private void getMyProfile() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.Student_get_profile + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.getString("profile_image").equals("")) {
                            main_image.setBackgroundResource(R.drawable.profile_basic);
                            imageURI = "";
                        } else {
                            Picasso.with(Edit_Student_Profile.this).load(Url_define.BASE_Image + response.getString("profile_image")).fit().into(main_image);
                            imageURI = response.getString("profile_image");
                        }
                        et_nick.setText(response.getString("nickname"));
                        name.setText(response.getString("name"));
                        email.setText(response.getString("email"));
                        et_intro.setText(response.getString("introduce"));
                        et_target_university.setText(response.getString("target_university"));
                        et_target_major.setText(response.getString("target_department"));
                        if (response.getString("year").equals("")) {
                            my_grade.setText("재도전");
                        } else {
                            my_grade.setText("고" + response.getString("year").toString());
                        }
                        if (response.getString("gender").equals("F")) {
                            woman.setBackgroundResource(R.drawable.woman_push);
                            gender = "F";
                        } else {
                            man.setBackgroundResource(R.drawable.man_push);
                            gender = "M";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    /*@Click(R.id.confirm_nick)
    protected void setConfirm_nick() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickname", et_nick.getText().toString());

            requestJson(Url_define.BASE + "/api/nickname/check" + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))){
                            Toast.makeText(Edit_Student_Profile.this, "해당 닉네임이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Edit_Student_Profile.this, "해당 닉네임은  사용 가능 합니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }*/



    @Click(R.id.photo)
    protected void setPhoto() {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction();
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        Log.e("1","1");
        new AlertDialog.Builder(this)

                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
        Log.e("2", "2");
    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

    @Click(R.id.input_grade)
    protected void setInput_grade() {
        showGradeInputDialog(Edit_Student_Profile.this);
    }

    @Click(R.id.my_grade_ll)
    protected void setMy_grade() {
        final CharSequence[] items = {"고1", "고2", "고3", "재도전"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("학년 선택");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                my_grade.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Click(R.id.complete)
    protected void setComplete_btn() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("nickname", et_nick.getText().toString());
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("gender", gender);
            jsonObject.put("profile_image", imageURI);
            jsonObject.put("target_university", et_target_university.getText().toString());
            jsonObject.put("target_department", et_target_major.getText().toString());
            jsonObject.put("introduce", et_intro.getText().toString());
            String year = "";
            if (my_grade.getText().toString().equals("고1")) {
                year = "1";
            } else if (my_grade.getText().toString().equals("고2")) {
                year = "2";
            } else if (my_grade.getText().toString().equals("고3")) {
                year = "3";
            } else if (my_grade.getText().toString().equals("재도전")) {
                year = "0";
            }
            jsonObject.put("year", year);

            RequestParams params = new RequestParams();
            if (photochange) {

                ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                BitmapDrawable d1 = (BitmapDrawable) ((ImageView) findViewById(R.id.main_image)).getDrawable();
                Bitmap b1 = d1.getBitmap();
                b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                params.put("profile_image", new ByteArrayInputStream(bos1.toByteArray()), "profile", "image/jpeg");

            }

            requestMultipart(Url_define.Student_Edit_profile + Url_define.KEY, jsonObject, params, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);


                    Intent i = new Intent(Edit_Student_Profile.this, StudentMainActivity.class);
                    i.putExtra("position", 0);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    @Click(R.id.man)
    protected void setMan() {
        man.setBackgroundResource(R.drawable.man_push);
        woman.setBackgroundResource(R.drawable.woman);
        gender = "M";
    }

    @Click(R.id.woman)
    protected void setWoman() {
        man.setBackgroundResource(R.drawable.man);
        woman.setBackgroundResource(R.drawable.woman_push);
        gender = "F";
    }

    private void doTakePhotoAction() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/azit/", url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.e("data", data.toString());

                try {
                    if (photo_bit != null) {
                        photo_bit.recycle();
                    }

                    String filePath = getPath(mImageCaptureUri);

                    InputStream stream = getContentResolver().openInputStream(mImageCaptureUri);
                    photo_bit = BitmapFactory.decodeStream(stream);
                    photo_bit = Bitmap.createScaledBitmap(photo_bit, 100, 100, false);

                    Picasso.with(Edit_Student_Profile.this).load(mImageCaptureUri).rotate(getExifOrientation(filePath)).fit().into(main_image);
                    photochange = true;


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case PICK_FROM_CAMERA: {
                try {

                    if (photo_bit != null) {
                        photo_bit.recycle();
                    }

                    String temp = mImageCaptureUri.toString();
                    temp.replace("file://", "");
                    Uri final_uri = Uri.parse(temp);
                    Log.e("final_uri", final_uri.toString());

                    InputStream stream = getContentResolver().openInputStream(mImageCaptureUri);
                    photo_bit = BitmapFactory.decodeStream(stream);
                    photo_bit = Bitmap.createScaledBitmap(photo_bit, 100, 100, false);

                    Picasso.with(Edit_Student_Profile.this).load(mImageCaptureUri).into(main_image);
                    photochange = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            Log.e("orientation", orientation + "");
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

}
