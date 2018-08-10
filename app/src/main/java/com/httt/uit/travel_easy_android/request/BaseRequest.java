package com.httt.uit.travel_easy_android.request;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.manager.CacheManager;
import com.httt.uit.travel_easy_android.model.APIKey;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;


public class BaseRequest {
    public static final String LOG_TAG = "BaseRequest";

    private static final int DEVELOPMENT = 1;
    private static final int PRODUCTION = 2;
    public static final String API_KEY = "jQI2IynUGOTAubqo3W1MjRX7HD0ye1oC";

    //java.lang.IllegalArgumentException: baseUrl must end in /
    private static final String BASE_SERVER = "https://api.sandbox.amadeus.com/v1.2/";
    private static final String LOW_FARE_URL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search";
    private static final String AIRPORT_AUTOCOMPELTE_URL = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete";
    private static final String NEAREST_AIRPORT_URL = "https://api.sandbox.amadeus.com/v1.2/airports/nearest-relevant";
    private static final String LOCATION_INFO_URL = "https://api.sandbox.amadeus.com/v1.2/location";

    private static final String GENERIC_URL = "{url}";

    private static String getBaseUrl(boolean withPrefix) {
        StringBuilder builder = new StringBuilder();

        builder.append(BASE_SERVER);


        return builder.toString();
    }

    public interface BaseRequestInterface {
        @GET(GENERIC_URL)
        Call<JsonElement> getGeneric(
                @Path(value = "url", encoded = true) String path,
                @QueryMap Map<String, Object> options);

        @GET(GENERIC_URL)
        Call<ResponseBody> getRaw(
                @Path(value = "url", encoded = true) String path,
                @QueryMap Map<String, Object> options);

        @PUT(GENERIC_URL)
        Call<JsonElement> putGeneric(
                @QueryMap Map<String, Object> options,
                @Path(value = "url", encoded = true) String path);

        @POST(GENERIC_URL)
        Call<JsonElement> postGeneric(
                @Body Map<String, Object> options,
                @Path(value = "url", encoded = true) String path);

        @DELETE(GENERIC_URL)
        Call<JsonElement> deleteGeneric(
                @QueryMap Map<String, Object> options,
                @Path(value = "url", encoded = true) String path);
    }


    public static BaseRequestInterface getBaseRequestInterface(String url) {
        //Auto-detect if we should use prefix or not
        boolean usePrefix = !url.startsWith("/");
        if (!usePrefix)
            url.substring(1);

        String baseUrl = BaseRequest.getBaseUrl(usePrefix);
        HashMap<String, String> headers = getStandardHeaderParameters();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        BaseRequestInterface baseRequestInterface = retrofit.create(BaseRequestInterface.class);
        return baseRequestInterface;
    }

    public static BaseRequestInterface getBaseRequestInterface(String url, String externalURl) {
//Auto-detect if we should use prefix or not
        boolean usePrefix = !url.startsWith("/");
        if (!usePrefix)
            url.substring(1);

        String baseUrl = externalURl;
        HashMap<String, String> headers = getStandardHeaderParameters();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        BaseRequestInterface baseRequestInterface = retrofit.create(BaseRequestInterface.class);
        return baseRequestInterface;
    }


    private static Callback getTheCallBack(final Context context, final Type theType, String dataWrapperElement, final MyDataCallback myCallback) {
        String functionName = Thread.currentThread().getStackTrace()[3].getMethodName();
        return JsonHelper.callbackJsonElement(context, myCallback, theType, dataWrapperElement, LOG_TAG, functionName);
    }

    private static Map<String, Object> getFinalParam(Map<String, Object> params) {
        Map<String, Object> finalParams = getStandardParameters();
        if (params != null && params.size() > 0)
            finalParams.putAll(params);
        return finalParams;
    }

    /**
     * Common method to construct auth header parameters
     */
    private static HashMap<String, String> getStandardHeaderParameters() {
        HashMap<String, String> params = new HashMap<>();

//        String accessToken = CredentialManager.getRememberedToken();
//        if (accessToken != null)
//            params.put("secret", accessToken);
//
//        Number userId = UserSessionDataManager.getCurrentUserID();
//        if (userId != null)
//            params.put("userid", "" + userId);
//
//        Context context = AppUtils.getAppContext();
//        String deviceUuid = DeviceUtil.deviceId(context);
//        if (StringUtils.isNotNull(deviceUuid))
//            params.put("deviceId", deviceUuid);

        //Return null if there is no parameters
        if (params.size() <= 0)
            params = null;
        return params;
    }

