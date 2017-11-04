package com.httt.uit.travel_easy_android.request;

/**
 * Created by nguyetquoi on 12/16/16.
 */

public interface MyDataCallback<T> {
    void success(T t);

    void failure(Throwable t);
}
