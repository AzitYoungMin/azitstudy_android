package com.trams.azit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.common.Constants;
import com.trams.azit.dialog.DialogUtils;

/**
 * Created by sonnv on 1/13/2016.
 */
public class NaturalScienceFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, DialogUtils.SelectSecondLanguage {
    private ScrollView parentScrollView, childScrollView;

    private TextView tvTitleMathType;
    private TextView tvTitleArtMusicPhysicalEducation;
    private TextView tvTitleEngineeringCollege;
    private TextView tvTitleCollegeOfHumanEcology;
    private TextView tvTitleNaturalScienceCollege;
    private TextView tvTitleCollegeOfMedicine;
    private TextView tvTitleCollegeOfEducation;
    private TextView tvTitleCollegeOfFineArt;
    private TextView tvTitleArtMusicPhysicalEducationSchool;
    private TextView tvTitleSecondLanguage;
    private TextView tvSecondLanguage;
    private TextView tvMajor;
    private TextView tvSocialScience;

    private CheckBox cbMathTypeA, cbMathTypeB,cbArtMusicPhysicalEducation;
    private CheckBox cbEngineerOfCollege;
    private CheckBox cbCollegeOfHumanEcology;
    private CheckBox cbNaturalScienceCollege;
    private CheckBox cbCollegeOfMedicine;
    private CheckBox cbCollegeOfEducation;
    private CheckBox cbCollegeOfFineArt;
    private CheckBox cbArtMusicPhysicalEducationSchool;
    private CheckBox cbSecondLanguage;

    private boolean isChooseMathTypeA = false;
    private boolean isChooseMathTypeB = false;
    private boolean isChooseArtMusicPhysicalEducation = false;
    private boolean isChooseSecondLanguage = false;

    private boolean isChooseEngineerOfCollege = false;
    private boolean isChooseCollegeOfHumanEcology = false;
    private boolean isChooseNaturalScienceCollege = false;
    private boolean isChooseCollegeOfMedicine = false;
    private boolean isChooseCollegeOfEducation = false;
    private boolean isChooseCollegeOfFineArt = false;
    private boolean isChooseArtMusicPhysicalEducationSchool = false;

    private boolean isChooseMajor = false;

    private boolean selectPhysic1 = false;
    private boolean selectPhysic2 = false;
    private boolean selectChemistry1 = false;
    private boolean selectChemistry2 = false;
    private boolean selectLifeScience1 = false;
    private boolean selectLifeScience2 = false;
    private boolean selectEarthScience1 = false;
    private boolean selectEarthScience2 = false;

    private Button btPhysic1;
    private Button btPhysic2;
    private Button btChemistry1;
    private Button btChemistry2;
    private Button btLifeScience1;
    private Button btLifeScience2;
    private Button btEarthScience1;
    private Button btEarthScience2;
    private Button btDone;

