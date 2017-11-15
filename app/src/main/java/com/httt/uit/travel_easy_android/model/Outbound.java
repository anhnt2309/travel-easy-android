package com.httt.uit.travel_easy_android.model;

import java.util.ArrayList;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Outbound {
    private ArrayList<Flights> flights;

    public ArrayList<Flights> getFlights ()
    {
        return flights;
    }

    public void setFlights (ArrayList<Flights> flights)
    {
        this.flights = flights;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [flights = "+flights+"]";
    }
}
