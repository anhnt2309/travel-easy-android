package com.httt.uit.travel_easy_android.request;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nguyetquoi on 12/16/16.
 */

public class JsonHelper {
    /**
     * @param context
     * @param myCallback
     * @param type
     * @param dataWrapperElement ("data" is default)
     * @param logTagClass
     * @param logTagMethod
     */
    public static Callback<JsonElement> callbackJsonElement(final Context context,
                                                            final MyDataCallback myCallback,
                                                            final Type type,
                                                            final String dataWrapperElement,
                                                            final String logTagClass,
                                                            final String logTagMethod) {
        Callback<JsonElement> callbackJsonElement = new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                // wrong data, callback null
                if (call == null) {
                    if (myCallback != null)
                        myCallback.success(null);

                    return;
                }

                // parse json and callback result
                if (response.isSuccessful()) {
                    if (type == null) {
                        if (myCallback != null)
                            myCallback.success(response.body());
                    } else {
                        JsonHelper.jsonParsingHelper(context, response.body(), myCallback, type, dataWrapperElement, logTagClass, logTagMethod);
                    }
                    return;
                }

                // wrong data, callback null
                if (myCallback != null)
                    myCallback.success(null);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (myCallback != null)
                    t.printStackTrace();
                myCallback.failure(t);
            }
        };

        return callbackJsonElement;
    }

    private static void jsonParsingHelper(final Context context,
                                          final JsonElement jsonElement,
                                          final MyDataCallback myCallback,
                                          final Type type,
                                          final String dataWrapperElement,
                                          final String logTagClass,
                                          final String logTagMethod) {

        if (myCallback == null)
            return;

        // run in background thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                // init gson
                final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

                Object object = null;

                try {
                    JsonElement theJsonElement = jsonElement;

                    //Check if our data model is wrapped inside dataWrapperElement or "data" element
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        // try to get dataWrapperElement, "data" is default
                        String finalDataElement;
                        if (dataWrapperElement == null || dataWrapperElement.isEmpty() || dataWrapperElement.toUpperCase().equals("NULL"))
                            finalDataElement = "data";
                        else
                            finalDataElement = dataWrapperElement;

                        JsonElement dataJsonElement = jsonObject.get(finalDataElement);
                        if (dataJsonElement != null)
                            theJsonElement = dataJsonElement;
                    }

                    object = gson.fromJson(theJsonElement, type);

                } catch (Exception e) {
                    String message = e.getMessage();
                    Log.e(logTagMethod, " error: " + message);
                    object = null;
                }

                //Unable to parse the object
                if (object == null) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myCallback.success(null);
                            }
                        });
                    } else {
                        myCallback.success(null);
                    }
                    return;
                }

                final Object finalObject = object;
                final Class finalClass = object.getClass();
                final Object castedObject = finalClass.cast(finalObject);

                // callback on current thread (background thread)
                if (context == null || !(context instanceof Activity)) {
                    myCallback.success(castedObject);
                    return;
                }

                // callback on UI thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myCallback.success(castedObject);
                    }
                });

            }
        }).start();
    }
}
