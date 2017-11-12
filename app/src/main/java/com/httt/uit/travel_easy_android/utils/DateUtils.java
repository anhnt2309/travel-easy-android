package com.httt.uit.travel_easy_android.utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TuanAnh on 11/4/17.
 */

public class DateUtils {
    private static String dayVns[] = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};

    public static String getDay(Date date) {
        int dayIndex = date.getDay();
        if (dayIndex == 0) {
            dayIndex = 6;
        }
        if (dayIndex != 0) {
            dayIndex = dayIndex - 1;
        }

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
}
