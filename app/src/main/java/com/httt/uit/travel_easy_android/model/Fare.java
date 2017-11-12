package com.httt.uit.travel_easy_android.model;

/**
 * Created by TuanAnh on 11/12/17.
 */

public class Fare {

    private Price_per_infant price_per_infant;

    private Restrictions restrictions;

    private Price_per_adult price_per_adult;

    private Price_per_child price_per_child;

    private String total_price;

    public Price_per_infant getPrice_per_infant ()
    {
        return price_per_infant;
    }

    public void setPrice_per_infant (Price_per_infant price_per_infant)
    {
        this.price_per_infant = price_per_infant;
    }

    public Restrictions getRestrictions ()
    {
        return restrictions;
    }

    public void setRestrictions (Restrictions restrictions)
    {
        this.restrictions = restrictions;
    }

    public Price_per_adult getPrice_per_adult ()
    {
        return price_per_adult;
    }

    public void setPrice_per_adult (Price_per_adult price_per_adult)
    {
        this.price_per_adult = price_per_adult;
    }

    public Price_per_child getPrice_per_child ()
    {
        return price_per_child;
    }

    public void setPrice_per_child (Price_per_child price_per_child)
    {
        this.price_per_child = price_per_child;
    }

    public String getTotal_price ()
    {
        return total_price;
    }

    public void setTotal_price (String total_price)
    {
        this.total_price = total_price;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [price_per_infant = "+price_per_infant+", restrictions = "+restrictions+", price_per_adult = "+price_per_adult+", price_per_child = "+price_per_child+", total_price = "+total_price+"]";
    }
}
