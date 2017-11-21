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

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.gson.Gson;
import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.adapters.SearchResultRecyclerviewAdapter;
import com.httt.uit.travel_easy_android.manager.CacheManager;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.FlightClass;
import com.httt.uit.travel_easy_android.model.FlightResults;
import com.httt.uit.travel_easy_android.model.Itineraries;
import com.httt.uit.travel_easy_android.model.Results;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.mahfa.dnswitch.DayNightSwitch;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.Date;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import io.feeeei.circleseekbar.CircleSeekBar;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
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

    private CircleSeekBar seekBarDepartFrom;
    private CircleSeekBar seekBarDepartTo;
    private CircleSeekBar seekBarReturnFrom;
    private CircleSeekBar seekBarReturnTo;
    private TextView tvFilterDepartTimeFrom;
    private TextView tvFilterDepartTimeTo;
    private TextView tvFilterReturnTimeFrom;
    private TextView tvFilterReturnTimeTo;
    private int mDepartTimeFrom;
    private int mDepartTimeTo;
    private int mReturnTimeFrom;
    private int mReturnTimeTo;

    //filter screen
    private DayNightSwitch dnsNonstop;
    private DayNightSwitch dnsHasTop;
    private TextView tvNonstopFlights;
    private TextView tvHasStopFlights;
    private RadioRealButtonGroup rbgPrice;
    private TextView tvTotalFlights;
    private LinearLayout grpReturnTime;


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
    private int mPriceId = R.id.rb_all_money;
    private boolean isRoundTrip = true;
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
        fillFlightsNumber();
        mResultAdapter = new SearchResultRecyclerviewAdapter(this, itinerariesArrayList, mType, mCurrency);

        rvResult.setAdapter(getRecyclerViewAdapterAnimatior(mResultAdapter));
        rvResult.setLayoutManager(new LinearLayoutManager(this));

        displayScreenResultGuide();

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
        grpReturnTime = (LinearLayout) findViewById(R.id.grpTimeReturn);

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
                displayScreenFilterGuide();
            }
        });

        grpBtnApplyFilter = (LinearLayout) findViewById(R.id.grp_filter_apply);
        grpBtnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Itineraries> filteredArray = getFilterArray();
                if (filteredArray == null) {
                    filterAnimationReverse(grpFilter, grpFilter);
                    return;
                }
                mResultAdapter = new SearchResultRecyclerviewAdapter(SearchResultActivity.this, filteredArray, mType, mCurrency);
                rvResult.setAdapter(getRecyclerViewAdapterAnimatior(mResultAdapter));
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

        //filter screen
        tvTotalFlights = (TextView) findViewById(R.id.tv_total_flight);
        tvHasStopFlights = (TextView) findViewById(R.id.tv_hasstop_detail);
        tvNonstopFlights = (TextView) findViewById(R.id.tv_nonstop_detail);


        dnsHasTop = (DayNightSwitch) findViewById(R.id.switch_hasstop);
        dnsNonstop = (DayNightSwitch) findViewById(R.id.switch_nonstop);
        rbgPrice = (RadioRealButtonGroup) findViewById(R.id.rbg_price);
        rbgPrice.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                mPriceId = button.getId();
            }
        });

        seekBarDepartFrom = (CircleSeekBar) findViewById(R.id.seek_depart_time_from);
        seekBarDepartTo = (CircleSeekBar) findViewById(R.id.seek_depart_time_to);
        seekBarReturnFrom = (CircleSeekBar) findViewById(R.id.seek_return_time_from);
        seekBarReturnTo = (CircleSeekBar) findViewById(R.id.seek_return_time_to);

        tvFilterDepartTimeFrom = (TextView) findViewById(R.id.tv_depart_time_from);
        tvFilterDepartTimeTo = (TextView) findViewById(R.id.tv_depart_time_to);
        tvFilterReturnTimeFrom = (TextView) findViewById(R.id.tv_return_time_from);
        tvFilterReturnTimeTo = (TextView) findViewById(R.id.tv_return_time_to);
        initSeekBarEvent();

    }

    public void fillFlightsNumber() {
        if (itinerariesArrayList == null || nonStopitineraries == null || hasStopitineraries == null)
            return;
        tvTotalFlights.setText(getResources().getString(R.string.filter_total_found, itinerariesArrayList.size()));
        tvNonstopFlights.setText(getResources().getString(R.string.filter_count, nonStopitineraries.size()));
        tvHasStopFlights.setText(getResources().getString(R.string.filter_count, hasStopitineraries.size()));
    }

    private ArrayList<Itineraries> getFilterArray() {
        if (!dnsNonstop.isNight() && dnsHasTop.isNight()) {
            return getFilteredArrayWithCondition(nonStopitineraries, mPriceId, mDepartTimeFrom, mDepartTimeTo, mReturnTimeFrom, mReturnTimeTo);
        }
        if (!dnsHasTop.isNight() && dnsNonstop.isNight()) {
            return getFilteredArrayWithCondition(hasStopitineraries, mPriceId, mDepartTimeFrom, mDepartTimeTo, mReturnTimeFrom, mReturnTimeTo);
        }
        if (!dnsHasTop.isNight() && !dnsNonstop.isNight() || dnsHasTop.isNight() && dnsNonstop.isNight()) {
//            return itinerariesArrayList;
            return getFilteredArrayWithCondition(itinerariesArrayList, mPriceId, mDepartTimeFrom, mDepartTimeTo, mReturnTimeFrom, mReturnTimeTo);
        }
        return null;
    }

    public ArrayList<Itineraries> getFilteredArrayWithCondition(ArrayList<Itineraries> array, int priceId, int departFrom, int departTo, int returnFrom, int returnTo) {
        ArrayList<Itineraries> resultArray = new ArrayList<>();

        if (priceId == R.id.rb_all_money) {
            if (departFrom == departTo && returnFrom == returnTo)
                resultArray.addAll(array);
        }

        for (Itineraries itineraries : array) {
            double priceDB = Double.parseDouble(itineraries.fare.getTotal_price());
            int priceDb = (int) priceDB;
            Date departDate = DateUtils.parseDateTime(itineraries.getOutbound().getFlights().get(0).getDeparts_at());
            Date returnDate = null;
            if (itineraries.getInbound() != null)
                returnDate = DateUtils.parseDateTime(itineraries.getInbound().getFlights().get(0).getDeparts_at());

            if (priceId == R.id.rb_little_money) {
                if (priceDb <= 1000000) {
                    datefilter(itineraries, departFrom, departTo, returnFrom, returnTo, departDate, returnDate, resultArray);
                }
            }

            if (priceId == R.id.rb_medium_money) {
                if (priceDb > 1000000 && priceDb < 3000000) {
                    resultArray.add(itineraries);
                    datefilter(itineraries, departFrom, departTo, returnFrom, returnTo, departDate, returnDate, resultArray);
                }
            }

            if (priceId == R.id.rb_large_money) {
                if (priceDb >= 3000000) {
                    resultArray.add(itineraries);
                    datefilter(itineraries, departFrom, departTo, returnFrom, returnTo, departDate, returnDate, resultArray);
                }
            }
            if (priceId == R.id.rb_all_money) {
                if (departDate.getHours() >= departFrom && departDate.getHours() <= departTo)
                    resultArray.add(itineraries);
                if (returnDate != null)
                    if (returnDate.getHours() >= returnFrom && returnDate.getHours() <= returnTo)
                        resultArray.add(itineraries);
            }

        }

        return resultArray;
    }

    public void datefilter(Itineraries itineraries, int departFrom, int departTo, int returnFrom, int returnTo,
                           Date departDate, Date returnDate, ArrayList<Itineraries> resultArray) {
        if (departFrom == departTo && returnFrom == returnTo)
            resultArray.add(itineraries);
        if (departDate.getHours() >= departFrom && departDate.getHours() <= departTo)
            resultArray.add(itineraries);
        if (returnDate != null)
            if (returnDate.getHours() >= returnFrom && returnDate.getHours() <= returnTo)
                resultArray.add(itineraries);
    }

    private void initSeekBarEvent() {
        //event
        seekBarDepartFrom.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar circleSeekBar, int i) {
                onFilterTimeChange(true, i, seekBarDepartTo.getCurProcess());

                if (i > seekBarDepartTo.getCurProcess())
                    seekBarDepartTo.setCurProcess(i);

            }
        });

        seekBarDepartTo.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar circleSeekBar, int i) {
                onFilterTimeChange(true, seekBarDepartFrom.getCurProcess(), i);
                if (i < seekBarDepartFrom.getCurProcess())
                    seekBarDepartFrom.setCurProcess(i);

            }
        });

        seekBarReturnFrom.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar circleSeekBar, int i) {
                onFilterTimeChange(false, i, seekBarReturnTo.getCurProcess());
                if (i > seekBarReturnTo.getCurProcess())
                    seekBarReturnTo.setCurProcess(i);
            }
        });

        seekBarReturnTo.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar circleSeekBar, int i) {
                onFilterTimeChange(false, seekBarReturnFrom.getCurProcess(), i);
                if (i < seekBarReturnFrom.getCurProcess())
                    seekBarReturnFrom.setCurProcess(i);
            }
        });


    }

    private void onFilterTimeChange(boolean isDepart, int from, int to) {
        String fromStr = from > 9 ? from + "" + ": 00" : "0" + from + ": 00";
        String toStr = to > 9 ? to + "" + ": 00" : "0" + to + ": 00";
        if (isDepart) {
            mDepartTimeFrom = from;
            mDepartTimeTo = to;
            tvFilterDepartTimeFrom.setText(fromStr);
            tvFilterDepartTimeTo.setText(toStr);
            return;
        } else {
            mReturnTimeFrom = from;
            mReturnTimeTo = to;
            tvFilterReturnTimeFrom.setText(fromStr);
            tvFilterReturnTimeTo.setText(toStr);
            return;
        }


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
        }, 800);

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
                    isRoundTrip = true;
                    grpReturnTime.setVisibility(View.VISIBLE);
                    if (itineraries.getInbound().getFlights().size() == 1 && itineraries.getOutbound().getFlights().size() == 1)
                        nonStopitineraries.add(itineraries);
                    if (itineraries.getInbound().getFlights().size() > 1 || itineraries.getOutbound().getFlights().size() > 1)
                        hasStopitineraries.add(itineraries);
                }
                if (mType == MainActivity.DEFAULT_ONE_WAY) {
                    isRoundTrip = false;
                    grpReturnTime.setVisibility(View.GONE);
                    if (itineraries.getOutbound().getFlights().size() == 1)
                        nonStopitineraries.add(itineraries);
                    if (itineraries.getOutbound().getFlights().size() > 1)
                        hasStopitineraries.add(itineraries);
                }
            }
        }

        Log.e("_x", flightResults.getCurrency());
    }

    public void displayScreenResultGuide() {
        String isShown = CacheManager.getStringCacheData(MainActivity.GUIDE_SCREEN_RESULT);
        if (isShown != null)
            return;
        new TapTargetSequence(SearchResultActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.grpResultInfo), "Thông tin tìm kiếm chuyến bay sẽ được hiển thị ở đây. ")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.shine_button_filter), "Bấm vào đây để lọc kết quả chuyến bay.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.page1)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.temp_layout), "Các chuyến bay tìm thấy sẽ được hiển thị ở đây. Bấm vào một chuyến bay để xem chi tiết.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.shine_button_back), "Bấm vào đây để trở về màn hình tìm chuyến bay.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.page1)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                        CacheManager.saveStringCacheData(MainActivity.GUIDE_SCREEN_RESULT, MainActivity.GUIDE_SHOWN_VALUE);
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
    }

    public void displayScreenFilterGuide() {
        String isShown = CacheManager.getStringCacheData(MainActivity.GUIDE_SCREEN_FILTER);
        if (isShown != null)
            return;
        new TapTargetSequence(SearchResultActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.tv_total_flight), "Số lượng chuyến bay tìm thấy sẽ được hiển thị ở đây. ")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.grpNonStopFilter), "Lọc các chuyến bay thẳng.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.grpHasStopFilter), "Lọc các chuyến bay có trạm dừng.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.rbg_price), "Lọc giá chuyến bay tại đây.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.grpTimeDepart), "Lọc chuyến bay theo khoảng thời gian cất cánh đi.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.grpTimeReturn), "Lọc chuyến bay theo khoảng thời gian cất cánh về.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.grp_filter_apply), "Bấm vào đây để xem kết quả các chuyến bay theo các điều kiện lọc ở trên.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                        CacheManager.saveStringCacheData(MainActivity.GUIDE_SCREEN_FILTER, MainActivity.GUIDE_SHOWN_VALUE);
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
    }

    public ScaleInAnimationAdapter getRecyclerViewAdapterAnimatior(SearchResultRecyclerviewAdapter resultAdapter) {
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(resultAdapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        return scaleInAnimationAdapter;
    }

}
