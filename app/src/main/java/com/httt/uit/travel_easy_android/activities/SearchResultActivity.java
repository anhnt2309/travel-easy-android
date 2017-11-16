package com.httt.uit.travel_easy_android.activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.adapters.SearchResultRecyclerviewAdapter;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.FlightClass;
import com.httt.uit.travel_easy_android.model.FlightResults;
import com.httt.uit.travel_easy_android.model.Itineraries;
import com.httt.uit.travel_easy_android.model.Results;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.Date;

import me.grantland.widget.AutofitTextView;

/**
 * Created by TuanAnh on 11/13/17.
 */

public class SearchResultActivity extends AppCompatActivity {
    private TextView tvOriginCode;
    private TextView tvDesCode;
    private AutofitTextView tvOriginAirport;
    private AutofitTextView tvDesAirport;
    private TextView tvDepartDate;
    private TextView tvReturnDate;
    private TextView tvClass;
    private TextView tvCurrency;
    private TextView tvType;
    private TextView tvAdult;
    private TextView tvChildren;
    private TextView tvInfant;
    private RecyclerView rvResult;
    private LinearLayout mGrpReturn;
    private ShineButton sbBack;
    private ShineButton sbFilter;
    private RelativeLayout grpFilter;
    private LinearLayout grpBtnApplyFilter;

    private AutoCompleteAirport mOriginAirport;
    private AutoCompleteAirport mDestinationAirport;
    private Date mDepartDate;
    private Date mReturnDate;
    private FlightClass mClass;
    private String mCurrency;
    private int mNoAdult;
    private int mNoChildren;
    private int mNoInfant;
    private int mType;
    private FlightResults mFlightResults;
    ArrayList<Itineraries> itinerariesArrayList;
    ArrayList<Itineraries> nonStopitineraries;
    ArrayList<Itineraries> hasStopitineraries;

    private SearchResultRecyclerviewAdapter mResultAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initUI();
        getData();
        fillUI();

