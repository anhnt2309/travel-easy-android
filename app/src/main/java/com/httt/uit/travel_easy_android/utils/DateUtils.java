package com.httt.uit.travel_easy_android.utils;

import android.icu.util.DateInterval;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by TuanAnh on 11/4/17.
 */

public class DateUtils {
    private static String dayVns[] = {"Chủ Nhật","Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};

    public static String getDay(Date date) {
        int dayIndex = date.getDay();
//        if (dayIndex == 0) {
//            dayIndex = 6;
//        }
//        if (dayIndex != 0 ) {
//            dayIndex = dayIndex - 1;
//        }

        return dayVns[dayIndex];
    }

    public static String getDayMonth(Date date) {
        int month = date.getMonth() + 1;
        int year = date.getYear() - 100 + 2000;
        return date.getDate() + " tháng " + month + ", " + year;
    }

    public static String getDateDisplay(Date date) {
        int month = date.getMonth() + 1;
        String dateVn = "";
        dateVn += getDay(date) + ", ";
        dateVn += date.getDate() + " tháng " + month;
        return dateVn;
    }

    public static String getSearchDate(Date date) {
        String returnDate = (date.getYear() - 100 + 2000) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        return returnDate;
    }

    public static Date parseResultDate(String dateT) {
        String date = dateT.substring(0, dateT.indexOf("T"));
        String time = dateT.substring(dateT.indexOf("T"), dateT.length());
        return parseDateTime(dateT);
    }

    public static Date parseDateTime(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String durationBetween2DateTime(Date depart, Date arrive) {

        long difference = arrive.getTime() - depart.getTime();
//        if(difference<0)
//        {
//            Date dateMax = simpleDateFormat.parse("24:00");
//            Date dateMin = simpleDateFormat.parse("00:00");
//            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
//        }
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);


//        long duration = arrive.getTime() - depart.getTime();
//        long minute = TimeUnit.MILLISECONDS.toMicros(duration);
//        long hour = TimeUnit.MILLISECONDS.toHours(duration);
        String durationString = "";
        if (hours > 0)
            durationString += hours + " Giờ ";
        durationString += min + " Phút";
        return durationString;

    }

}
