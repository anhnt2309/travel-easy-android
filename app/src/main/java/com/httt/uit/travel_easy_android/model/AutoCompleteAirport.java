package com.httt.uit.travel_easy_android.model;


import java.io.Serializable;


/**
 * Created by TuanAnh on 11/5/17.
 */

public class AutoCompleteAirport implements Serializable{
    public String value;
    public String label;
    public String city_name;
    public String airport_name;
    public boolean hasCityName = true;

    public double distance;
    public double latitude;
    public double longitude;
    public String airport;


    public void setValue(String value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setHasCityName(boolean hasCityName) {
        this.hasCityName = hasCityName;
    }

    public void setCityName(String cityName) {
        this.city_name = cityName;
    }

    public void setAirportName(String airportName) {
        this.airport_name = airportName;
    }

    public AutoCompleteAirport(String value, String label) {
        this.value = value;
        this.label = label;
    }
    public AutoCompleteAirport(String a,String b,String c){
        this.value=a;
        this.airport_name=b;
        this.city_name=c;
    }
    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
    public String getAirport_name(){return this.airport_name;};
    public String getCity_name(){return this.city_name;};
}