        resolveResultModel(mFlightResults);
        mResultAdapter = new SearchResultRecyclerviewAdapter(this, itinerariesArrayList, mType, mCurrency);
        rvResult.setAdapter(mResultAdapter);
        rvResult.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initUI() {
        tvOriginCode = (TextView) findViewById(R.id.tv_result_origin_code);
        tvDesCode = (TextView) findViewById(R.id.tv_result_des_code);
        tvOriginAirport = (AutofitTextView) findViewById(R.id.tv_result_origin_airport);
        tvDesAirport = (AutofitTextView) findViewById(R.id.tv_result_des_airport);

        tvDepartDate = (TextView) findViewById(R.id.tv_result_depart_date);
        tvReturnDate = (TextView) findViewById(R.id.tv_result_return_date);

        tvClass = (TextView) findViewById(R.id.tv_class);
        tvCurrency = (TextView) findViewById(R.id.tv_currency);
        tvType = (TextView) findViewById(R.id.tv_type);

        tvAdult = (TextView) findViewById(R.id.tv_adults);
        tvChildren = (TextView) findViewById(R.id.tv_childrens);
        tvInfant = (TextView) findViewById(R.id.tv_infants);
        grpFilter = (RelativeLayout) findViewById(R.id.grpFilter);
        sbFilter = (ShineButton) findViewById(R.id.shine_button_filter);
        sbFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sbFilter.setChecked(false);
                filterAnimation(grpFilter, grpFilter);
            }
        });

        grpBtnApplyFilter = (LinearLayout) findViewById(R.id.grp_filter_apply);
        grpBtnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterAnimationReverse(grpFilter, grpFilter);
            }
        });
        rvResult = (RecyclerView) findViewById(R.id.rv_results);
        mGrpReturn = (LinearLayout) findViewById(R.id.grpReturn);
        sbBack = (ShineButton) findViewById(R.id.shine_button_back);
        sbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 600);
            }
        });
    }

    private void fillUI() {
        //Sanity check
        if (mOriginAirport == null)
            return;
        if (mDestinationAirport == null)
            return;
        if (mDepartDate == null)
            return;
        if (mClass == null)
            return;
        if (mFlightResults == null)
            return;

        //airport info
        tvOriginCode.setText(mOriginAirport.value);
        tvDesCode.setText(mDestinationAirport.value);
        tvOriginAirport.setText(mOriginAirport.airport_name);
        tvDesAirport.setText(mDestinationAirport.airport_name);

        //date info
        String departDate = DateUtils.getDateDisplay(mDepartDate);
        tvDepartDate.setText(departDate);
        if (mReturnDate != null) {
            mGrpReturn.setVisibility(View.VISIBLE);
            String returnDate = DateUtils.getDateDisplay(mReturnDate);
            tvReturnDate.setText(returnDate);
        }
        if (mReturnDate == null)
            mGrpReturn.setVisibility(View.GONE);

        //detail
        tvClass.setText(mClass.getClassVn());
        tvCurrency.setText(mCurrency);
        if (mType == MainActivity.DEFAULT_ROUND_TRIP)
            tvType.setText(getResources().getString(R.string.round_trip));
        if (mType == MainActivity.DEFAULT_ONE_WAY)
            tvType.setText(getResources().getString(R.string.one_way));

        tvAdult.setText("" + mNoAdult);
        tvChildren.setText("" + mNoChildren);
        tvInfant.setText("" + mNoInfant);
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent == null)
            return;

        String originAirportString = intent.getStringExtra(MainActivity.ORIGIN_AIRPORT_MODEL);
        if (originAirportString.isEmpty())
            return;
        mOriginAirport = new Gson().fromJson(originAirportString, AutoCompleteAirport.class);


        String destinationAirportString = intent.getStringExtra(MainActivity.DESTINATION_AIRPORT_MODEL);
        if (originAirportString.isEmpty())
            return;
        mDestinationAirport = new Gson().fromJson(destinationAirportString, AutoCompleteAirport.class);


        String departDateString = intent.getStringExtra(MainActivity.DEPART_DATE_MODEL);
        if (departDateString.isEmpty())
            return;
        mDepartDate = new Gson().fromJson(departDateString, Date.class);


        String returnDateString = intent.getStringExtra(MainActivity.RETURN_DATE_MODEL);
        if (returnDateString.isEmpty())
            return;
        mReturnDate = new Gson().fromJson(returnDateString, Date.class);


        String classString = intent.getStringExtra(MainActivity.FLIGHT_CLASS_MODEL);
        if (classString.isEmpty())
            return;
        mClass = new Gson().fromJson(classString, FlightClass.class);

        String resultString = intent.getStringExtra(MainActivity.FLIGHT_RESULT_MODEL);
        if (resultString.isEmpty())
            return;
        mFlightResults = new Gson().fromJson(resultString, FlightResults.class);


        mNoAdult = intent.getIntExtra(MainActivity.FLIGHT_ADULT_STRING, -1);


        mNoChildren = intent.getIntExtra(MainActivity.FLIGHT_CHILDREN_STRING, -1);

        mNoInfant = intent.getIntExtra(MainActivity.FLIGHT_INFANT_STRING, -1);

        mType = intent.getIntExtra(MainActivity.FLIGHT_TYPE_STRING, -1);


        mCurrency = intent.getStringExtra(MainActivity.FLIGHT_CURRENCY_STRING);

    }

    public void filterAnimation(View view, ViewGroup layoutMain) {
//        // Could optimize by reusing a temporary Rect instead of allocating a new one
//        Rect bounds = new Rect();
//        view.getDrawingRect(bounds);
//        int x = bounds.centerX();
//        int y = bounds.centerY();


        int x = (view.getRight() + view.getLeft()) / 2;
        int y = view.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        }

        view.setVisibility(View.VISIBLE);
        anim.setDuration(800);
        anim.start();
    }

    public void filterAnimationReverse(final View view, ViewGroup layoutMain) {
        int x = (view.getRight() + view.getLeft()) / 2;
        int y = view.getBottom();

        int startRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());
        int endRadius = 0;

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        }

        view.setVisibility(View.VISIBLE);
        anim.setDuration(800);
        anim.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                });
            }
        },800);

    }

    public void resolveResultModel(FlightResults flightResults) {
        itinerariesArrayList = new ArrayList<>();
        nonStopitineraries = new ArrayList<>();
        hasStopitineraries = new ArrayList<>();

        if (flightResults.getResults().size() == 0)
            return;
        for (Results results : flightResults.getResults()) {
            for (Itineraries itineraries : results.getItineraries()) {
                itineraries.fare = results.getFare();
                itinerariesArrayList.add(itineraries);
                if (mType == MainActivity.DEFAULT_ROUND_TRIP) {
                    if (itineraries.getInbound().getFlights().size() == 1 && itineraries.getOutbound().getFlights().size() == 1)
                        nonStopitineraries.add(itineraries);
                    if (itineraries.getInbound().getFlights().size() > 1 || itineraries.getOutbound().getFlights().size() > 1)
                        hasStopitineraries.add(itineraries);
                }
                if (mType == MainActivity.DEFAULT_ONE_WAY) {
                    if (itineraries.getOutbound().getFlights().size() == 1)
                        nonStopitineraries.add(itineraries);
                    if (itineraries.getOutbound().getFlights().size() > 1)
                        hasStopitineraries.add(itineraries);
                }
            }
        }

        Log.e("_x", flightResults.getCurrency());
    }
}
