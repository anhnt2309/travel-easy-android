package com.httt.uit.travel_easy_android.model;

import java.io.Serializable;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Price_per_child implements Serializable {
    private String tax;

    private String total_fare;

    public String getTax ()
    {
        return tax;
    }

    public void setTax (String tax)
    {
        this.tax = tax;
    }

    public String getTotal_fare ()
    {
        return total_fare;
    }

    public void setTotal_fare (String total_fare)
    {
        this.total_fare = total_fare;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tax = "+tax+", total_fare = "+total_fare+"]";
    }
}
