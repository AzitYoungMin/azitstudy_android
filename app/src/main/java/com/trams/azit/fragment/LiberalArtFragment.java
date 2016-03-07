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
public class LiberalArtFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, DialogUtils.SelectSecondLanguage {
    private ScrollView parentScrollView, childScrollView;
    private TextView tvTitleMathType;
    private TextView tvTitleArtMusicPhysicalEducation;
    private TextView tvTitleLiberalArtCollege;
    private TextView tvTitleCollegeOfEducation;
    private TextView tvTitleCollegeOfLaw;
    private TextView tvTitleCollegeOfBusinessAdmin;
    private TextView tvTitleCollegeOfSocialScience;
    private TextView tvTitleCollegeOfFineArt;
    private TextView tvTitleArtMusicPhysicalEducationSchool;
    private TextView tvTitleCollegeOfMedicine;
    private TextView tvTitleSecondLanguage;
    private TextView tvSecondLanguage;
    private TextView tvMajor;
    private TextView tvSocialScience;

    private CheckBox cbMathTypeA, cbMathTypeB, cbArtMusicPhysicalEducation;
    private CheckBox cbLiberalArtCollege;
    private CheckBox cbCollegeOfEducation;
    private CheckBox cbCollegeOfLaw;
    private CheckBox cbCollegeOfBusinessAdmin;
    private CheckBox cbCollegeOfSocialScience;
    private CheckBox cbCollegeOfFineArt;
    private CheckBox cbArtMusicPhysicalEducationSchool;
    private CheckBox cbCollegeOfMedicine;
    private CheckBox cbSecondLanguage;


    private boolean isChooseMathTypeA = false;
    private boolean isChooseMathTypeB = false;
    private boolean isChooseArtMusicPhysicalEducation = false;
    private boolean isChooseLiberalArtCollege = false;
    private boolean isChooseCollegeOfEducation = false;
    private boolean isChooseCollegeOfLaw = false;
    private boolean isChooseCollegeOfBusinessAdmin = false;
    private boolean isChooseCollegeOfSocialScience = false;
    private boolean isChooseCollegeOfFineArt = false;
    private boolean isChooseArtMusicPhysicalEducationSchool = false;
    private boolean isChooseCollegeOfMedicine = false;
    private boolean isChooseSecondLanguage = false;
    private boolean isChooseMajor = false;

    private boolean selectLivingEthics = false;
    private boolean selectEasternAsiaHistory = false;
    private boolean selectEthicThought = false;
    private boolean selectWorldHistory = false;
    private boolean selectKoreanHistory = false;
    private boolean selectLawPolitic = false;
    private boolean selectKoreanGeoGraphic = false;
    private boolean selectEconomic = false;
    private boolean selectWorldGeographic = false;
    private boolean selectSocialCulture = false;

    private Button btLivingEthics;
    private Button btEasternAsiaHistory;
    private Button btEthicThought;
    private Button btWorldHistory;
    private Button btKoreanHistory;
    private Button btLawPolitic;
    private Button btKoreanGeoGraphic;
    private Button btEconomic;
    private Button btWorldGeoGraphic;
    private Button btSocialCulture;
    private Button btDone;

    private int numberSubject = 0;