    /**
     * Convenient function to perform a HTTP GET
     */
    protected static void GET(final Context context, String url, HashMap<String, Object> params, final Type theType, String dataWrapperElement, final MyDataCallback myCallback) {
        HashMap<String, Object> finalParams = getStandardParameters();
        if (params != null && params.size() > 0)
            finalParams.putAll(params);


        if (params == null)
            params = new HashMap<>();


        String functionName = Thread.currentThread().getStackTrace()[3].getMethodName();

        retrofit2.Callback<JsonElement> theCallback = JsonHelper.callbackJsonElement(context, myCallback, theType, dataWrapperElement, LOG_TAG, functionName);

        BaseRequestInterface requestInterface = getBaseRequestInterface(url);
        Call<JsonElement> theCall = requestInterface.getGeneric(url, finalParams);
        theCall.enqueue(theCallback);
    }

    protected static void GETEXTERNAL(final Context context, String url, String baseUrl, HashMap<String, Object> params, final Type theType, String dataWrapperElement, final MyDataCallback myCallback) {
        HashMap<String, Object> finalParams = getStandardParameters();
        if (params != null && params.size() > 0)
            finalParams.putAll(params);


        if (params == null)
            params = new HashMap<>();


        String functionName = Thread.currentThread().getStackTrace()[3].getMethodName();

        retrofit2.Callback<ResponseBody> theCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                myCallback.success(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                myCallback.failure(t);
            }
        };

        BaseRequestInterface requestInterface = getBaseRequestInterface(url, baseUrl);
        Call<ResponseBody> theCall = requestInterface.getRaw(url, finalParams);
        theCall.enqueue(theCallback);
    }

    /**
     * Convenient function to perform a HTTP PUT
     */
    protected static void PUT(final Context context, final String url, HashMap<String, Object> params, final Type theType, String dataWrapperElement, final MyDataCallback myCallback) {
        HashMap<String, Object> finalParams = getStandardParameters();
        if (params != null && params.size() > 0)
            finalParams.putAll(params);

        String functionName = Thread.currentThread().getStackTrace()[3].getMethodName();

        Callback<JsonElement> theCallback = JsonHelper.callbackJsonElement(context, myCallback, theType, dataWrapperElement, LOG_TAG, functionName);

        BaseRequestInterface requestInterface = getBaseRequestInterface(url);
        Call<JsonElement> theCall = requestInterface.putGeneric(finalParams, url);
        theCall.enqueue(theCallback);
    }

    /**
     * Convenient function to perform a HTTP POST
     */
    protected static void POST(final Context context, final String url, Map<String, Object> params, final Type theType, String dataWrapperElement, final MyDataCallback myCallback) {
        HashMap<String, Object> finalParams = getStandardParameters();
        if (params != null && params.size() > 0)
            finalParams.putAll(params);

        String functionName = Thread.currentThread().getStackTrace()[3].getMethodName();

        Callback<JsonElement> theCallback = JsonHelper.callbackJsonElement(context, myCallback, theType, dataWrapperElement, LOG_TAG, functionName);

        BaseRequestInterface requestInterface = getBaseRequestInterface(url);
        Call<JsonElement> theCall = requestInterface.postGeneric(finalParams, url);
        theCall.enqueue(theCallback);
    }

    /**
     * Convenient function to perform a HTTP POST
     */
    protected static void DELETE(final Context context, final String url, HashMap<String, Object> params, final Type theType, String dataWrapperElement, final MyDataCallback myCallback) {
        HashMap<String, Object> finalParams = getStandardParameters();
        if (params != null && params.size() > 0)
            finalParams.putAll(params);

        String functionName = Thread.currentThread().getStackTrace()[3].getMethodName();

        Callback<JsonElement> theCallback = JsonHelper.callbackJsonElement(context, myCallback, theType, dataWrapperElement, LOG_TAG, functionName);

        BaseRequestInterface requestInterface = getBaseRequestInterface(url);
        Call<JsonElement> theCall = requestInterface.deleteGeneric(finalParams, url);
        theCall.enqueue(theCallback);
    }

    /**
     * Common method to construct standard parameters such as device parameters
     */
    private static HashMap<String, Object> getStandardParameters() {

        HashMap<String, Object> params = new HashMap<>();
        APIKey keys = CacheManager.getObjectCacheData(MainActivity.APIKEY_KEY, APIKey.class);
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(keys.key1);
        keyList.add(keys.key2);
        keyList.add(keys.key3);
        keyList.add(keys.key4);

        Random rand = new Random();
        String key = keyList.get(rand.nextInt(3));

        params.put("apikey", key);
//        params.put("os", "android");
////        params.put("app_ver", AppUtils.getAppVersionName());
//        params.put("os_type", 2);
//        params.put("os_ver", Build.VERSION.SDK_INT);
//        params.put("model", Build.MODEL);
//        params.put("manufacturer", Build.MANUFACTURER);

//        String accessToken = CredentialManager.getRememberedToken();
//        if (accessToken != null)
//            params.put("secret", accessToken);
//
//        Context context = AppUtils.getAppContext();
//        String deviceUuid = DeviceUtil.deviceId(context);
//        if (StringUtils.isNotNull(deviceUuid))
//            params.put("deviceId", deviceUuid);

        return params;
    }


}
