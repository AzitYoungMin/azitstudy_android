package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.trams.azit.activity.CreateScheduleActivity;
import com.trams.azit.adapter.ScheduleSubjectAdapter;
import com.trams.azit.adapter.ScheduleTextBookAdapter;
import com.trams.azit.adapter.ScheduleTypeAdapter;
import com.trams.azit.dialog.TextbookPopupDialog;
import com.trams.azit.model.ScheduleSubjectModel;
import com.trams.azit.model.ScheduleTextBookModel;
import com.trams.azit.model.ScheduleTypeModel;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.MyProgressDialog;
import com.trams.azit.util.Url_define;
import com.trams.azit.util.Utils;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.Click;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-10-02.
 */
public class Plus_Act extends ConnActivity implements View.OnClickListener {


    private GridView grScheduleType;
    private ArrayList<ScheduleTypeModel> listType = new ArrayList<>();
    private ScheduleTypeAdapter scheduleTypeAdapter;

    private GridView grScheduleSubject;
    private ArrayList<ScheduleSubjectModel> listSubject = new ArrayList<>();
    private ScheduleSubjectAdapter scheduleSubjectAdapter;

    private GridView grScheduleTextBook;
    private ArrayList<ScheduleTextBookModel> listTextbook = new ArrayList<>();
    private ScheduleTextBookAdapter scheduleTextBookAdapter;

    private LinearLayout subjectLayout, textbookLayout, layoutContent;

    private ImageView imgBack, Replay;

    private Button btnAddSchedule;

    private EditText edtGoal, edtContent;

    private ToggleButton tgRepeat;

    public static final int REQUEST_CODE_CREATE_SCHEDULE = 283;

    private ArrayList<ScheduleTypeModel> customTypes = new ArrayList<>();

