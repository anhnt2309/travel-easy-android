package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class FlightResults {
    private Results[] results;

    private String currency;

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
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
