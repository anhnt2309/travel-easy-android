package com.httt.uit.travel_easy_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.Fare;
import com.httt.uit.travel_easy_android.model.Flights;
import com.httt.uit.travel_easy_android.model.Restrictions;
import com.httt.uit.travel_easy_android.utils.StringUtils;
import com.joanzapata.iconify.widget.IconTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by TuanAnh on 11/25/17.
 */

public class FareFragment extends Fragment {

    private ObservableScrollView scrollView;
    private TextView title;
    private Fare mModel;

    private TextView tvTotalPrice;
    private IconTextView itvAdultNumber;
    private IconTextView itvChildrenNumber;
    private IconTextView itvInfantsNumber;
    private TextView tvAdultPrice;
    private TextView tvChildrenPrice;
    private TextView tvInfantPrice;
    private IconTextView itvRefundable;
    private IconTextView itvChangePenalties;

    private String mCurrency;
    private int mNoAdult;
    private int mNoChildren;
    private int mNoInfant;

    public FareFragment() {

    }

    public static FareFragment newInstance(String title, Fare fare, String currency, int noAdult, int noChildren, int noInfant) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("currency", currency);
        args.putInt("noAdult", noAdult);
        args.putInt("noChildren", noChildren);
        args.putInt("noInfant", noInfant);
        args.putSerializable("fare_model", fare);


        FareFragment fragment = new FareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FareFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);

        FareFragment fragment = new FareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price, container, false);
        if (mModel == null)
            this.mModel = (Fare) getArguments().getSerializable("fare_model");
        mCurrency = getArguments().getString("currency");
        mNoAdult = getArguments().getInt("noAdult");
        mNoChildren = getArguments().getInt("noChildren");
        mNoInfant = getArguments().getInt("noInfant");

        scrollView = view.findViewById(R.id.scrollView);
        title = view.findViewById(R.id.title);

        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        itvAdultNumber = view.findViewById(R.id.tv_adult_price_number);
        itvChildrenNumber = view.findViewById(R.id.tv_children_price_number);
        itvInfantsNumber = view.findViewById(R.id.tv_infant_price_number);
        tvAdultPrice = view.findViewById(R.id.tv_adult_price);
        tvChildrenPrice = view.findViewById(R.id.tv_children_price);
        tvInfantPrice = view.findViewById(R.id.tv_infants_price);
        itvRefundable = view.findViewById(R.id.tv_refundable);
        itvChangePenalties = view.findViewById(R.id.tv_change_penalties);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        title.setText(getArguments().getString("title"));

        if (mModel != null) {
            double priceDb = Double.parseDouble(mModel.getTotal_price());
            String totalPrice = StringUtils.formatPrice(priceDb, mCurrency);
            tvTotalPrice.setText(totalPrice);
            if (mModel.getPrice_per_adult() != null) {
                double priceAdult = Double.parseDouble(mModel.getPrice_per_adult().getTotal_fare());
                String adultPrice = StringUtils.formatPrice(priceAdult, mCurrency);
                tvAdultPrice.setText(adultPrice);
            }

            if (mModel.getPrice_per_child() != null) {
                double priceChildren = Double.parseDouble(mModel.getPrice_per_child().getTotal_fare());
                String adultChildren = StringUtils.formatPrice(priceChildren, mCurrency);
                tvChildrenPrice.setText(adultChildren);
            }

            if (mModel.getPrice_per_infant() != null) {
                double priceInfant = Double.parseDouble(mModel.getPrice_per_infant().getTotal_fare());
                String adultInfant = StringUtils.formatPrice(priceInfant, mCurrency);
                tvInfantPrice.setText(adultInfant);
            }

            String noAdult = getResources().getString(R.string.txt_time_number, mNoAdult);
            String noChildren = getResources().getString(R.string.txt_time_number, mNoChildren);
            String noInfant = getResources().getString(R.string.txt_time_number, mNoInfant);

            itvAdultNumber.setText(noAdult);
            itvChildrenNumber.setText(noChildren);
            itvInfantsNumber.setText(noInfant);

            Restrictions restrictions = mModel.getRestrictions();

            if (restrictions.getRefundable().equals("false"))
                itvRefundable.setText(getResources().getString(R.string.txt_no));

            else
                itvRefundable.setText(getResources().getString(R.string.txt_yes));


            if (restrictions.getChange_penalties().equals("false"))
                itvChangePenalties.setText(getResources().getString(R.string.txt_no));
            else
                itvChangePenalties.setText(getResources().getString(R.string.txt_yes));

        }


        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }
}
