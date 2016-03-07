package com.trams.azit.common;

/**
 * Created by Administrator on 04/01/2016.
 */
public class Constants {
    public static final boolean DEBUG = true;

    public static final String[] arraySecondLanguage ={"중국어","일본어","베트남어","아랍어","독일어","스페인어","프랑스어","러시아어","한문"};
    public static final int[] arraySecondLanguageID ={80100,80200,80300,80400,80500,80600,80700,80800,80900};
    public static final String[] arraySubjectLiberalID ={"60100","60200","60300","60400","60500","60600","60700","60800","60900","61000"};
    public static final String[] arraySubjectNaturalID ={"70100","70200","70300","70400","70500","70600","70700","70800"};
    public static final String[] arrayKoreanTier ={"1등급","2등급","3등급","4등급","5등급","6등급","7등급","8등급","9등급","잘 모르겠다"};

    /*KEY FOR STUDENT JOIN*/
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String NAME_KEY = "name";
    public static final String NICKNAME_KEY = "nickname";
    public static final String PHONE_KEY = "phone";
    public static final String EDU_INST_ID_KEY = "edu_inst_id";
    public static final String YEAR_KEY = "year";
    public static final String GENDER_KEY = "gender";
    public static final String PARENT_NAME_KEY = "parent_name";
    public static final String PARENT_PHONE_KEY = "parent_phone";
    public static final String DEPARTMENT_KEY = "department";
    public static final String MATH_TYPE_KEY = "math_type";
    public static final String OPTIONAL_SUBJECTS_KEY = "optional_subjects";
    public static final String FOREIGN_LANGUAGE_KEY = "foreign_language";
    public static final String MP_EDUCATION_KEY = "mp_education";
    public static final String TARGET_DEPARTMENTS_KEY = "target_departments";

    /*ACTION_REGISTER STUDENT*/
    public static final String ACTION_REGISTER_LIBERAL_STUDENT = "com.trams.azit.liberal";
    public static final String ACTION_REGISTER_NATURAL_STUDENT = "com.trams.azit.natural";

    public static Boolean IS_LOGIN = false;
}
