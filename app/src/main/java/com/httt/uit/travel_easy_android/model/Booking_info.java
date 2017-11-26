package com.httt.uit.travel_easy_android.model;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Booking_info implements Serializable{
    private String travel_class;

    private String seats_remaining;

    private String booking_code;

    public String getTravel_class ()
    {
        return travel_class;
    }

    public void setTravel_class (String travel_class)
    {
        this.travel_class = travel_class;
    }

    public String getSeats_remaining ()
    {
        return seats_remaining;
    }

    public void setSeats_remaining (String seats_remaining)
    {
        this.seats_remaining = seats_remaining;
    }

    public String getBooking_code ()
    {
        return booking_code;
    }

    public void setBooking_code (String booking_code)
    {
        this.booking_code = booking_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [travel_class = "+travel_class+", seats_remaining = "+seats_remaining+", booking_code = "+booking_code+"]";
    }
}
