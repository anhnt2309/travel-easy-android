package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Destination {
    private String airport;

    private String terminal;

    public String getAirport ()
    {
        return airport;
    }

    public void setAirport (String airport)
    {
        this.airport = airport;
    }

    public String getTerminal ()
    {
        return terminal;
    }

    public void setTerminal (String terminal)
    {
        this.terminal = terminal;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [airport = "+airport+", terminal = "+terminal+"]";
    }
}
