package com.httt.uit.travel_easy_android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Results implements Serializable {
    private Fare fare;

    private ArrayList<Itineraries> itineraries;

    public Fare getFare ()
    {
        return fare;
    }

    public void setFare (Fare fare)
    {
        this.fare = fare;
    }

    public ArrayList<Itineraries> getItineraries ()
    {
        return itineraries;
    }

    public void setItineraries (ArrayList<Itineraries> itineraries)
    {
        this.itineraries = itineraries;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [fare = "+fare+", itineraries = "+itineraries+"]";
    }
}