     @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_act);

        Replay = (ImageView)findViewById(R.id.question_re);
        Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Plus_Act.this, "반복을 설정하면 고정 스케줄로 등록됩니다.", Toast.LENGTH_SHORT).show();
            }
        });

        grScheduleType = (GridView) findViewById(R.id.gr_type);

        grScheduleSubject = (GridView) findViewById(R.id.gr_subject);

        grScheduleTextBook = (GridView) findViewById(R.id.gr_textbook);

        subjectLayout = (LinearLayout) findViewById(R.id.subject_layout);

        textbookLayout = (LinearLayout) findViewById(R.id.textbook_layout);

        imgBack = (ImageView) findViewById(R.id.back_btn);
        imgBack.setOnClickListener(this);

        btnAddSchedule = (Button) findViewById(R.id.btn_add_schedule);
        btnAddSchedule.setOnClickListener(this);

        edtGoal = (EditText) findViewById(R.id.et_goal);

        tgRepeat = (ToggleButton) findViewById(R.id.tg_repeat);

        layoutContent = (LinearLayout) findViewById(R.id.layout_content_schedule);

        edtContent = (EditText) findViewById(R.id.edt_content);

        initScheduleSubjectGridview();

    }

    @Override
    protected void onStart() {
        super.onStart();

        initScheduleTextbook();

        getCustomType();

        refreshView();

    }

    private void refreshView() {
        subjectLayout.setVisibility(View.VISIBLE);
        textbookLayout.setVisibility(View.VISIBLE);

        scheduleSubjectAdapter.setItemSelected(0);


    }

    private void getCustomType() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));

            requestJson(Url_define.SCHEDULE_GET_ALL_CUSTOM_TYPE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    customTypes = new ArrayList<ScheduleTypeModel>();

                    try {

                        if(response.has("custom_type_list")){
                            JSONArray arrCustomTypes = new JSONArray(response.getString("custom_type_list"));

                            for (int i = 0; i < arrCustomTypes.length(); i++) {

                                JSONObject jsonCustom = arrCustomTypes.getJSONObject(i);

                                ScheduleTypeModel scheduleTypeModel = new ScheduleTypeModel();

                                scheduleTypeModel.setId(jsonCustom.getInt("id"));
                                scheduleTypeModel.setIcon_id(jsonCustom.getInt("icon_id"));
                                scheduleTypeModel.setName(jsonCustom.getString("title"));

                                scheduleTypeModel.setActivityType(3);
                                scheduleTypeModel.setIsAddIcon(false);
                                scheduleTypeModel.setIdDelete(false);

                                customTypes.add(scheduleTypeModel);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    initScheduleTypeGridView();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);

                    customTypes = new ArrayList<ScheduleTypeModel>();
                    initScheduleTypeGridView();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                    super.onFailure(statusCode, headers, t, res);

                    customTypes = new ArrayList<ScheduleTypeModel>();
                    initScheduleTypeGridView();
                }

            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void initScheduleTypeGridView() {

        listType = new ArrayList<>();

//      ScheduleTypeModel(int id, String name, boolean isAddIcon, int activityType, boolean idDelete, int icon_id) {


        listType.add(new ScheduleTypeModel(1, "자습", false, 1, false, 0));
        listType.add(new ScheduleTypeModel(2, "인강", false, 1, false, 0));
        listType.add(new ScheduleTypeModel(3, "학교", false, 1, false, 0));
        listType.add(new ScheduleTypeModel(4, "학원", false, 1, false, 0));
        listType.add(new ScheduleTypeModel(5, "과외", false, 1, false, 0));
        listType.add(new ScheduleTypeModel(6, "오답", false, 1, false, 0));
        listType.add(new ScheduleTypeModel(7, "모의고사", false, 1, false, 0));

        listType.add(new ScheduleTypeModel(8, "독서", false, 2, false, 0));
        listType.add(new ScheduleTypeModel(9, "수면", false, 2, false, 0));
        listType.add(new ScheduleTypeModel(10, "여가활동", false, 2, false, 0));
        listType.add(new ScheduleTypeModel(11, "휴식", false, 2, false, 0));
        listType.add(new ScheduleTypeModel(12, "식사", false, 2, false, 0));
        listType.add(new ScheduleTypeModel(13, "운동", false, 2, false, 0));

        listType.addAll(customTypes);

        listType.add(new ScheduleTypeModel(0, "", true, 0, false, 0));

        refreshScheduleType();

        grScheduleType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScheduleTypeModel itemSelected = (ScheduleTypeModel) parent.getAdapter().getItem(position);

                if (itemSelected.getId() == 0) {
                    // open add type screen

                    Intent intent = new Intent(Plus_Act.this, CreateScheduleActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CREATE_SCHEDULE);

                } else {
                    if (itemSelected.getActivityType() == 1) {
                        subjectLayout.setVisibility(View.VISIBLE);
                        textbookLayout.setVisibility(View.VISIBLE);
                        layoutContent.setVisibility(View.GONE);
                    } else {
                        subjectLayout.setVisibility(View.GONE);
                        textbookLayout.setVisibility(View.GONE);
                        layoutContent.setVisibility(View.VISIBLE);
                    }

                    scheduleTypeAdapter.setItemSelected(position);
                    scheduleTypeAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void refreshScheduleType() {
        scheduleTypeAdapter = new ScheduleTypeAdapter(this, listType);
        grScheduleType.setAdapter(scheduleTypeAdapter);
    }

    private void initScheduleSubjectGridview() {
        listSubject.add(new ScheduleSubjectModel(1, "국어", 1, "", ""));
        listSubject.add(new ScheduleSubjectModel(2, "수학", 2, "", ""));
        listSubject.add(new ScheduleSubjectModel(3, "영어", 3, "", ""));
        listSubject.add(new ScheduleSubjectModel(4, "사회", 4, "", ""));
        listSubject.add(new ScheduleSubjectModel(5, "과학", 5, "", ""));
        listSubject.add(new ScheduleSubjectModel(6, "제2외국어", 6, "", ""));
        listSubject.add(new ScheduleSubjectModel(7, "논술", 7, "", ""));
        listSubject.add(new ScheduleSubjectModel(8, "자소서", 8, "", ""));
        listSubject.add(new ScheduleSubjectModel(9, "비교과", 9, "", ""));
        listSubject.add(new ScheduleSubjectModel(10, "자격증", 10, "", ""));

        scheduleSubjectAdapter = new ScheduleSubjectAdapter(this, listSubject);
        grScheduleSubject.setAdapter(scheduleSubjectAdapter);

        grScheduleSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                scheduleSubjectAdapter.setItemSelected(position);
                scheduleSubjectAdapter.notifyDataSetChanged();

                initScheduleTextbook();

            }
        });

    }

    private void refreshTextbook() {
        scheduleTextBookAdapter = new ScheduleTextBookAdapter(this, listTextbook);
        grScheduleTextBook.setAdapter(scheduleTextBookAdapter);

        if (listTextbook.size() > 1) {
            scheduleTextBookAdapter.setItemSelected(0);
            scheduleTextBookAdapter.notifyDataSetChanged();
        }

    }


    // public ScheduleTextBookModel(int id, String name, int typeId, String typeName, String des, boolean isImgPlus) {
    private void initScheduleTextbook() {

        listTextbook = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));
            jsonObject.put("study_type", listSubject.get(scheduleSubjectAdapter.getItemSelected()).getId());

            requestJson(Url_define.SCHEDULE_TEXT_BOOK_LIST + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    //get all textbook list



                    try {
                        if (response.getString("result").equals("success")) {
                            String textBooksStr = response.getString("textbook_list");
                            JSONArray textBooksArr = new JSONArray(textBooksStr);

                            for (int i = 0; i < textBooksArr.length(); i++) {

                                JSONObject jsonTextBook = textBooksArr.getJSONObject(i);

                                ScheduleTextBookModel scheduleTextBookModel = new ScheduleTextBookModel();
                                scheduleTextBookModel.setId(jsonTextBook.getInt("id"));
                                scheduleTextBookModel.setName(jsonTextBook.getString("title"));
                                scheduleTextBookModel.setIsImgPlus(false);
                                scheduleTextBookModel.setIsCustom(jsonTextBook.getBoolean("isCustom"));

                                listTextbook.add(scheduleTextBookModel);

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listTextbook.add(new ScheduleTextBookModel(0, "", 1, "", "", true));

                    refreshTextbook();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);


                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }


        grScheduleTextBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ScheduleTextBookModel itemSelected = (ScheduleTextBookModel) parent.getAdapter().getItem(position);

                if (itemSelected.isImgPlus()) {
                    // show dialog add new textbook
                    TextbookPopupDialog textbookPopupDialog = new TextbookPopupDialog(Plus_Act.this, listSubject.get(scheduleSubjectAdapter.getItemSelected()).getId());
                    textbookPopupDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    textbookPopupDialog.setTextbookPopupDialogListener(new TextbookPopupDialog.TextbookPopupDialogListener() {
                        @Override
                        public void onComplete(ScheduleTextBookModel textBookModel) {
                            listTextbook.add(0, textBookModel);
                            refreshTextbook();
                        }
                    });

                    textbookPopupDialog.showView();
                } else {
                    scheduleTextBookAdapter.setItemSelected(position);
                    scheduleTextBookAdapter.notifyDataSetChanged();

                }

            }
        });

        grScheduleTextBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                DialogUtils.showConfirmAndCancelAlertDialog(Plus_Act.this, "해당 교재를 삭제하시겠어요?", new DialogUtils.ConfirmDialogOkCancelListener() {
                    @Override
                    public void onOkClick() {


                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("secret", PreferUtils.getSecret(Plus_Act.this));
                            jsonObject.put("user_id", PreferUtils.getUserId(Plus_Act.this));
                            jsonObject.put("study_type", listSubject.get(scheduleSubjectAdapter.getItemSelected()).getId());
                            jsonObject.put("textbook_id", listTextbook.get(scheduleTextBookAdapter.getItemSelected()).getId());
                            jsonObject.put("is_custom", listTextbook.get(scheduleTextBookAdapter.getItemSelected()).getIsCustom());

                            requestJson(Url_define.SCHEDULE_TEXT_BOOK_DELETE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);

                                    ScheduleTextBookModel itemSelected = (ScheduleTextBookModel) parent.getAdapter().getItem(position);
                                    listTextbook.remove(position);
                                    scheduleTextBookAdapter.remove(itemSelected);
                                    scheduleTextBookAdapter.notifyDataSetChanged();

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

                    @Override
                    public void onCancelClick() {

                    }
                });

                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn:
                finish();
                break;

            case R.id.btn_add_schedule:

                if (TextUtils.isEmpty(edtGoal.getText().toString())) {
                    Utils.showToastShort(this, "입력하신 정보를 다시 확인해주세요.");
                    break;
                }

                if (listType.get(scheduleTypeAdapter.getItemSelected()).getActivityType() == 1 && scheduleTextBookAdapter.getItemSelected() < 0) {
                    Utils.showToastShort(this, "입력하신 정보를 다시 확인해주세요.");
                    break;
                }

                if (TextUtils.isEmpty(edtGoal.getText().toString()) && (listType.get(scheduleTypeAdapter.getItemSelected()).getActivityType() == 2 || listType.get(scheduleTypeAdapter.getItemSelected()).getActivityType() == 3)) {
                    Utils.showToastShort(this, "입력하신 정보를 다시 확인해주세요.");
                    break;
                }

                doAddSchedule();

                break;
        }
    }

    private void doAddSchedule() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));
            jsonObject.put("activity_type", listType.get(scheduleTypeAdapter.getItemSelected()).getActivityType());
            jsonObject.put("activity_type_id", listType.get(scheduleTypeAdapter.getItemSelected()).getId());

            jsonObject.put("study_type", listSubject.get(scheduleSubjectAdapter.getItemSelected()).getId());

            if (scheduleTextBookAdapter.getItemSelected() >= 0 && listTextbook.get(scheduleTextBookAdapter.getItemSelected()) != null) {
                if(listTextbook.get(scheduleTextBookAdapter.getItemSelected()).getIsCustom()){
                    jsonObject.put("custom_textbook_id", listTextbook.get(scheduleTextBookAdapter.getItemSelected()).getId());
                    jsonObject.put("textbook_id", 0);
                }else{
                    jsonObject.put("textbook_id", listTextbook.get(scheduleTextBookAdapter.getItemSelected()).getId());
                    jsonObject.put("custom_textbook_id", 0);
                }
            } else {
                jsonObject.put("custom_textbook_id", 0);
                jsonObject.put("textbook_id", 0);
            }

            jsonObject.put("contents", edtContent.getText().toString());
            jsonObject.put("goal", edtGoal.getText().toString());
            jsonObject.put("is_repeated", tgRepeat.isChecked());

            requestJson(Url_define.Student_Plus_My_act + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Toast.makeText(Plus_Act.this, "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();

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

        finish();
    }


}
