package com.trams.azit.dialog;

import android.app.Dialog;
import android.content.Context;

import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.trams.azit.R;
import com.trams.azit.adapter.TextbookSearchAdapter;
import com.trams.azit.model.ScheduleTextBookModel;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.MyProgressDialog;
import com.trams.azit.util.Url_define;
import com.trams.azit.util.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 05/01/2016.
 */
public class TextbookPopDialog extends Dialog implements View.OnClickListener {


    public interface TextbookPopDialogListener {
        public void onComplete(ScheduleTextBookModel textBookModel);
    }

    private TextbookPopDialogListener textbookPopDialogListener;

    public TextbookPopDialogListener getTextbookPopDialogListener() {
        return textbookPopDialogListener;
    }

    public void setTextbookPopDialogListener(TextbookPopDialogListener textbookPopDialogListener) {
        this.textbookPopDialogListener = textbookPopDialogListener;
    }

    private static final String TAG = TextbookPopupDialog.class.getName();
    private ImageView btnClose;
    private Button btnOk;
    private ListView lvResult;
    private TextbookSearchAdapter textbookSearchAdapter;
    private ArrayList<ScheduleTextBookModel> textBookModels = new ArrayList<>();
    private EditText edtInput;
    private int subjectId;

    private ArrayList<ScheduleTextBookModel> textBookResultSearch = new ArrayList<>();

    public TextbookPopDialog(Context context, int subjectId) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.textbook_search_popup_layout);

        btnClose = (ImageView) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_belong_ok);
        btnOk.setOnClickListener(this);

        lvResult = (ListView) findViewById(R.id.lv_belong_result);

        edtInput = (EditText) findViewById(R.id.edt_input);

        this.subjectId = subjectId;

        getTextbookFromSubjectId();

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = edtInput.getText().toString();

                if(textbookSearchAdapter != null)
                    textbookSearchAdapter.getFilter().filter(inputStr);
            }
        });

    }


    private void refreshListview() {
        textbookSearchAdapter = new TextbookSearchAdapter(getContext(), textBookResultSearch);
        lvResult.setAdapter(textbookSearchAdapter);
    }


    private void getTextbookFromSubjectId() {

        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("secret", PreferUtils.getSecret(getContext()));
            jsonRequest.put("user_id", PreferUtils.getUserId(getContext()));
            jsonRequest.put("subject_id", subjectId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkHelper.requestJson(getContext(), Url_define.GET_TEXTBOOK_LIST + Url_define.KEY, jsonRequest, new NetworkHelper.ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    if (response.getString("result").equals("success")) {
                        String jsonArrayStr = response.getString("textbook_list");
                        JSONArray arrTextbook = new JSONArray(jsonArrayStr);

                        for (int i = 0; i < arrTextbook.length(); i++) {

                            ScheduleTextBookModel scheduleTextBookModel = new ScheduleTextBookModel();
                            scheduleTextBookModel.setId(arrTextbook.getJSONObject(i).getInt("id"));
                            scheduleTextBookModel.setName(arrTextbook.getJSONObject(i).getString("title"));
                            scheduleTextBookModel.setIsCustom(arrTextbook.getJSONObject(i).getBoolean("isCustom"));

                            textBookModels.add(scheduleTextBookModel);
                        }

                        textBookResultSearch = textBookModels;

                        refreshListview();

                        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                textbookSearchAdapter.setSelectedItemdId(textbookSearchAdapter.getItem(position).getId());
                                textbookSearchAdapter.setSelectedItemIsCustom(textbookSearchAdapter.getItem(position).getIsCustom());

                                textbookSearchAdapter.setItemSelected(position);
                                textbookSearchAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d("statusCode : ", statusCode + "");
                super.onFailure(statusCode, headers, res, t);

            }
        });

    }

    /**
     * Show view
     */
    public void showView() {
        if (!this.isShowing()) {
            this.show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;

            case R.id.btn_belong_ok:
                if (textbookSearchAdapter == null || (TextUtils.isEmpty(edtInput.getText().toString()) && textbookSearchAdapter.getSelectedItemId() == 0)) {
                    if (!TextUtils.isEmpty(edtInput.getText().toString())) {
                        addCustomTextbook(edtInput.getText().toString());
                    } else {
                        Utils.showToastShort(getContext(),"교재를 선택해주세요.");
                        return;
                    }
                } else if (!TextUtils.isEmpty(edtInput.getText().toString()) && textbookSearchAdapter.getSelectedItemId() == 0) {
                    addCustomTextbook(edtInput.getText().toString());
                } else {
                    doSubmit();
                }
                break;

        }
    }

    private void doSubmit() {
        if (textbookPopDialogListener != null) {
            ScheduleTextBookModel textBook = new ScheduleTextBookModel();
            for (ScheduleTextBookModel scheduleTextBookModel : textBookResultSearch){
                if(scheduleTextBookModel.getId() == textbookSearchAdapter.getSelectedItemId() && scheduleTextBookModel.getIsCustom() == textbookSearchAdapter.getSelectedItemIsCustom()){
                    textBook = scheduleTextBookModel;
                    break;
                }
            }
            textbookPopDialogListener.onComplete(textBook);
        }

        dismiss();
    }

    private void addCustomTextbook(String title) {

        final String textbookTitle = title;

        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("secret", PreferUtils.getSecret(getContext()));
            jsonRequest.put("user_id", PreferUtils.getUserId(getContext()));
            jsonRequest.put("subject_id", subjectId);
            jsonRequest.put("title", textbookTitle);
            jsonRequest.put("is_study", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkHelper.requestJson(getContext(), Url_define.STUDENT_ADD_CUSTOM_TEXTBOOK + Url_define.KEY, jsonRequest, new NetworkHelper.ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    if (response.getString("result").equals("success")) {
                        String textbookId = response.getString("custom_textbook_id");

                        ScheduleTextBookModel textbook = new ScheduleTextBookModel();
                        textbook.setId(Integer.parseInt(textbookId));
                        textbook.setName(textbookTitle);
                        textbook.setIsCustom(true);
                        textbookPopDialogListener.onComplete(textbook);
                        dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d("statusCode : ", statusCode + "");
                super.onFailure(statusCode, headers, res, t);

            }
        });

    }
}
