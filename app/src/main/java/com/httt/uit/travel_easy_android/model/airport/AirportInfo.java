package com.httt.uit.travel_easy_android.model.airport;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/25/17.
 */

public class AirportInfo implements Serializable {
    private Airports[] airports;

    private City city;

    public Airports[] getAirports() {
        return airports;
    }

    public void setAirports(Airports[] airports) {
        this.airports = airports;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ClassPojo [airports = " + airports + ", city = " + city + "]";
    }
}
