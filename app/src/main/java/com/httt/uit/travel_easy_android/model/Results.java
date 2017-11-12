package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Results {
    private Fare fare;

    private Itineraries[] itineraries;

    public Fare getFare ()
    {
        return fare;
    }

    public void setFare (Fare fare)
    {
        this.fare = fare;
    }

    public Itineraries[] getItineraries ()
    {
        return itineraries;
    }

    public void setItineraries (Itineraries[] itineraries)
    {
        this.itineraries = itineraries;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [fare = "+fare+", itineraries = "+itineraries+"]";
    }
}
