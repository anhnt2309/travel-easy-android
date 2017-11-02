package com.httt.uit.travel_easy_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.httt.uit.travel_easy_android.R;

/**
 * Created by TuanAnh on 11/2/17.
 */

public class LocationExtraInputFragment extends Fragment {
    public static LocationExtraInputFragment newInstance(String title, int resMainImage, int resSecondaryImage) {
        LocationExtraInputFragment fragment = new LocationExtraInputFragment();
        Bundle args = new Bundle();
        args.putInt("imageMain", resMainImage);
        args.putInt("imageSecondary", resSecondaryImage);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        imageMain = getArguments().getInt("imageMain", 0);
//        imageSecondary = getArguments().getInt("imageSecondary", 0);
//        title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airport_quick_input,container,false);
        return view;
    }
}
