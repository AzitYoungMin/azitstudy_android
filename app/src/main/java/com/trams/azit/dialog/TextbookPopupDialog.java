package com.trams.azit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
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
public class TextbookPopupDialog extends Dialog implements View.OnClickListener {


    public interface TextbookPopupDialogListener {
        public void onComplete(ScheduleTextBookModel textBookModel);
    }

    private TextbookPopupDialogListener textbookPopupDialogListener;

    public TextbookPopupDialogListener getTextbookPopupDialogListener() {
        return textbookPopupDialogListener;
    }

    public void setTextbookPopupDialogListener(TextbookPopupDialogListener textbookPopupDialogListener) {
        this.textbookPopupDialogListener = textbookPopupDialogListener;
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

    public TextbookPopupDialog(Context context, int subjectId) {
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
                //String inputStr = edtInput.getText().toString();

                if(textbookSearchAdapter != null)
                    textbookSearchAdapter.getFilter().filter(s.toString());

//                textBookResultSearch = new ArrayList<ScheduleTextBookModel>();

//                if (!TextUtils.isEmpty(inputStr)) {

//                    textBookResultSearch = new ArrayList<>();
//
//                    getListSearch(inputStr);
//                    refreshListview();

//                }

//                for (String schoolName : arrayOfSchool) {
//                    if (schoolName.contains(inputStr)) {
//                        belongModels.add(new BelongModel(false, hashTable.get(schoolName), schoolName));
//                    }
//                }

            }
        });

    }

    private void getListSearch(String inputSearch) {
        for (ScheduleTextBookModel scheduleTextBookModel : textBookModels) {
            if (scheduleTextBookModel.getName().contains(inputSearch))
                textBookResultSearch.add(scheduleTextBookModel);
        }
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
            jsonRequest.put("study_type", subjectId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkHelper.requestJson(getContext(), Url_define.GET_TEXTBOOK + Url_define.KEY, jsonRequest, new NetworkHelper.ConnHttpResponseHandler() {
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

//                    textbookSearchAdapter = new TextbookSearchAdapter(getContext(), textBookModels);
//                    lvResult.setAdapter(textbookSearchAdapter);

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

    /**
     * hide view
     */
    public void hideView() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        Window window = getDialog().getWindow();
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.textbook_search_popup_layout, container);
//
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.NewDialog);
//
//        btnClose = (ImageView) view.findViewById(R.id.btn_close);
//        btnClose.setOnClickListener(this);
//
//        btnOk = (Button) view.findViewById(R.id.btn_belong_ok);
//        btnOk.setOnClickListener(this);
//
//        lvResult = (ListView) view.findViewById(R.id.lv_belong_result);
//
//        edtInput = (EditText) view.findViewById(R.id.edt_input);
//
//        edtInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String inputStr = edtInput.getText().toString();
//                textBookModels = new ArrayList<>();
//
////                for (String schoolName : arrayOfSchool) {
////                    if (schoolName.contains(inputStr)) {
////                        belongModels.add(new BelongModel(false, hashTable.get(schoolName), schoolName));
////                    }
////                }
//
//                refreshBelongResult();
//
//            }
//        });
//
//        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                textbookSearchAdapter.setItemSelected(position);
//                textbookSearchAdapter.notifyDataSetChanged();
//            }
//        });
//
//        return view;
//    }
//
//    private void refreshBelongResult() {
//        textbookSearchAdapter = new TextbookSearchAdapter(getActivity(), textBookModels);
//        lvResult.setAdapter(textbookSearchAdapter);
//    }
//
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;

            case R.id.btn_belong_ok:
                if(textbookSearchAdapter == null || (TextUtils.isEmpty(edtInput.getText().toString()) && textbookSearchAdapter.getSelectedItemId() == 0)){
                    if(!TextUtils.isEmpty(edtInput.getText().toString())){
                        addCustomTextbook(edtInput.getText().toString());
                    }else{
                        Utils.showToastShort(getContext(),"援먯옱瑜??좏깮?댁＜?몄슂.");
                        return;
                    }
                }else if(!TextUtils.isEmpty(edtInput.getText().toString()) && textbookSearchAdapter.getSelectedItemId() == 0){
                    addCustomTextbook(edtInput.getText().toString());
                }else{
                    doSubmit();
                }
//                if (belongPopupListener != null) {
//                    belongPopupListener.onComplete(belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()));
//                    LogUtils.d(TAG, "Belong Selected : " + belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()));
//                }
//                doSubmit();
                break;

        }
    }

    private void doSubmit() {

 //       if(textbookSearchAdapter == null ||textbookSearchAdapter.getItemSelected() <0){
  //          Utils.showToastShort(getContext(),"교재를 선택해주세요.");
  //          return;
   //     }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(getContext()));
            jsonObject.put("user_id", PreferUtils.getUserId(getContext()));
            jsonObject.put("study_type", subjectId);
            jsonObject.put("textbook_id", textbookSearchAdapter.getSelectedItemId());
            jsonObject.put("is_custom", textbookSearchAdapter.getSelectedItemIsCustom());


            NetworkHelper.requestJson(getContext(), Url_define.SCHEDULE_TEXT_BOOK_ADD + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (textbookPopupDialogListener != null) {
                        ScheduleTextBookModel textBook = new ScheduleTextBookModel();
                        for (ScheduleTextBookModel scheduleTextBookModel : textBookResultSearch){
                            if(scheduleTextBookModel.getId() == textbookSearchAdapter.getSelectedItemId() && scheduleTextBookModel.getIsCustom() == textbookSearchAdapter.getSelectedItemIsCustom()){
                                textBook = scheduleTextBookModel;
                                break;
                            }
                        }
                        textbookPopupDialogListener.onComplete(textBook);
                    }

                    dismiss();
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

    private void addCustomTextbook(String title) {

        final String textbookTitle = title;

        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("secret", PreferUtils.getSecret(getContext()));
            jsonRequest.put("user_id", PreferUtils.getUserId(getContext()));
            jsonRequest.put("subject_id", subjectId);
            jsonRequest.put("title", textbookTitle);
            jsonRequest.put("is_study", true);
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
                        textbookPopupDialogListener.onComplete(textbook);
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
