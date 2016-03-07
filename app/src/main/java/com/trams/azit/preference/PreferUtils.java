package com.trams.azit.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 */
public class PreferUtils {

    private static final String PREFER_AZIT_NAME = "Azit";

    private static final String PREFER_IS_LOGIN = "is_login";
    private static final String PREFER_IS_STR = "login";
    private static final String PREFER_USER_ID = "user_id";
    private static final String PREFER_SECRET = "secret";
    private static final String PREFER_GCM_TOKEN = "gcm_token";

    private static final String PREFER_PUSH_SETTING = "setting_push";

    private static final String PREFER_IS_SHOW_POPUP_CHART_START = "is_show_popup";
    private static final String PREFER_WEEK_POPUP = "week_show_popup";
    private static final String PREFER_STUDENT_ID = "student_id_teacher_view";


    public static void setLoginString(Context context, String isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREFER_IS_STR, isLogin);
        editor.commit();
    }

    public static String isLoginString(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_IS_STR, "false");
    }


    public static void setLogin(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_IS_LOGIN, isLogin);
        editor.commit();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_IS_LOGIN, false);
    }

    public static void setUserId(Context context, String userId) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREFER_USER_ID, userId);
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_USER_ID, "");
    }

    public static void setToken(Context context, String token) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREFER_GCM_TOKEN, token);
        editor.commit();
    }

    public static String getSecret(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_SECRET, "");
    }

    public static void setSecret(Context context, String secret) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREFER_SECRET, secret);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_GCM_TOKEN, "");
    }

    public static void setPushSetting(Context context, boolean isPush) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_SETTING, isPush);
        editor.commit();
    }

    public static boolean getPushSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_SETTING, true);
    }

    public static void setIsShowPopup(Context context, boolean isShow) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_IS_SHOW_POPUP_CHART_START, isShow);
        editor.commit();
    }

    public static boolean getIsShowPopup(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_IS_SHOW_POPUP_CHART_START, true);
    }

    public static void setWeekShowPopup(Context context, int week) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_WEEK_POPUP, week);
        editor.commit();
    }

    public static int getWeekShowPopup(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_WEEK_POPUP, 0);
    }

    public static int getStudentTeacher(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_STUDENT_ID, 0);
    }

    public static void setStudentTeacher(Context context, int studentId) {
        Editor editor = context.getSharedPreferences(PREFER_AZIT_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_STUDENT_ID, studentId);
        editor.commit();
    }

}