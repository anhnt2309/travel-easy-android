package com.httt.uit.travel_easy_android.model.airport;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/25/17.
 */

public class Airports implements Serializable {
    private String timezone;

    private String city_name;

    private Location location;

    private String name;

    private String state;

    private String aircraft_movements;

    private String city_code;

    private String code;

    private String country;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAircraft_movements() {
        return aircraft_movements;
    }

    public void setAircraft_movements(String aircraft_movements) {
        this.aircraft_movements = aircraft_movements;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "ClassPojo [timezone = " + timezone + ", city_name = " + city_name + ", location = " + location + ", name = " + name + ", state = " + state + ", aircraft_movements = " + aircraft_movements + ", city_code = " + city_code + ", code = " + code + ", country = " + country + "]";
    }
}