    private int math_typeValue = 1;
    private boolean mp_educationValue = false;
    private String  optional_subjectsValue = "", target_departmentValue="";
    private int foreign_languageValue = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_liberal_arts, container, false);

        parentScrollView = (ScrollView) view.findViewById(R.id.scrollViewParent);
        childScrollView = (ScrollView) view.findViewById(R.id.scrollViewChild);

        setupScrollView();

        cbMathTypeA = (CheckBox) view.findViewById(R.id.cbMathTypeA);
        cbMathTypeB = (CheckBox) view.findViewById(R.id.cbMathTypeB);
        cbArtMusicPhysicalEducation = (CheckBox) view.findViewById(R.id.cbArtMusicPhysicalEducation);
        cbLiberalArtCollege = (CheckBox) view.findViewById(R.id.cbLiberalArtCollege);
        cbCollegeOfEducation = (CheckBox) view.findViewById(R.id.cbCollegeOfEducation);
        cbCollegeOfLaw = (CheckBox) view.findViewById(R.id.cbCollegeOfLaw);
        cbCollegeOfBusinessAdmin = (CheckBox) view.findViewById(R.id.cbCollegeOfBusinessAdmin);
        cbCollegeOfSocialScience = (CheckBox) view.findViewById(R.id.cbCollegeOfSocialScience);
        cbCollegeOfFineArt = (CheckBox) view.findViewById(R.id.cbCollegeOfFineArt);
        cbArtMusicPhysicalEducationSchool = (CheckBox) view.findViewById(R.id.cbArtMusicPhysicalEducationSchool);
        cbCollegeOfMedicine = (CheckBox) view.findViewById(R.id.cbCollegeOfMedicine);
        cbSecondLanguage = (CheckBox) view.findViewById(R.id.cbSecondLanguage);

        cbMathTypeA.setOnCheckedChangeListener(this);
        cbMathTypeB.setOnCheckedChangeListener(this);
        cbArtMusicPhysicalEducation.setOnCheckedChangeListener(this);
        cbLiberalArtCollege.setOnCheckedChangeListener(this);
        cbCollegeOfEducation.setOnCheckedChangeListener(this);
        cbCollegeOfLaw.setOnCheckedChangeListener(this);
        cbCollegeOfBusinessAdmin.setOnCheckedChangeListener(this);
        cbCollegeOfSocialScience.setOnCheckedChangeListener(this);
        cbCollegeOfFineArt.setOnCheckedChangeListener(this);
        cbArtMusicPhysicalEducationSchool.setOnCheckedChangeListener(this);
        cbCollegeOfMedicine.setOnCheckedChangeListener(this);
        cbSecondLanguage.setOnCheckedChangeListener(this);

        btLivingEthics = (Button) view.findViewById(R.id.btLivingEthics);
        btEasternAsiaHistory = (Button) view.findViewById(R.id.btEasternAsiaHistory);
        btEthicThought = (Button) view.findViewById(R.id.btEthicThought);
        btWorldHistory = (Button) view.findViewById(R.id.btWorldHistory);
        btKoreanHistory = (Button) view.findViewById(R.id.btKoreanHistory);
        btLawPolitic = (Button) view.findViewById(R.id.btLawPolitic);
        btKoreanGeoGraphic = (Button) view.findViewById(R.id.btKoreanGeoGraphic);
        btEconomic = (Button) view.findViewById(R.id.btEconomic);
        btWorldGeoGraphic = (Button) view.findViewById(R.id.btWorldGeoGraphic);
        btSocialCulture = (Button) view.findViewById(R.id.btSocialCulture);
        btDone = (Button) view.findViewById(R.id.btDone);

        btLivingEthics.setOnClickListener(this);
        btEasternAsiaHistory.setOnClickListener(this);
        btEthicThought.setOnClickListener(this);
        btWorldHistory.setOnClickListener(this);
        btKoreanHistory.setOnClickListener(this);
        btLawPolitic.setOnClickListener(this);
        btKoreanGeoGraphic.setOnClickListener(this);
        btEconomic.setOnClickListener(this);
        btWorldGeoGraphic.setOnClickListener(this);
        btSocialCulture.setOnClickListener(this);
        btDone.setOnClickListener(onClickListener);

        tvTitleMathType = (TextView) view.findViewById(R.id.tvTitleMathType);
        tvTitleArtMusicPhysicalEducation = (TextView) view.findViewById(R.id.tvTitleArtMusicPhysicalEducation);
        tvTitleLiberalArtCollege = (TextView) view.findViewById(R.id.tvTitleLiberalArtCollege);
        tvTitleCollegeOfEducation = (TextView) view.findViewById(R.id.tvTitleCollegeOfEducation);
        tvTitleCollegeOfLaw = (TextView) view.findViewById(R.id.tvTitleCollegeOfLaw);
        tvTitleCollegeOfBusinessAdmin = (TextView) view.findViewById(R.id.tvTitleCollegeOfBusinessAdmin);
        tvTitleCollegeOfSocialScience = (TextView) view.findViewById(R.id.tvTitleCollegeOfSocialScience);
        tvTitleCollegeOfFineArt = (TextView) view.findViewById(R.id.tvTitleCollegeOfFineArt);
        tvTitleArtMusicPhysicalEducationSchool = (TextView) view.findViewById(R.id.tvTitleArtMusicPhysicalEducationSchool);
        tvTitleCollegeOfMedicine = (TextView) view.findViewById(R.id.tvTitleCollegeOfMedicine);
        tvTitleSecondLanguage = (TextView) view.findViewById(R.id.tvTitleSecondLanguage);
        tvMajor = (TextView) view.findViewById(R.id.tvMajor);
        tvSocialScience = (TextView) view.findViewById(R.id.tvSocialScience);
        tvSecondLanguage = (TextView) view.findViewById(R.id.tvSecondLanguage);
        tvSecondLanguage.setText("");

        setupTitleMajor();
        btDone.setEnabled(false);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLivingEthics:
                if (selectLivingEthics) {
                    selectLivingEthics = false;
                    v.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btLivingEthics.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectLivingEthics = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btLivingEthics.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btEasternAsiaHistory:
                if (selectEasternAsiaHistory) {
                    selectEasternAsiaHistory = false;
                    v.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btEasternAsiaHistory.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectEasternAsiaHistory = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btEasternAsiaHistory.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btEthicThought:
                if (selectEthicThought) {
                    selectEthicThought = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btEthicThought.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectEthicThought = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btEthicThought.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btWorldHistory:
                if (selectWorldHistory) {
                    selectWorldHistory = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btWorldHistory.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectWorldHistory = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btWorldHistory.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btKoreanHistory:
                if (selectKoreanHistory) {
                    selectKoreanHistory = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btKoreanHistory.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectKoreanHistory = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btKoreanHistory.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btLawPolitic:
                if (selectLawPolitic) {
                    selectLawPolitic = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btLawPolitic.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectLawPolitic = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btLawPolitic.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btKoreanGeoGraphic:
                if (selectKoreanGeoGraphic) {
                    selectKoreanGeoGraphic = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btKoreanGeoGraphic.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectKoreanGeoGraphic = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btKoreanGeoGraphic.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btEconomic:
                if (selectEconomic) {
                    selectEconomic = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btEconomic.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectEconomic = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btEconomic.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btWorldGeoGraphic:
                if (selectWorldGeographic) {
                    selectWorldGeographic = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btWorldGeoGraphic.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject<2){
                    selectWorldGeographic = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btWorldGeoGraphic.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
            case R.id.btSocialCulture:
                if (selectSocialCulture) {
                    selectSocialCulture = false;
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btSocialCulture.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
                    numberSubject--;
                } else if (numberSubject < 2){
                    selectSocialCulture = true;
                    v.setBackgroundColor(getResources().getColor(R.color.azit_green_bg));
                    btSocialCulture.setTextColor(getResources().getColor(R.color.white));
                    numberSubject++;
                }
                break;
        }

        setupSocialScience();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
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
            case R.id.cbLiberalArtCollege:
                isChooseLiberalArtCollege = isChecked;
                setupTitleLiberalArtCollege();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfEducation:
                isChooseCollegeOfEducation = isChecked;
                setupTitleCollegeOfEducation();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfLaw:
                isChooseCollegeOfLaw = isChecked;
                setupTitleCollegeOfLaw();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfBusinessAdmin:
                isChooseCollegeOfBusinessAdmin = isChecked;
                setupTitleCollegeOfBusinessAdmin();
                setupTitleMajor();
                break;
            case R.id.cbCollegeOfSocialScience:
                isChooseCollegeOfSocialScience = isChecked;
                setupTitleCollegeOfSocialScience();
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
            case R.id.cbCollegeOfMedicine:
                isChooseCollegeOfMedicine = isChecked;
                setupTitleCollegeOfMedicine();
                setupTitleMajor();
                break;
            case R.id.cbSecondLanguage:
                isChooseSecondLanguage = isChecked;
                if (isChooseSecondLanguage) {
                    DialogUtils.showChooseSecondLanguageDialog(getContext(), this);
                }
                setupTitleSecondLanguage();
                break;
        }
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

    private void setupTitleLiberalArtCollege() {
        if (isChooseLiberalArtCollege) {
            tvTitleLiberalArtCollege.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleLiberalArtCollege.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
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

    private void setupTitleCollegeOfLaw() {
        if (isChooseCollegeOfLaw) {
            tvTitleCollegeOfLaw.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfLaw.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleCollegeOfBusinessAdmin() {
        if (isChooseCollegeOfBusinessAdmin) {
            tvTitleCollegeOfBusinessAdmin.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfBusinessAdmin.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleCollegeOfSocialScience() {
        if (isChooseCollegeOfSocialScience) {
            tvTitleCollegeOfSocialScience.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfSocialScience.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
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

    private void setupTitleCollegeOfMedicine() {
        if (isChooseCollegeOfMedicine) {
            tvTitleCollegeOfMedicine.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvTitleCollegeOfMedicine.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
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

    private void setupSocialScience() {

        if (numberSubject >= 2) {
            tvSocialScience.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvSocialScience.setTextColor(getResources().getColor(R.color.text_gray_join_student_3));
        }
        setStatusButtonComplete();
    }

    private void setupTitleMajor() {

        if (isChooseLiberalArtCollege ||
                isChooseCollegeOfEducation ||
                isChooseCollegeOfLaw ||
                isChooseCollegeOfBusinessAdmin ||
                isChooseCollegeOfSocialScience ||
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
        if ((isChooseMathTypeB || isChooseMathTypeA) /*&& (numberSubject >= 2) && isChooseArtMusicPhysicalEducation*/ && isChooseMajor) {
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
            intent.setAction(Constants.ACTION_REGISTER_LIBERAL_STUDENT);

            intent.putExtra(Constants.DEPARTMENT_KEY, 1);
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
        if (selectLivingEthics) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[0];
        }
        if (selectEasternAsiaHistory) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[1];
        }
        if (selectEthicThought) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[2];
        }
        if (selectWorldHistory) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[3];
        }
        if (selectKoreanHistory) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[4];
        }
        if (selectLawPolitic) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[5];
        }
        if (selectKoreanGeoGraphic) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[6];
        }
        if (selectEconomic) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[7];
        }
        if (selectWorldGeographic) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[8];
        }
        if (selectSocialCulture) {
            optional_subjectsValue += "," + Constants.arraySubjectLiberalID[9];
        }
    }

    private void getTargetDepartments(){

        if (isChooseArtMusicPhysicalEducation) {
            target_departmentValue += ",1";
        }
        if (isChooseLiberalArtCollege) {
            target_departmentValue += ",2";
        }
        if (isChooseCollegeOfEducation) {
            target_departmentValue += ",3";
        }
        if (isChooseCollegeOfLaw) {
            target_departmentValue += ",4";
        }
        if (isChooseCollegeOfBusinessAdmin) {
            target_departmentValue += ",5";
        }
        if (isChooseCollegeOfSocialScience) {
            target_departmentValue += ",6";
        }
        if (isChooseCollegeOfFineArt) {
            target_departmentValue += ",7";
        }
        if (isChooseArtMusicPhysicalEducationSchool) {
            target_departmentValue += ",8";
        }
    }

}
