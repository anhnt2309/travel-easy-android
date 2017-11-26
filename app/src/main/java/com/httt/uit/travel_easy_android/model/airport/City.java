package com.httt.uit.travel_easy_android.model.airport;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/25/17.
 */

public class City implements Serializable {
    private String timezone;

    private Location location;

    private String name;

    private String state;

    private String code;

    private String geonames_ID;

    private String currency;

    private String country;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGeonames_ID() {
        return geonames_ID;
    }

    public void setGeonames_ID(String geonames_ID) {
        this.geonames_ID = geonames_ID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "ClassPojo [timezone = " + timezone + ", location = " + location + ", name = " + name + ", state = " + state + ", code = " + code + ", geonames_ID = " + geonames_ID + ", currency = " + currency + ", country = " + country + "]";
    }
}
