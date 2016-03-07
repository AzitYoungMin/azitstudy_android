package com.trams.azit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.trams.azit.util.Utils;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Administrator on 05/01/2016.
 */
public class BelongPopupDialog extends Dialog implements View.OnClickListener {

    Context context;


    public interface BelongPopupListener {
        public void onComplete(BelongModel belongModel);
    }

    private BelongPopupListener belongPopupListener;

    public BelongPopupListener getBelongPopupListener() {
        return belongPopupListener;
    }

    public void setBelongPopupListener(BelongPopupListener belongPopupListener) {
        this.belongPopupListener = belongPopupListener;
    }

    public void showView() {
        if (!this.isShowing()) {
            this.show();
        }
    }


    private static final String TAG = BelongPopupDialog.class.getName();
    private ImageView btnClose;
    private Button btnOk;
    private ListView lvResult;
    private BelongPopupAdapter belongPopupAdapter;
    private ArrayList<BelongModel> belongModels = new ArrayList<>();
    private EditText edtInput;
    Hashtable<String, String> hashTable;
    String[] arrayOfSchool;
    String[] arrayOfSchoolid;

    public BelongPopupDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.belong_popup_layout);

        this.context = context;

        arrayOfSchool = context.getResources().getStringArray(R.array.school_name);
        arrayOfSchoolid = context.getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolid[i]);
        }

        btnClose = (ImageView) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_belong_ok);
        btnOk.setOnClickListener(this);

        lvResult = (ListView) findViewById(R.id.lv_belong_result);

        edtInput = (EditText) findViewById(R.id.edt_input);

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
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        Window window = context.getDialog().getWindow();
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        context.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.belong_popup_layout, container);
//
//        context.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        context.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.NewDialog);
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
//        belongModels = new ArrayList<>();
//
//        for (String schoolName : arrayOfSchool) {
//            belongModels.add(new BelongModel(false, hashTable.get(schoolName), schoolName));
//        }
//
//        refreshBelongResult();
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
//                belongModels = new ArrayList<>();
//
//                for (String schoolName : arrayOfSchool) {
//                    if (schoolName.contains(inputStr)) {
//                        belongModels.add(new BelongModel(false, hashTable.get(schoolName), schoolName));
//                    }
//                }
//
//                refreshBelongResult();
//
//            }
//        });
//
//        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                belongPopupAdapter.setSelectedIdx(position);
//                belongPopupAdapter.notifyDataSetChanged();
//            }
//        });
//
//        return view;
//    }

    private void refreshBelongResult() {
        belongPopupAdapter = new BelongPopupAdapter(context, belongModels);
        lvResult.setAdapter(belongPopupAdapter);
    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;

            case R.id.btn_belong_ok:

                if(belongPopupAdapter.getSelectedIdx() < 0){
                    Utils.showToastShort(context,"학교를 선택해주세요.");
                    break;
                }

                if (belongPopupListener != null) {
                    belongPopupListener.onComplete(belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()));
                    LogUtils.d(TAG, "Belong Selected : " + belongPopupAdapter.getItem(belongPopupAdapter.getSelectedIdx()));
                }

                dismiss();
                break;

        }
    }
}
