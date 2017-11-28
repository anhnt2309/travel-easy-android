package com.httt.uit.travel_easy_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.google.gson.reflect.TypeToken;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.activities.BrowserActivity;
import com.httt.uit.travel_easy_android.activities.SearchResultDetailActivity;
import com.httt.uit.travel_easy_android.manager.ApiManager;
import com.httt.uit.travel_easy_android.manager.CacheManager;
import com.httt.uit.travel_easy_android.model.Airline;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.Flights;
import com.httt.uit.travel_easy_android.model.airport.AirportInfo;
import com.httt.uit.travel_easy_android.request.MyDataCallback;
import com.httt.uit.travel_easy_android.utils.DateUtils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;
import okhttp3.ResponseBody;
import retrofit2.Response;


import static com.httt.uit.travel_easy_android.adapters.SearchResultRecyclerviewAdapter.AIRLINE_LOGO_URL;


public class ScrollViewFragment extends Fragment {

    private ObservableScrollView scrollView;
    private TextView title;
    private TextView tvOriginCode;
    private TextView tvDestinationCode;
    private AutofitTextView tvOriginName;
    private AutofitTextView tvDestinationName;
    private TextView tvTotalTime;
    private TextView tvNumFilght;
    private View grpWaitTime;
    private TextView tvWaitTime;

    private View grpFirstFlight;
    private AutofitTextView tvFirstDepartName;
    private TextView tvFirstDepartCode;
    private TextView tvFirstDepartDate;
    private TextView tvFirstDepartTime;
    private ImageView imgFirstAirline;
    private AutofitTextView tvFirstAirlineName;
    private TextView tvFirstFlightNumber;
    private TextView tvFirstGate;
    private AutofitTextView tvFirstCountry;
    private ImageView imgFirstCountry;
    private TextView tvFirstFlighTime;
    private TextView tvFirstAircraft;
    private TextView tvFirstClass;
    private TextView tvFirstBookingCode;
    private TextView tvFirstNumSeat;
    private AutofitTextView tvFirstArriveName;
    private TextView tvFirstArriveCode;
    private TextView tvFirstArriveDate;
    private TextView tvFirstArriveTime;
    private LinearLayout btnFirstWeb;


    private View grpSecondFlight;
    private AutofitTextView tvSecondDepartName;
    private TextView tvSecondDepartCode;
    private TextView tvSecondDepartDate;
    private TextView tvSecondDepartTime;
    private ImageView imgSecondAirline;
    private AutofitTextView tvSecondAirlineName;
    private TextView tvSecondFlightNumber;
    private TextView tvSecondGate;
    private AutofitTextView tvSecondCountry;
    private ImageView imgSecondCountry;
    private TextView tvSecondFlighTime;
    private TextView tvSecondAircraft;
    private TextView tvSecondClass;
    private TextView tvSecondBookingCode;
    private TextView tvSecondNumSeat;
    private AutofitTextView tvSecondArriveName;
    private TextView tvSecondArriveCode;
    private TextView tvSecondArriveDate;
    private TextView tvSecondArriveTime;
    private LinearLayout btnSecondWeb;

    private ArrayList<Flights> mModel;
    private ArrayList<Airline> mAirlines;
    private AutoCompleteAirport mOriginAirport;
    private AutoCompleteAirport mDestinationAirport;
    private Airline firstAirline;
    private Airline secondAirline;
    private boolean isNonstop = true;

    private String mCurrency;
    private int mNoAdult;
    private int mNoChildren;
    private int mNoInfant;

    public ScrollViewFragment() {

    }

    public static ScrollViewFragment newInstance(String title, ArrayList<Flights> model, AutoCompleteAirport origin, AutoCompleteAirport des, String currency, int noAdult, int noChildren, int noInfant) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("model", model);
        args.putSerializable("origin_model", origin);
        args.putSerializable("des_model", des);

