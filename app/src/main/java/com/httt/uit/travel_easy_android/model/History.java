package com.httt.uit.travel_easy_android.model;

/**
 * Created by hoqua on 11/19/2017.
 */

public class History {
    private String value;
    private String airport;
    private String City_name;

    public History(){

    }
    public History(String a,String b,String c){
        this.value=a;
        this.airport=b;
        this.City_name=c;
    }
    public String getValue(){
        return this.value;
    }
    public String getAirport(){
        return this.airport;
    }
    public  String getCity_name(){
        return this.City_name;
    }
}
