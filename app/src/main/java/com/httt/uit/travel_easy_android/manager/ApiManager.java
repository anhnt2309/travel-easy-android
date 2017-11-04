package com.httt.uit.travel_easy_android.manager;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.httt.uit.travel_easy_android.request.BaseRequest;
import com.httt.uit.travel_easy_android.request.MyDataCallback;

import java.lang.reflect.Type;
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
}