        args.putString("currency", currency);
        args.putInt("noAdult", noAdult);
        args.putInt("noChildren", noChildren);
        args.putInt("noInfant", noInfant);

        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ScrollViewFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);

        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);

        if (mModel == null)
            this.mModel = (ArrayList<Flights>) getArguments().getSerializable("model");
        if (mOriginAirport == null)
            this.mOriginAirport = (AutoCompleteAirport) getArguments().getSerializable("origin_model");
        if (mDestinationAirport == null)
            this.mDestinationAirport = (AutoCompleteAirport) getArguments().getSerializable("des_model");

        mCurrency = getArguments().getString("currency");
        mNoAdult = getArguments().getInt("noAdult");
        mNoChildren = getArguments().getInt("noChildren");
        mNoInfant = getArguments().getInt("noInfant");

        if (mModel != null)
            if (mModel.size() == 1)
                isNonstop = true;
            else
                isNonstop = false;
        mAirlines = CacheManager.getListCacheData(SearchResultDetailActivity.ARRAY_AIRLINE_KEY, new TypeToken<ArrayList<Airline>>() {
        }.getType());

        scrollView = view.findViewById(R.id.scrollView);
        title = view.findViewById(R.id.title);

        tvOriginCode = view.findViewById(R.id.tv_origin_code);
        tvDestinationCode = view.findViewById(R.id.tv_destination_code);
        tvOriginName = view.findViewById(R.id.tv_origin_name);
        tvDestinationName = view.findViewById(R.id.tv_destination_name);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        tvNumFilght = view.findViewById(R.id.tv_num_of_flight);
        grpWaitTime = view.findViewById(R.id.grpWaitTime);
        tvWaitTime = view.findViewById(R.id.tv_wait_time);

        grpFirstFlight = view.findViewById(R.id.grpLayoutOutbound);
        tvFirstDepartName = grpFirstFlight.findViewById(R.id.tv_first_orgin_name);
        tvFirstDepartCode = grpFirstFlight.findViewById(R.id.tv_first_orgin_code);
        tvFirstDepartDate = grpFirstFlight.findViewById(R.id.tv_first_depart_date);
        tvFirstDepartTime = grpFirstFlight.findViewById(R.id.tv_first_depart_time);
        imgFirstAirline = grpFirstFlight.findViewById(R.id.img_airline_logo);
        tvFirstAirlineName = grpFirstFlight.findViewById(R.id.tv_airline_name);
        tvFirstFlightNumber = grpFirstFlight.findViewById(R.id.tv_flight_number);
        tvFirstGate = grpFirstFlight.findViewById(R.id.tv_gate);
        tvFirstCountry = grpFirstFlight.findViewById(R.id.tv_country);
        imgFirstCountry = grpFirstFlight.findViewById(R.id.img_country);
        tvFirstFlighTime = grpFirstFlight.findViewById(R.id.tv_flight_duration);
        tvFirstAircraft = grpFirstFlight.findViewById(R.id.tv_aircraft);
        tvFirstClass = grpFirstFlight.findViewById(R.id.tv_class);
        tvFirstBookingCode = grpFirstFlight.findViewById(R.id.tv_booking_code);
        tvFirstNumSeat = grpFirstFlight.findViewById(R.id.tv_num_seat_left);
        tvFirstArriveName = grpFirstFlight.findViewById(R.id.tv_first_arrive_name);
        tvFirstArriveCode = grpFirstFlight.findViewById(R.id.tv_first_arrive_code);
        tvFirstArriveDate = grpFirstFlight.findViewById(R.id.tv_first_arrive_date);
        tvFirstArriveTime = grpFirstFlight.findViewById(R.id.tv_first_arrive_time);
        btnFirstWeb = grpFirstFlight.findViewById(R.id.grpWebsite);

        grpSecondFlight = view.findViewById(R.id.grpLayoutInbound);
        tvSecondDepartName = grpSecondFlight.findViewById(R.id.tv_first_orgin_name);
        tvSecondDepartCode = grpSecondFlight.findViewById(R.id.tv_first_orgin_code);
        tvSecondDepartDate = grpSecondFlight.findViewById(R.id.tv_first_depart_date);
        tvSecondDepartTime = grpSecondFlight.findViewById(R.id.tv_first_depart_time);
        imgSecondAirline = grpSecondFlight.findViewById(R.id.img_airline_logo);
        tvSecondAirlineName = grpSecondFlight.findViewById(R.id.tv_airline_name);
        tvSecondFlightNumber = grpSecondFlight.findViewById(R.id.tv_flight_number);
        tvSecondGate = grpSecondFlight.findViewById(R.id.tv_gate);
        tvSecondCountry = grpSecondFlight.findViewById(R.id.tv_country);
        imgSecondCountry = grpSecondFlight.findViewById(R.id.img_country);
        tvSecondFlighTime = grpSecondFlight.findViewById(R.id.tv_flight_duration);
        tvSecondAircraft = grpSecondFlight.findViewById(R.id.tv_aircraft);
        tvSecondClass = grpSecondFlight.findViewById(R.id.tv_class);
        tvSecondBookingCode = grpSecondFlight.findViewById(R.id.tv_booking_code);
        tvSecondNumSeat = grpSecondFlight.findViewById(R.id.tv_num_seat_left);
        tvSecondArriveName = grpSecondFlight.findViewById(R.id.tv_first_arrive_name);
        tvSecondArriveCode = grpSecondFlight.findViewById(R.id.tv_first_arrive_code);
        tvSecondArriveDate = grpSecondFlight.findViewById(R.id.tv_first_arrive_date);
        tvSecondArriveTime = grpSecondFlight.findViewById(R.id.tv_first_arrive_time);
        btnSecondWeb = grpSecondFlight.findViewById(R.id.grpWebsite);


        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        String totalTime = "";
        title.setText(getArguments().getString("title"));


        if (isNonstop) {
            if (mModel != null) {
                grpSecondFlight.setVisibility(View.GONE);
                grpWaitTime.setVisibility(View.GONE);
                Flights flights = mModel.get(0);
                Date departDate = DateUtils.parseDateTime(flights.getDeparts_at());
                Date arriveDate = DateUtils.parseDateTime(flights.getArrives_at());
                totalTime = DateUtils.durationBetween2DateTime(departDate, arriveDate);
                tvFirstFlighTime.setText(totalTime);

                firstFlightDisplay(flights, true);
            }
        }

        if (!isNonstop) {
            if (mModel != null) {
                grpSecondFlight.setVisibility(View.VISIBLE);
                grpWaitTime.setVisibility(View.VISIBLE);
                Flights firstFlight = mModel.get(0);
                Flights secondFlight = mModel.get(1);
                Date departDate = DateUtils.parseDateTime(firstFlight.getDeparts_at());
                Date arriveDate = DateUtils.parseDateTime(secondFlight.getArrives_at());

                Date firstArriveDate = DateUtils.parseDateTime(firstFlight.getArrives_at());
                Date secondDepartDate = DateUtils.parseDateTime(secondFlight.getDeparts_at());

                totalTime = DateUtils.durationBetween2DateTime(departDate, arriveDate);

                String waitTime = DateUtils.durationBetween2DateTime(firstArriveDate, secondDepartDate);
                tvWaitTime.setText(waitTime);

                ApiManager.getAirportInfo(getContext(), firstFlight.getDestination().getAirport(), new MyDataCallback<AirportInfo>() {
                    @Override
                    public void success(AirportInfo airportInfos) {
                        tvFirstArriveName.setText(airportInfos.getAirports()[0].getName());
                        tvSecondDepartName.setText(airportInfos.getAirports()[0].getName());
                    }

                    @Override
                    public void failure(Throwable t) {

                    }
                });

                firstFlightDisplay(firstFlight, false);
                secondFlightDisplay(secondFlight);
            }
        }

        //set default info
        if (mOriginAirport != null && mDestinationAirport != null) {
            tvOriginCode.setText(mOriginAirport.value);
            tvDestinationCode.setText(mDestinationAirport.value);
            tvOriginName.setText(mOriginAirport.airport_name);
            tvDestinationName.setText(mDestinationAirport.airport_name);
        }
        if (mModel != null)
            tvNumFilght.setText("" + mModel.size());
        tvTotalTime.setText(totalTime);


        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }

    public void firstFlightDisplay(final Flights flights, boolean isNonStopDisplay) {


        final Date departDate = DateUtils.parseDateTime(flights.getDeparts_at());
        Date arriveDate = DateUtils.parseDateTime(flights.getArrives_at());


        String departDateString = DateUtils.getDateDisplay(departDate);
        String arriveDateString = DateUtils.getDateDisplay(arriveDate);
        String departTimeString = DateUtils.getTimeFromDate(departDate);
        String arriveTimeString = DateUtils.getTimeFromDate(arriveDate);

        String flighDuration = DateUtils.durationBetween2DateTime(departDate, arriveDate);

        String flightClass = flights.getBooking_info().getTravel_class();
        String bookingCode = flights.getBooking_info().getBooking_code();
        String seatRemain = flights.getBooking_info().getSeats_remaining();
        firstAirline = null;
        if (mAirlines != null)
            for (Airline airline : mAirlines)
                if (airline.code.toLowerCase().equals(flights.getMarketing_airline().toLowerCase()))
                    firstAirline = airline;

        if (firstAirline != null) {
            tvFirstAirlineName.setText(firstAirline.name);
            tvFirstCountry.setText(firstAirline.country);
            Glide.with(getContext()).load(firstAirline.countryImage).into(imgFirstCountry);
        }

        String logoUrl = AIRLINE_LOGO_URL + flights.getMarketing_airline() + ".png";

        String flightNumber = flights.getMarketing_airline() + flights.getFlight_number();


        //get aircraft
        ApiManager.getAircraft(getContext(), flights.getAircraft(), new MyDataCallback<Response>() {
            @Override
            public void success(final Response response) {
                ResponseBody responseBody = (ResponseBody) response.body();
                try {
                    final String aircraft = responseBody.string();
                    if (getContext() != null) {
                        Handler mainHandler = new Handler(getContext().getMainLooper());
                        mainHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                tvFirstAircraft.setText(aircraft);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Throwable t) {
                t.printStackTrace();
            }
        });

        tvFirstDepartDate.setText(departDateString);
        tvFirstDepartTime.setText(departTimeString);

        tvFirstArriveDate.setText(arriveDateString);
        tvFirstArriveTime.setText(arriveTimeString);


        tvFirstClass.setText(flightClass);
        tvFirstBookingCode.setText(bookingCode);
        tvFirstNumSeat.setText(seatRemain);
        tvFirstFlighTime.setText(flighDuration);

        Glide.with(getContext()).load(logoUrl).into(imgFirstAirline);
        tvFirstFlightNumber.setText(flightNumber);

        if (isNonStopDisplay) {
            if (mOriginAirport != null && mDestinationAirport != null) {
                tvFirstDepartCode.setText(mOriginAirport.value);
                tvFirstArriveCode.setText(mDestinationAirport.value);
                tvFirstDepartName.setText(mOriginAirport.airport_name);
                tvFirstArriveName.setText(mDestinationAirport.airport_name);
            }
        } else {
            if (mOriginAirport != null && mDestinationAirport != null) {
                tvFirstDepartCode.setText(mOriginAirport.value);
                tvFirstDepartName.setText(mOriginAirport.airport_name);
                tvFirstArriveName.setText("");
                tvFirstArriveCode.setText(flights.getDestination().getAirport());
            }
        }
        btnFirstWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BrowserActivity.class);
                intent.putExtra("url", getFullWebsite(firstAirline, flights.getOrigin().getAirport(), flights.getDestination().getAirport(), departDate));
                startActivity(intent);
                startActivity(intent);
            }
        });
    }

    public void secondFlightDisplay(final Flights flights) {

        final Date departDate = DateUtils.parseDateTime(flights.getDeparts_at());
        Date arriveDate = DateUtils.parseDateTime(flights.getArrives_at());


        String departDateString = DateUtils.getDateDisplay(departDate);
        String arriveDateString = DateUtils.getDateDisplay(arriveDate);
        String departTimeString = DateUtils.getTimeFromDate(departDate);
        String arriveTimeString = DateUtils.getTimeFromDate(arriveDate);

        String flighDuration = DateUtils.durationBetween2DateTime(departDate, arriveDate);

        String flightClass = flights.getBooking_info().getTravel_class();
        String bookingCode = flights.getBooking_info().getBooking_code();
        String seatRemain = flights.getBooking_info().getSeats_remaining();
        secondAirline = null;
        for (Airline airline : mAirlines)
            if (airline.code.toLowerCase().equals(flights.getMarketing_airline().toLowerCase()))
                secondAirline = airline;

        if (secondAirline != null) {
            tvSecondAirlineName.setText(secondAirline.name);
            tvSecondCountry.setText(secondAirline.country);
            Glide.with(getContext()).load(secondAirline.countryImage).into(imgSecondCountry);
        }

        String logoUrl = AIRLINE_LOGO_URL + flights.getMarketing_airline() + ".png";

        String flightNumber = flights.getMarketing_airline() + flights.getFlight_number();


        //get aircraft
        ApiManager.getAircraft(getContext(), flights.getAircraft(), new MyDataCallback<Response>() {
            @Override
            public void success(final Response response) {
                ResponseBody responseBody = (ResponseBody) response.body();
                try {
                    final String aircraft = responseBody.string();

                    Handler mainHandler = new Handler(getContext().getMainLooper());
                    mainHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            tvSecondAircraft.setText(aircraft);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Throwable t) {
                t.printStackTrace();
            }
        });

        tvSecondDepartDate.setText(departDateString);
        tvSecondDepartTime.setText(departTimeString);

        tvSecondArriveDate.setText(arriveDateString);
        tvSecondArriveTime.setText(arriveTimeString);


        tvSecondClass.setText(flightClass);
        tvSecondBookingCode.setText(bookingCode);
        tvSecondNumSeat.setText(seatRemain);
        tvSecondFlighTime.setText(flighDuration);

        Glide.with(getContext()).load(logoUrl).into(imgSecondAirline);
        tvSecondFlightNumber.setText(flightNumber);

        if (mOriginAirport != null && mDestinationAirport != null) {
            tvSecondDepartCode.setText(flights.getOrigin().getAirport());
            tvSecondArriveCode.setText(mDestinationAirport.value);
            tvSecondDepartName.setText("");
            tvSecondArriveName.setText(mDestinationAirport.airport_name);
        }

        btnSecondWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BrowserActivity.class);
                intent.putExtra("url", getFullWebsite(secondAirline, flights.getOrigin().getAirport(), flights.getDestination().getAirport(), departDate));
                startActivity(intent);
            }
        });

    }

    public String getFullWebsite(Airline airline, String origin, String destination, Date departDate) {

        if (airline == null)
            return "";
        String departDateString = DateUtils.getSearchableDate(departDate);
        String departDateVN = DateUtils.getVNAirlineDate(departDate);
        String website = airline.website;
        String fullWebsite = "https://" + website;
        if (airline.code.toLowerCase().equals("bl")) {
            fullWebsite += "/vn/vi/home?origin=" + origin + "&destination=" + destination + "&flight-type=1&selected-departure-date=" + departDateString + "&adult=" + mNoAdult + "&children=" + mNoChildren + "&infants=" + mNoInfant + "&currency=VND";
        }
        if (airline.code.toLowerCase().equals("vn")) {
            fullWebsite = "https://m.sabresonicweb.com/SSW2010/VNM0/#webqtrip/e1s1?searchType=NORMAL&journeySpan=RT&origin=" + origin + "&destination=" + destination + "&departureDate=" + departDateVN + "&numAdults=" + mNoAdult + "&numChildren=" + mNoChildren + "&numInfants=" + mNoInfant + "&alternativeLandingPage=AIR_SEARCH_PAGE&lang=en_US&promoCode=";
        }

        Log.e("xx ", fullWebsite);
        return fullWebsite;
    }
}
