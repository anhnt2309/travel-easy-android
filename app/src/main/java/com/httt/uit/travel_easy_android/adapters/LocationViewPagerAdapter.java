package com.httt.uit.travel_easy_android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.fragment.LocationExtraInputFragment;
import com.httt.uit.travel_easy_android.fragment.LocationInputFragment;

/**
 * Created by TuanAnh on 11/2/17.
 */

public class LocationViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public LocationViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LocationInputFragment.newInstance("Fragment 1", R.drawable.arrow_chart,R.drawable.avatar_fred);
            case 1:
                return LocationInputFragment.newInstance("Fragment 2", R.drawable.arrow_chart,R.drawable.avatar_fred);
            default:
                return null;
        }
    }

}
