package com.trams.azit.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 19/01/2016.
 */
public class DateTimeUtils {
    private static final String TAG = DateTimeUtils.class.getName();
    private static final String DATE_FORMAT = "yyyy-MM-dd";


    // 00:00:00
    public static long getSecondsFromDate(String date) {
        long result = 0;

        String[] arr = date.split(":");

        result = Integer.valueOf(arr[0]) * 3600 + Integer.valueOf(arr[1]) * 60 + Integer.valueOf(arr[2]);

        LogUtils.d(TAG, "getSecondsFromDate , result : " + result);

        return result;
    }

    public static String getDateFromSeconds(long seconds) {
        String result = "";

        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int secondStr = (int) ((seconds % 3600) % 60);

        result = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", secondStr);

        LogUtils.d(TAG, "getDateFromSeconds , result : " + result);
        return result;
    }

    public static String getToday() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        String result = dateFormat.format(date);
        return result;
    }

    public static String getNextDate(String curDate) {

        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            Date date = format.parse(curDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPreviousDate(String curDate) {

        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            Date date = format.parse(curDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


}
