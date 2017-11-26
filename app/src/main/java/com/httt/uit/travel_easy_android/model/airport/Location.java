package com.httt.uit.travel_easy_android.model.airport;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/25/17.
 */

public class Location implements Serializable {
    private String longitude;

    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ClassPojo [longitude = " + longitude + ", latitude = " + latitude + "]";
    }
}
