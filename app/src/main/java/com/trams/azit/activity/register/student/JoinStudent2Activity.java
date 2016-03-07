package com.trams.azit.activity.register.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.common.Constants;
import com.trams.azit.util.ConnActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Hashtable;

/**
 * Created by sonnv on 1/12/2016.
 */
public class JoinStudent2Activity extends ConnActivity {
    private Button btSelectMan, btSelectWoman, btGotoNext;
    private EditText edName, edNickname, edCellPhone, edParentName, edParentCellPhone;
    private AutoCompleteTextView edSchool;
    private Spinner spStudentYear;

    private String textName, textNickname, textCellPhone, textSchool, textParentName, textParentCellPhone;
    private boolean genderMan = true;
    private Hashtable<String, String> hashTable;

    private String emailValue, passwordValue, edu_inst_idValue, genderValue;
    private int yearValue;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join2);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            emailValue = extra.getString(Constants.EMAIL_KEY);
            passwordValue = extra.getString(Constants.PASSWORD_KEY);
        }
        initUI();
    }

    private void initUI() {
        btSelectMan = (Button) findViewById(R.id.btSelectMan);
        btSelectWoman = (Button) findViewById(R.id.btSelectWoman);
        btGotoNext = (Button) findViewById(R.id.btGotoNext);
        btGotoNext.setEnabled(false);


        spStudentYear = (Spinner) findViewById(R.id.spStudentYear);
        spStudentYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearValue = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edName = (EditText) findViewById(R.id.edName);
        edNickname = (EditText) findViewById(R.id.edNickname);
        edCellPhone = (EditText) findViewById(R.id.edCellPhone);
        edSchool = (AutoCompleteTextView) findViewById(R.id.edSchool);
        edParentName = (EditText) findViewById(R.id.edParentName);
        edParentCellPhone = (EditText) findViewById(R.id.edParentCellPhone);

        edName.addTextChangedListener(textWatcher);
        edNickname.addTextChangedListener(textWatcher);
        edCellPhone.addTextChangedListener(textWatcher);
        edParentName.addTextChangedListener(textWatcher);
        edParentCellPhone.addTextChangedListener(textWatcher);
        edSchool.addTextChangedListener(textWatcher);

        initData();
    }

    private void initData() {
        final String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        final String[] arrayOfSchoolId = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolId[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        edSchool.setAdapter(adapter);

        edSchool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < arrayOfSchool.length; i++) {
                    if (arrayOfSchool[i].equals(edSchool.getText().toString())) {
                        edu_inst_idValue = arrayOfSchoolId[i];

                    }
                }

                Log.e(edSchool.getText().toString(), hashTable.get(edSchool.getText().toString()));
            }
        });

        genderMan = true;
        genderValue = "M";
        btSelectMan.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
        btSelectWoman.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
    }

    public void setMan(View view) {
        genderMan = true;
        genderValue = "M";
        btSelectMan.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
        btSelectWoman.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
    }

    public void setWoman(View view) {
        genderValue = "F";
        genderMan = false;
        btSelectMan.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
        btSelectWoman.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
    }

    public void gotoBack(View view) {
        finish();
    }

    public void gotoNext(View view) {
        Intent intent = new Intent(JoinStudent2Activity.this, JoinStudent3Activity.class);
        getData();
        intent.putExtra(Constants.EMAIL_KEY, emailValue);
        intent.putExtra(Constants.PASSWORD_KEY, passwordValue);
        intent.putExtra(Constants.NAME_KEY, textName);
        intent.putExtra(Constants.NICKNAME_KEY, textNickname);
        intent.putExtra(Constants.PHONE_KEY, textCellPhone);
        intent.putExtra(Constants.EDU_INST_ID_KEY, edu_inst_idValue);
        intent.putExtra(Constants.YEAR_KEY, yearValue);
        intent.putExtra(Constants.GENDER_KEY, genderValue);
        intent.putExtra(Constants.PARENT_NAME_KEY, textParentName);
        intent.putExtra(Constants.PARENT_PHONE_KEY, textParentCellPhone);

        startActivity(intent);
    }

    public void showAdvantageParentCellPhone(View view) {
        Toast.makeText(JoinStudent2Activity.this, getResources().getString(R.string.text_show_advantage_parent_cell_phone), Toast.LENGTH_LONG).show();
    }

    private boolean checkStatusGotoNext() {
        getData();
        if (!textName.equals("") && !textNickname.equals("")
                && !textCellPhone.equals("") && !textSchool.equals("")
                && !textParentName.equals("") && !textParentCellPhone.equals("")) {
            return true;
        }
        return false;
    }

    private void getData() {
        textName = edName.getText().toString().trim();
        textNickname = edNickname.getText().toString().trim();
        textCellPhone = edCellPhone.getText().toString().trim();
        textParentName = edParentName.getText().toString().trim();
        textParentCellPhone = edParentCellPhone.getText().toString().trim();
        textSchool = edSchool.getText().toString().trim();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (checkStatusGotoNext()) {
                btGotoNext.setEnabled(true);
                btGotoNext.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
            } else {
                btGotoNext.setEnabled(false);
                btGotoNext.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
            }
        }
    };


}
