package com.trams.azit.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.trams.azit.BelongModel;
import com.trams.azit.BelongPopupAdapter;
import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.network.NetworkHelper;
import com.trams.azit.preference.PreferUtils;
import com.trams.azit.util.Url_define;
import com.trams.azit.util.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Administrator on 05/01/2016.
 */
public class BelongPopupUpdateDialog extends DialogFragment implements View.OnClickListener {

    public interface BelongPopupListener {
        public void onComplete(BelongModel belongModel);
    }

    private BelongPopupListener belongPopupListener;

    public boolean isGraduate() {
        return isGraduate;
    }

    public void setIsGraduate(boolean isGraduate) {
        this.isGraduate = isGraduate;
    }

    private boolean isGraduate;

    public BelongPopupListener getBelongPopupListener() {
        return belongPopupListener;
    }

    public void setBelongPopupListener(BelongPopupListener belongPopupListener) {
        this.belongPopupListener = belongPopupListener;
    }


    private static final String TAG = BelongPopupUpdateDialog.class.getName();
    private ImageView btnClose;
    private Button btnOk;
    private ListView lvResult;
    private BelongPopupAdapter belongPopupAdapter;
    private ArrayList<BelongModel> belongModels = new ArrayList<>();
    private EditText edtInput;
    Hashtable<String, String> hashTable;
    String[] arrayOfSchool;
    String[] arrayOfSchoolid;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayOfSchool = getResources().getStringArray(R.array.school_name);
        arrayOfSchoolid = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolid[i]);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.belong_popup_layout, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.NewDialog);

        btnClose = (ImageView) view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnOk = (Button) view.findViewById(R.id.btn_belong_ok);
        btnOk.setOnClickListener(this);

        lvResult = (ListView) view.findViewById(R.id.lv_belong_result);

        edtInput = (EditText) view.findViewById(R.id.edt_input);

        belongModels = new ArrayList<>();

        for (String schoolName : arrayOfSchool) {
            belongModels.add(new BelongModel(false, hashTable.get(schoolName), schoolName));
        }

        refreshBelongResult();

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
                belongModels = new ArrayList<>();

                for (String schoolName : arrayOfSchool) {
                    if (schoolName.contains(inputStr)) {
                        belongModels.add(new BelongModel(false, hashTable.get(schoolName), schoolName));
                    }
                }

                refreshBelongResult();

            }
        });

        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                belongPopupAdapter.setSelectedIdx(position);
                belongPopupAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void refreshBelongResult() {
        belongPopupAdapter = new BelongPopupAdapter(getActivity(), belongModels);
        lvResult.setAdapter(belongPopupAdapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;

            case R.id.btn_belong_ok:
                onSubmit();
                break;
        }
    }

    private void onSubmit() {

        if (belongPopupAdapter.getSelectedIdx() < 0) {
            Utils.showToastShort(getActivity(), "학교를 선택해주세요.");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", PreferUtils.getSecret(getActivity()));
            jsonObject.put("user_id", PreferUtils.getUserId(getActivity()));
            jsonObject.put("edu_inst_id", Integer.parseInt(belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()).getId()));

            Log.e("jsonObject", jsonObject.toString());

            NetworkHelper.requestJson(getActivity(), Url_define.UPDATE_EDU_TEACHER + Url_define.KEY, jsonObject, new NetworkHelper.ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (belongPopupListener != null) {
                            belongPopupListener.onComplete(belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()));
                            LogUtils.d(TAG, "Belong Selected : " + belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()));
                        }

                        dismiss();
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

}
