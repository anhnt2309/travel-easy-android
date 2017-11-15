package com.httt.uit.travel_easy_android.model;

import java.util.Date;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Flights {
    public Fare fare;

    private String marketing_airline;

    private String aircraft;

    private Origin origin;

    private String flight_number;

    private String operating_airline;

    private String arrives_at;

    private Booking_info booking_info;

    private Destination destination;

    private String departs_at;

    public Date arrives_date;

    public Date departs_date;

    public String getMarketing_airline() {
        return marketing_airline;
    }

    public void setMarketing_airline(String marketing_airline) {
        this.marketing_airline = marketing_airline;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getOperating_airline() {
        return operating_airline;
    }

    public void setOperating_airline(String operating_airline) {
        this.operating_airline = operating_airline;
    }

    public String getArrives_at() {
        return arrives_at;
    }

    public void setArrives_at(String arrives_at) {
        this.arrives_at = arrives_at;
    }

    public Booking_info getBooking_info() {
        return booking_info;
    }

    public void setBooking_info(Booking_info booking_info) {
        this.booking_info = booking_info;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getDeparts_at() {
        return departs_at;
    }

    public void setDeparts_at(String departs_at) {
        this.departs_at = departs_at;
    }

    @Override
    public String toString() {
        return "ClassPojo [marketing_airline = " + marketing_airline + ", aircraft = " + aircraft + ", origin = " + origin + ", flight_number = " + flight_number + ", operating_airline = " + operating_airline + ", arrives_at = " + arrives_at + ", booking_info = " + booking_info + ", destination = " + destination + ", departs_at = " + departs_at + "]";
    }
}
