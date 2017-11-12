package com.httt.uit.travel_easy_android.manager;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.request.BaseRequest;
import com.httt.uit.travel_easy_android.request.MyDataCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TuanAnh on 11/3/17.
 */

public class ApiManager extends BaseRequest {

    public static void getLocationInfo(final Context context, String locationCode, MyDataCallback<JsonElement> myCallback) {
        if (locationCode == null || locationCode.equals(""))
            return;

        Type type = new TypeToken<JsonElement>() {
        }.getType();
        String url = "location/" + locationCode;
        GET(context, url, null, type, null, myCallback);
    }

    public static void getAutoCompleteAirport(final Context context, String text, String countryCode, MyDataCallback<ArrayList<AutoCompleteAirport>> myCallback) {
        if (text == null || text.equals(""))
            return;
        HashMap<String, Object> params = new HashMap<>();
        if (countryCode != null)
            params.put("country", countryCode);
        params.put("term", text);


        Type type = new TypeToken<ArrayList<AutoCompleteAirport>>() {
        }.getType();
        String url = "airports/autocomplete";
        GET(context, url, params, type, null, myCallback);
    }

    public static void getNearestAirports(final Context context, double lat, double lg, MyDataCallback<ArrayList<AutoCompleteAirport>> myCallback) {
        if (lat == 0 || lg == 0)
            return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("latitude", lat);
        params.put("longitude", lg);


        Type type = new TypeToken<ArrayList<AutoCompleteAirport>>() {
        }.getType();
        String url = "airports/nearest-relevant";
        GET(context, url, params, type, null, myCallback);
    }

    public static void getSearchResult(final Context context, double lat, double lg, MyDataCallback<ArrayList<AutoCompleteAirport>> myCallback) {
        if (lat == 0 || lg == 0)
            return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("latitude", lat);
        params.put("longitude", lg);


        Type type = new TypeToken<ArrayList<AutoCompleteAirport>>() {
        }.getType();
        String url = "airports/nearest-relevant";
        GET(context, url, params, type, null, myCallback);
    }


}
