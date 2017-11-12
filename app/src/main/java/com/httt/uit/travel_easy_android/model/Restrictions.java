package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Restrictions {
    private String refundable;

    private String change_penalties;

    public String getRefundable ()
    {
        return refundable;
    }

    public void setRefundable (String refundable)
    {
        this.refundable = refundable;
    }

    public String getChange_penalties ()
    {
        return change_penalties;
    }

    public void setChange_penalties (String change_penalties)
    {
        this.change_penalties = change_penalties;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [refundable = "+refundable+", change_penalties = "+change_penalties+"]";
    }
}