    private int numberSubject = 0;
    private int math_typeValue = 1;
    private boolean mp_educationValue = false;
    private String  optional_subjectsValue = "", target_departmentValue="";
    private int foreign_languageValue = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_natural_science, container, false);

        parentScrollView = (ScrollView)view.findViewById(R.id.scrollViewParent);
        childScrollView = (ScrollView)view.findViewById(R.id.scrollViewChild);

        setupScrollView();

        cbMathTypeA = (CheckBox) view.findViewById(R.id.cbMathTypeA);
        cbMathTypeB = (CheckBox) view.findViewById(R.id.cbMathTypeB);
        cbArtMusicPhysicalEducation = (CheckBox) view.findViewById(R.id.cbArtMusicPhysicalEducation);
        cbSecondLanguage = (CheckBox) view.findViewById(R.id.cbSecondLanguage);

        cbEngineerOfCollege = (CheckBox) view.findViewById(R.id.cbEngineerOfCollege);
        cbCollegeOfHumanEcology = (CheckBox) view.findViewById(R.id.cbCollegeOfHumanEcology);
        cbNaturalScienceCollege = (CheckBox) view.findViewById(R.id.cbNaturalScienceCollege);
        cbCollegeOfMedicine = (CheckBox) view.findViewById(R.id.cbCollegeOfMedicine);
        cbCollegeOfEducation = (CheckBox) view.findViewById(R.id.cbCollegeOfEducation);
        cbCollegeOfFineArt = (CheckBox) view.findViewById(R.id.cbCollegeOfFineArt);
        cbArtMusicPhysicalEducationSchool = (CheckBox) view.findViewById(R.id.cbArtMusicPhysicalEducationSchool);

        cbMathTypeA.setOnCheckedChangeListener(this);
        cbMathTypeB.setOnCheckedChangeListener(this);
        cbArtMusicPhysicalEducation.setOnCheckedChangeListener(this);
        cbSecondLanguage.setOnCheckedChangeListener(this);

        cbEngineerOfCollege.setOnCheckedChangeListener(this);
        cbCollegeOfHumanEcology.setOnCheckedChangeListener(this);
        cbNaturalScienceCollege.setOnCheckedChangeListener(this);
        cbCollegeOfMedicine.setOnCheckedChangeListener(this);
        cbCollegeOfEducation.setOnCheckedChangeListener(this);
        cbCollegeOfFineArt.setOnCheckedChangeListener(this);
        cbArtMusicPhysicalEducationSchool.setOnCheckedChangeListener(this);


        btPhysic1 = (Button) view.findViewById(R.id.btPhysic1);
        btPhysic2 = (Button) view.findViewById(R.id.btPhysic2);
        btChemistry1 = (Button) view.findViewById(R.id.btChemistry1);
        btChemistry2 = (Button) view.findViewById(R.id.btChemistry2);
        btLifeScience1 = (Button) view.findViewById(R.id.btLifeScience1);
        btLifeScience2 = (Button) view.findViewById(R.id.btLifeScience2);
        btEarthScience1 = (Button) view.findViewById(R.id.btEarthScience1);
        btEarthScience2 = (Button) view.findViewById(R.id.btEarthScience2);
        btDone = (Button) view.findViewById(R.id.btDone);

        btPhysic1.setOnClickListener(this);
        btPhysic2.setOnClickListener(this);
        btChemistry1.setOnClickListener(this);
        btChemistry2.setOnClickListener(this);
        btLifeScience1.setOnClickListener(this);
        btLifeScience2.setOnClickListener(this);
        btEarthScience1.setOnClickListener(this);
        btEarthScience2.setOnClickListener(this);
        btDone.setOnClickListener(onClickListener);


        tvTitleMathType = (TextView) view.findViewById(R.id.tvTitleMathType);
        tvTitleArtMusicPhysicalEducation = (TextView) view.findViewById(R.id.tvTitleArtMusicPhysicalEducation);

        tvTitleEngineeringCollege = (TextView) view.findViewById(R.id.tvTitleEngineeringCollege);
        tvTitleCollegeOfHumanEcology = (TextView) view.findViewById(R.id.tvTitleCollegeOfHumanEcology);
        tvTitleNaturalScienceCollege = (TextView) view.findViewById(R.id.tvTitleNaturalScienceCollege);
        tvTitleCollegeOfMedicine = (TextView) view.findViewById(R.id.tvTitleCollegeOfMedicine);
        tvTitleCollegeOfEducation = (TextView) view.findViewById(R.id.tvTitleCollegeOfEducation);
        tvTitleCollegeOfFineArt = (TextView) view.findViewById(R.id.tvTitleCollegeOfFineArt);
        tvTitleArtMusicPhysicalEducationSchool = (TextView) view.findViewById(R.id.tvTitleArtMusicPhysicalEducationSchool);

        tvTitleSecondLanguage = (TextView) view.findViewById(R.id.tvTitleSecondLanguage);
        tvSecondLanguage = (TextView) view.findViewById(R.id.tvSecondLanguage);
        tvSecondLanguage.setText("");
        tvMajor = (TextView) view.findViewById(R.id.tvMajor);
        tvSocialScience = (TextView) view.findViewById(R.id.tvSocialScience);

        setupTitleMajor();
        btDone.setEnabled(false);

        return view;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cbMathTypeA:
                isChooseMathTypeA = isChecked;
                if (isChooseMathTypeA && isChooseMathTypeB) {
                    cbMathTypeB.setChecked(false);
                    isChooseMathTypeB = false;
                }
                setupTitleMathType();
                break;
            case R.id.cbMathTypeB:
                isChooseMathTypeB = isChecked;
                if (isChooseMathTypeA && isChooseMathTypeB) {
                    cbMathTypeA.setChecked(false);
                    isChooseMathTypeA = false;
                }
                setupTitleMathType();
                break;
            case R.id.cbArtMusicPhysicalEducation:
                isChooseArtMusicPhysicalEducation = isChecked;
                setupTitleArtMusicPhysicalEducation();
                break;
            case R.id.cbSecondLanguage:
                isChooseSecondLanguage = isChecked;
                if (isChooseSecondLanguage){
                    DialogUtils.showChooseSecondLanguageDialog(getContext(),this);
                }
                setupTitleSecondLanguage();
                break;
            case R.id.cbEngineerOfCollege:
                isChooseEngineerOfCollege = isChecked;
                setupTitleEngineerOfCollege();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfHumanEcology:
                isChooseCollegeOfHumanEcology = isChecked;
                setupTitleCollegeOfHumanEcology();
                setupTitleMajor();
                break;
            case R.id.cbNaturalScienceCollege:
                isChooseNaturalScienceCollege = isChecked;
                setupTitleNaturalScienceCollege();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfMedicine:
                isChooseCollegeOfMedicine = isChecked;
                setupTitleCollegeOfMedicine();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfEducation:
                isChooseCollegeOfEducation = isChecked;
                setupTitleCollegeOfEducation();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfFineArt:
                isChooseCollegeOfFineArt = isChecked;
                setupTitleCollegeOfFineArt();
                setupTitleMajor();
                break;
            case R.id.cbArtMusicPhysicalEducationSchool:
                isChooseArtMusicPhysicalEducationSchool = isChecked;
                setupTitleArtMusicPhysicalEducationSchool();
                setupTitleMajor();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btPhysic1:
                if (selectPhysic1) {
                    selectPhysic1 = false;
                    v.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btPhysic1.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectPhysic1 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btPhysic1.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btPhysic2:
                if (selectPhysic2) {
                    selectPhysic2 = false;
                    v.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btPhysic2.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectPhysic2 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btPhysic2.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btChemistry1:
                if (selectChemistry1) {
                    selectChemistry1 = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btChemistry1.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectChemistry1 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btChemistry1.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btChemistry2:
                if (selectChemistry2) {
                    selectChemistry2 = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btChemistry2.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectChemistry2 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btChemistry2.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btLifeScience1:
                if (selectLifeScience1) {
                    selectLifeScience1 = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btLifeScience1.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectLifeScience1 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btLifeScience1.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btLifeScience2:
                if (selectLifeScience2) {
                    selectLifeScience2 = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btLifeScience2.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectLifeScience2 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btLifeScience2.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btEarthScience1:
                if (selectEarthScience1) {
                    selectEarthScience1 = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btEarthScience1.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectEarthScience1 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btEarthScience1.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btEarthScience2:
                if (selectEarthScience2) {
                    selectEarthScience2 = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btEarthScience2.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectEarthScience2 = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btEarthScience2.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
        }

        setupSocialScience();
    }

    private void setupSocialScience() {

        if (numberSubject >= 2) {
            tvSocialScience.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvSocialScience.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    @Override
    public void onSelectSecondLanguage(int languageIndex) {
        if (languageIndex!= -1){
            tvSecondLanguage.setText(Constants.arraySecondLanguage[languageIndex]);
            foreign_languageValue = Constants.arraySecondLanguageID[languageIndex];
        }else{
            isChooseSecondLanguage = false;
            cbSecondLanguage.setChecked(false);
            setupTitleSecondLanguage();
        }
    }

    private void setupTitleMathType() {
        if (isChooseMathTypeA || isChooseMathTypeB) {
            tvTitleMathType.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleMathType.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleArtMusicPhysicalEducation() {
        if (isChooseArtMusicPhysicalEducation) {
            tvTitleArtMusicPhysicalEducation.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleArtMusicPhysicalEducation.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleEngineerOfCollege() {
        if (isChooseEngineerOfCollege) {
            tvTitleEngineeringCollege.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleEngineeringCollege.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleCollegeOfEducation() {
        if (isChooseCollegeOfEducation) {
            tvTitleCollegeOfEducation.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfEducation.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleCollegeOfHumanEcology() {
        if (isChooseCollegeOfHumanEcology) {
            tvTitleCollegeOfHumanEcology.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfHumanEcology.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleNaturalScienceCollege() {
        if (isChooseNaturalScienceCollege) {
            tvTitleNaturalScienceCollege.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleNaturalScienceCollege.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleCollegeOfMedicine() {
        if (isChooseCollegeOfMedicine) {
            tvTitleCollegeOfMedicine.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfMedicine.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleCollegeOfFineArt() {
        if (isChooseCollegeOfFineArt) {
            tvTitleCollegeOfFineArt.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfFineArt.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleArtMusicPhysicalEducationSchool() {
        if (isChooseArtMusicPhysicalEducationSchool) {
            tvTitleArtMusicPhysicalEducationSchool.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleArtMusicPhysicalEducationSchool.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleSecondLanguage() {
        if (isChooseSecondLanguage) {
            tvTitleSecondLanguage.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleSecondLanguage.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
            tvSecondLanguage.setText("");
            foreign_languageValue = 0;
        }
    }

    private void setupTitleMajor() {

        if (isChooseEngineerOfCollege ||
                isChooseCollegeOfEducation ||
                isChooseNaturalScienceCollege ||
                isChooseCollegeOfHumanEcology ||
                isChooseCollegeOfFineArt ||
                isChooseArtMusicPhysicalEducationSchool ||
                isChooseCollegeOfMedicine) {
            tvMajor.setTextColor(getResources().getColor(R.color.black));
            isChooseMajor = true;
        } else {
            tvMajor.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
            isChooseMajor = false;
        }

        setStatusButtonComplete();
    }

    private void setStatusButtonComplete() {
        if ((isChooseMathTypeB || isChooseMathTypeA) /*&& (numberSubject >= 2) && isChooseArtMusicPhysicalEducation */&& isChooseMajor) {
            btDone.setEnabled(true);
            btDone.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
        } else {
            btDone.setEnabled(false);
            btDone.setBackgroundColor(getResources().getColor(R.color.bg_button_gray));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            math_typeValue = cbMathTypeA.isChecked() ? 1 : 2;
            mp_educationValue = cbArtMusicPhysicalEducation.isChecked();
            getOptionSubject();
            if (optional_subjectsValue.length()>0){
                optional_subjectsValue = optional_subjectsValue.substring(1, optional_subjectsValue.length());
            }

            getTargetDepartments();
            if (target_departmentValue.length()>0){
                target_departmentValue = target_departmentValue.substring(1, target_departmentValue.length());
            }


            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setAction(Constants.ACTION_REGISTER_NATURAL_STUDENT);

            intent.putExtra(Constants.DEPARTMENT_KEY, 2);
            intent.putExtra(Constants.MATH_TYPE_KEY, math_typeValue);
            intent.putExtra(Constants.OPTIONAL_SUBJECTS_KEY, optional_subjectsValue);
            intent.putExtra(Constants.FOREIGN_LANGUAGE_KEY, foreign_languageValue);
            intent.putExtra(Constants.MP_EDUCATION_KEY, mp_educationValue);
            intent.putExtra(Constants.TARGET_DEPARTMENTS_KEY, target_departmentValue);

            getContext().sendBroadcast(intent);

            resetData();
        }
    };

    private void resetData(){
        optional_subjectsValue = "";
        math_typeValue = 1;
        target_departmentValue = "";
        foreign_languageValue = 0;
        mp_educationValue = false;
        numberSubject = 0;
    }


    private void getOptionSubject() {
        if (selectPhysic1) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[0];
        }
        if (selectPhysic2) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[1];
        }
        if (selectChemistry1) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[2];
        }
        if (selectChemistry2) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[3];
        }
        if (selectLifeScience1) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[4];
        }
        if (selectLifeScience2) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[5];
        }
        if (selectEarthScience1) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[6];
        }
        if (selectEarthScience2) {
            optional_subjectsValue += "," + Constants.arraySubjectNaturalID[7];
        }

    }

    private void getTargetDepartments(){

        if (isChooseEngineerOfCollege) {
            target_departmentValue += ",9";
        }
        if (isChooseCollegeOfHumanEcology) {
            target_departmentValue += ",10";
        }
        if (isChooseCollegeOfHumanEcology) {
            target_departmentValue += ",11";
        }
        if (isChooseNaturalScienceCollege) {
            target_departmentValue += ",12";
        }
        if (isChooseCollegeOfMedicine) {
            target_departmentValue += ",13";
        }
        if (isChooseCollegeOfEducation) {
            target_departmentValue += ",14";
        }
        if (isChooseCollegeOfFineArt) {
            target_departmentValue += ",15";
        }

    }

    /**
     * Setup ScrollView in ScrollView
     */
    private void setupScrollView() {
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                childScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        childScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
}
