package com.httt.uit.travel_easy_android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class FlightResults implements Serializable {
    private ArrayList<Results> results;

    private String currency;

    public ArrayList<Results> getResults ()
    {
        return results;
    }

    public void setResults (ArrayList<Results> results)
    {
        this.results = results;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+", currency = "+currency+"]";
    }
}
