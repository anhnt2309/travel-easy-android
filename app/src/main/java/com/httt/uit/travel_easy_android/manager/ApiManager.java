package com.httt.uit.travel_easy_android.manager;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.FlightResults;
import com.httt.uit.travel_easy_android.model.airport.AirportInfo;
import com.httt.uit.travel_easy_android.request.BaseRequest;
import com.httt.uit.travel_easy_android.request.MyDataCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;


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

    public static void getSearchResult(final Context context, String origin, String destination, String departure_date, String return_date, int adults, int children, int infants, String currency, String travel_class, MyDataCallback<FlightResults> myCallback) {
        if (origin.isEmpty() || destination.isEmpty() || departure_date.isEmpty())
            return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("departure_date", departure_date);
        if (!return_date.isEmpty())
            params.put("return_date", return_date);
        params.put("adults", adults);
        params.put("children", children);
        params.put("infants", infants);
        params.put("currency", currency);
        params.put("travel_class", travel_class);


        Type type = new TypeToken<FlightResults>() {
        }.getType();
        String url = "flights/low-fare-search";
        GET(context, url, params, type, null, myCallback);
    }

    public static void getAircraft(final Context context, String code, MyDataCallback<Response> myCallback) {
        if (code == null)
            return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("hex", code);


        Type type = new TypeToken<Response>() {
        }.getType();
        String url = "hex-type.php";
        String baseUrl = "https://ae.roplan.es/api/";
        GETEXTERNAL(context, url, baseUrl, params, type, null, myCallback);
    }

    public static void getAirportInfo(final Context context, String code, MyDataCallback<AirportInfo> myCallback) {

        Type type = new TypeToken<AirportInfo>() {
        }.getType();
        String url = "location/" + code;
        GET(context, url, null, type, null, myCallback);
    }

}
