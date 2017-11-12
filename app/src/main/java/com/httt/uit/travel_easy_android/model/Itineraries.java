package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Itineraries {
    private Outbound outbound;

    private Inbound inbound;

    public Outbound getOutbound ()
    {
        return outbound;
    }

    public void setOutbound (Outbound outbound)
    {
        this.outbound = outbound;
    }

    public Inbound getInbound ()
    {
        return inbound;
    }

    public void setInbound (Inbound inbound)
    {
        this.inbound = inbound;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [outbound = "+outbound+", inbound = "+inbound+"]";
    }
}
