package com.trams.azit.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.trams.azit.R;
import com.trams.azit.adapter.ScheduleCreateAdapter;
import com.trams.azit.adapter.ScheduleTypeAdapter;
import com.trams.azit.dialog.DialogUtils;
import com.trams.azit.model.ScheduleTypeModel;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.trams.azit.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ADMIN on 1/15/2016.
 */
public class CreateScheduleActivity extends ConnActivity implements View.OnClickListener {

    private GridView grSchedule;
    private ScheduleTypeAdapter scheduleTypeAdapter;
    private ArrayList<ScheduleTypeModel> scheduleTypeModels = new ArrayList<>();

    private GridView grCreate;
    private ScheduleCreateAdapter scheduleCreateAdapter;
    private ArrayList<ScheduleTypeModel> scheduleCreates = new ArrayList<>();

    private ImageView imgBack;

    private Button btnAddSchedule;

    private EditText edtScheduleName;
    private ArrayList<ScheduleTypeModel> customTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_schedule_activity);

        grSchedule = (GridView) findViewById(R.id.gr_schedule);

        grCreate = (GridView) findViewById(R.id.gr_schedule_all);

        imgBack = (ImageView) findViewById(R.id.back_btn);
        imgBack.setOnClickListener(this);

        btnAddSchedule = (Button) findViewById(R.id.btn_add_schedule);
        btnAddSchedule.setOnClickListener(this);

        edtScheduleName = (EditText) findViewById(R.id.edt_shedule_name);

        initScheduleCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCustomType();
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
                        JSONArray arrCustomTypes = new JSONArray(response.getString("custom_type_list"));

                        for (int i = 0; i < arrCustomTypes.length(); i++) {

                            JSONObject jsonCustom = arrCustomTypes.getJSONObject(i);

                            ScheduleTypeModel scheduleTypeModel = new ScheduleTypeModel();

                            scheduleTypeModel.setId(jsonCustom.getInt("id"));
                            scheduleTypeModel.setIcon_id(jsonCustom.getInt("icon_id"));
                            scheduleTypeModel.setName(jsonCustom.getString("title"));

                            scheduleTypeModel.setActivityType(3);
                            scheduleTypeModel.setIsAddIcon(false);
                            scheduleTypeModel.setIdDelete(true);

                            customTypes.add(scheduleTypeModel);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    addSchedule();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);

                    customTypes = new ArrayList<ScheduleTypeModel>();
                    addSchedule();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                    super.onFailure(statusCode, headers, t, res);

                    customTypes = new ArrayList<ScheduleTypeModel>();
                    addSchedule();
                }

            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void addSchedule() {
        scheduleTypeModels = new ArrayList<>();

        //        listType.add(new ScheduleTypeModel(1, "자습", false, 1, false, 0));

        scheduleTypeModels.add(new ScheduleTypeModel(1, "자습", false, 1, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(2, "인강", false, 1, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(3, "학교", false, 1, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(4, "학원", false, 1, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(5, "과외", false, 1, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(6, "오답", false, 1, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(7, "모의고사", false, 1, false, 0));

        scheduleTypeModels.add(new ScheduleTypeModel(8, "수면", false, 2, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(9, "식사", false, 2, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(10, "여가활동", false, 2, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(11, "휴식", false, 2, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(12, "독서", false, 2, false, 0));
        scheduleTypeModels.add(new ScheduleTypeModel(13, "운동", false, 2, false, 0));

        scheduleTypeModels.addAll(customTypes);

        refreshSchedule();
    }

    private void deleteScheduleCustomType(final int position) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(this));
            jsonObject.put("user_id", PreferUtils.getUserId(this));
            jsonObject.put("custom_type_id", scheduleTypeModels.get(position).getId());

            requestJson(Url_define.SCHEDULE_DELETE_CUSTOM_TYPE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    scheduleTypeModels.remove(position);
                    refreshSchedule();

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

    private void refreshSchedule() {
        scheduleTypeAdapter = new ScheduleTypeAdapter(this, scheduleTypeModels);
        scheduleTypeAdapter.setItemSelected(-1);
        grSchedule.setAdapter(scheduleTypeAdapter);

        scheduleTypeAdapter.setScheduleDeleteListener(new ScheduleTypeAdapter.ScheduleDeleteListener() {
            @Override
            public void onDelete(final int position) {
                DialogUtils.showConfirmAndCancelAlertDialog(CreateScheduleActivity.this, "삭제하시겠습니까?", new DialogUtils.ConfirmDialogOkCancelListener() {
                    @Override
                    public void onOkClick() {

                        deleteScheduleCustomType(position);

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
            }
        });
    }

    private void initScheduleCreate() {
//        listType.add(new ScheduleTypeModel(1, "자습", false, 1, false, 0));

        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 21));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 22));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 23));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 24));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 25));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 26));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 27));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 28));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 29));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 30));

        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 31));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 32));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 33));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 34));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 35));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 36));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 37));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 38));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 39));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 40));

        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 41));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 42));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 43));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 44));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 45));
        scheduleCreates.add(new ScheduleTypeModel(-1, "", false, 3, true, 46));


        grCreate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                scheduleCreateAdapter.setPositionSelected(position);
                scheduleCreateAdapter.notifyDataSetChanged();
            }
        });

        refreshScheduleCreate();

    }

    private void refreshScheduleCreate() {
        scheduleCreateAdapter = new ScheduleCreateAdapter(this, scheduleCreates);
        grCreate.setAdapter(scheduleCreateAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn:
                finish();
                break;

            case R.id.btn_add_schedule:
                doCreate();
                break;
        }
    }

    private void doCreate() {

        if (scheduleCreateAdapter.getPositionSelected() == -1 || TextUtils.isEmpty(edtScheduleName.getText().toString())) {
            Utils.showToastShort(this, "입력하신 정보를 다시 확인해주세요.");
        } else {
            //add new custom type

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("secret", PreferUtils.getSecret(this));
                jsonObject.put("user_id", PreferUtils.getUserId(this));

                jsonObject.put("title", edtScheduleName.getText().toString());
                jsonObject.put("icon_id", scheduleCreates.get(scheduleCreateAdapter.getPositionSelected()).getIcon_id());

                requestJson(Url_define.SCHEDULE_ADD_CUSTOM_TYPE + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        finish();
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
    }
}
