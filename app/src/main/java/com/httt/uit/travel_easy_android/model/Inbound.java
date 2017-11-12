package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Inbound {
    private Flights[] flights;

    public Flights[] getFlights ()
    {
        return flights;
    }

    public void setFlights (Flights[] flights)
    {
        this.flights = flights;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [flights = "+flights+"]";
    }
}
