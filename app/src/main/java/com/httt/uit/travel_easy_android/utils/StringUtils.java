package com.httt.uit.travel_easy_android.utils;

import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;

/**
 * Created by TuanAnh on 11/5/17.
 */

public class StringUtils {
    public static AutoCompleteAirport parseAutoCompleteModel(AutoCompleteAirport model) {
        if (model == null)
            return null;
        String label = model.label;
        String cityName = "";
        String aiportName = "";
        if (!label.contains("-")) {
            cityName = null;
            aiportName = label.substring(0, label.indexOf("["));
            ;
            model.setHasCityName(false);
        }
        if (label.contains("-")) {
            model.setHasCityName(true);
            cityName = label.substring(0, label.indexOf("-"));
            aiportName = label.substring(label.indexOf("-") + 1, label.indexOf("["));
        }
        model.setCityName(cityName);
        model.setAirportName(aiportName);
        return model;
    }

    public static String formatPrice(double amount, String currency) {
        String currencyPrefix = currency;
        if (currency.equals(MainActivity.DEFAULT_CURRENCY))
            currencyPrefix = "đ";
        return String.format("%,.0f", amount) + currencyPrefix;
    }
}
