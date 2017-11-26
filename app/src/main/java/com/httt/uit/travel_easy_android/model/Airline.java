package com.httt.uit.travel_easy_android.model;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/23/17.
 */

public class Airline implements Serializable {
    public String code;
    public String name;
    public String website;
    public String country;
    public String countryImage;

    public Airline(String code, String name, String website, String country, String countryImage) {
        this.code = code;
        this.name = name;
        this.website = website;
        this.country = country;
        this.countryImage = countryImage;
    }
}
