package com.httt.uit.travel_easy_android.model;

import com.httt.uit.travel_easy_android.R;

/**
 * Created by TuanAnh on 11/3/17.
 */

public enum FlightClass {
    ecoClass("ECONOMY", "Economy Class", "Hạng Phổ Thông", R.drawable.ic_economy),
    preEcoClass("PREMIUM_ECONOMY","Premium Economy Class", "Hạng Phổ Thông Đặc Biệt", R.drawable.ic_premium_economy),
    bussClass("BUSINESS ","Bussiness Class", "Hạng Thương Gia", R.drawable.ic_bussiness),
    fiClass("FIRST","First Class", "Hạng Nhất", R.drawable.ic_first_class);

    private String classSearch;
    private String classEng;
    private String classVn;
    private int icon;


    FlightClass(String classSearch, String classEng, String classVn, int icon) {
        this.classSearch = classSearch;
        this.classEng = classEng;
        this.classVn = classVn;
        this.icon = icon;
    }

    public String getClassSearch() {
        return classSearch;
    }

    public String getClassEng() {
        return classEng;
    }

    public String getClassVn() {
        return classVn;
    }

    public int getIcon() {
        return icon;
    }

    public void setClassEng(String classEng) {
        this.classEng = classEng;
    }

    public void setClassVn(String classVn) {
        this.classVn = classVn;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
