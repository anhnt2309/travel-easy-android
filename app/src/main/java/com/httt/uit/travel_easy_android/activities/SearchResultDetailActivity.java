package com.httt.uit.travel_easy_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.fragment.FareFragment;
import com.httt.uit.travel_easy_android.fragment.ScrollViewFragment;
import com.httt.uit.travel_easy_android.manager.CacheManager;
import com.httt.uit.travel_easy_android.model.Airline;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.Flights;
import com.httt.uit.travel_easy_android.model.Itineraries;
import com.httt.uit.travel_easy_android.model.Outbound;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TuanAnh on 11/20/17.
 */

public class SearchResultDetailActivity extends AppCompatActivity {
    public static final String RESULT_DETAIL_MODEL_KEY = "RESULT_DETAIL_MODEL_KEY";
    public static final String RESULT_DETAIL_ORIGIN_AIRPORT_KEY = "RESULT_DETAIL_ORIGIN_AIRPORT_KEY";
    public static final String RESULT_DETAIL_DESTINATION_AIRPORT_KEY = "RESULT_DETAIL_DESTINATION_AIRPORT_KEY";

    public static final String RESULT_CURRENCY_KEY = "RESULT_CURRENCY_KEY";
    public static final String RESULT_ADULT_NUMBER_KEY = "RESULT_ADULT_NUMBER_KEY";
    public static final String RESULT_CHILDREN_NUMBER_KEY = "RESULT_CHILDREN_NUMBER_KEY";
    public static final String RESULT_INFANT_NUMBER_KEY = "RESULT_INFANT_NUMBER_KEY";


    public static final String ARRAY_AIRLINE_KEY = "ARRAY_AIRLINE_KEY";
    int pageCount = 3;
    int outboundSize = 0;
    int inboundSize = 0;
    int fareSize = 1;

    private HollyViewPager hollyViewPager;
    private ShineButton sbBack;

    private Document mDocument;
    private Itineraries mModel;
    private AutoCompleteAirport mOriginAirport;
    private AutoCompleteAirport mDestinationAirport;
    private ArrayList<Airline> mAirlines;
    private String mCurrency;
    private int mNoAdult;
    private int mNoChildren;
    private int mNoInfant;
    private boolean hasStopFlight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_detail_activity);
        Iconify.with(new FontAwesomeModule());
        initUI();
        getData();
        initHollyViewPager();


    }

    public void initUI() {
        hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);
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

    public void initHollyViewPager() {
        if (mModel.getInbound() == null)
            pageCount = 2;
        else {
            if (mModel.getInbound().getFlights().size() > 1) {
                inboundSize = 2;
            } else
                inboundSize = 0;
        }

        if (mModel.getOutbound().getFlights().size() > 1) {
            outboundSize = 2;
        } else
            outboundSize = 0;


        hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.alerter_padding_half));
        hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {
                if (pageCount == 2) {
                    if (page == 0)
                        return ((outboundSize + 4) % 10) / 10f;
                    if (page == 1)
                        return ((fareSize + 4) % 10) / 10f;
                } else {
                    if (page == 0)
                        return ((outboundSize + 4) % 10) / 10f;
                    if (page == 1)
                        return ((inboundSize + 4) % 10) / 10f;
                    if (page == 2)
                        return ((fareSize + 4) % 10) / 10f;
                }
                return 0;
            }
        });

        hollyViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (pageCount == 2) {
                    if (position == 0)
                        return ScrollViewFragment.newInstance((String) getPageTitle(position), mModel.getOutbound().getFlights(), mOriginAirport, mDestinationAirport, mCurrency, mNoAdult, mNoChildren, mNoInfant);
                    if (position == 1)

                        return FareFragment.newInstance((String) getPageTitle(position), mModel.fare, mCurrency, mNoAdult, mNoChildren, mNoInfant,hasStopFlight);
                } else {
                    if (position == 0)
                        return ScrollViewFragment.newInstance((String) getPageTitle(position), mModel.getOutbound().getFlights(), mOriginAirport, mDestinationAirport, mCurrency, mNoAdult, mNoChildren, mNoInfant);
                    if (position == 1)
                        return ScrollViewFragment.newInstance((String) getPageTitle(position), mModel.getInbound().getFlights(), mDestinationAirport, mOriginAirport, mCurrency, mNoAdult, mNoChildren, mNoInfant);
                    if (position == 2)
                        return FareFragment.newInstance((String) getPageTitle(position), mModel.fare, mCurrency, mNoAdult, mNoChildren, mNoInfant,hasStopFlight);
                }
                return ScrollViewFragment.newInstance((String) getPageTitle(position));
            }

            @Override
            public int getCount() {
                return pageCount;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (pageCount == 2) {
                    if (position == 0)
                        return getString(R.string.txt_depart);
                    if (position == 1)
                        return getString(R.string.txt_price_detail);
                } else {
                    if (position == 0)
                        return getString(R.string.txt_depart);
                    if (position == 1)
                        return getString(R.string.txt_return);
                    if (position == 2)
                        return getString(R.string.txt_price_detail);
                }
                return "";
            }

        });
    }

    public void getData() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        String modelString = intent.getStringExtra(RESULT_DETAIL_MODEL_KEY);
        if (modelString.isEmpty())
            return;
        mModel = new Gson().fromJson(modelString, Itineraries.class);



        //Outbound handle
        Outbound outbound = mModel.getOutbound();
        Flights obFirstFlight = null;
        Flights obSecondFlight = null;

        ArrayList<Flights> outboundFlights = outbound.getFlights();
        if (outboundFlights.size() == 1) {
            obFirstFlight = outboundFlights.get(0);

        }
        if (outboundFlights.size() >= 2) {
            obFirstFlight = outboundFlights.get(0);
            obSecondFlight = outboundFlights.get(1);
        }

        if (obSecondFlight == null) {
            hasStopFlight = false;

        }
        if (obSecondFlight != null) {
            hasStopFlight = true;

        }



        String originString = intent.getStringExtra(RESULT_DETAIL_ORIGIN_AIRPORT_KEY);
        mOriginAirport = new Gson().fromJson(originString, AutoCompleteAirport.class);

        String desString = intent.getStringExtra(RESULT_DETAIL_DESTINATION_AIRPORT_KEY);
        mDestinationAirport = new Gson().fromJson(desString, AutoCompleteAirport.class);

        mCurrency = intent.getStringExtra(RESULT_CURRENCY_KEY);
        mNoAdult = intent.getIntExtra(RESULT_ADULT_NUMBER_KEY,0);
        mNoChildren = intent.getIntExtra(RESULT_CHILDREN_NUMBER_KEY,0);
        mNoInfant = intent.getIntExtra(RESULT_INFANT_NUMBER_KEY,0);

        mAirlines = CacheManager.getListCacheData(ARRAY_AIRLINE_KEY, new TypeToken<ArrayList<Airline>>() {
        }.getType());
        //get all airline from inbound and outbound flight for crawler
        ArrayList<String> airlineCodes = new ArrayList<>();
        ArrayList<Flights> obFlights = mModel.getOutbound().getFlights();
        if (obFlights.size() == 1) {
            airlineCodes.add(obFlights.get(0).getMarketing_airline());
        } else {
            for (Flights flights : obFlights) {
                if (!airlineCodes.contains(flights))
                    airlineCodes.add(flights.getMarketing_airline());
            }
        }

        if (mModel.getInbound() != null && mModel.getInbound().getFlights().size() >= 1) {
            ArrayList<Flights> ibFlights = mModel.getInbound().getFlights();
            if (ibFlights.size() == 1) {
                if (!airlineCodes.contains(ibFlights.get(0)))
                    airlineCodes.add(ibFlights.get(0).getMarketing_airline());
            } else {
                for (Flights flights : ibFlights) {
                    if (!airlineCodes.contains(flights))
                        airlineCodes.add(flights.getMarketing_airline());
                }
            }
        }

        //get and save airline to cache
        if (mAirlines == null || mAirlines.size() == 0) {
            mAirlines = new ArrayList<>();
            for (int i = 0; i < airlineCodes.size(); i++)
                getAirline(airlineCodes.get(i), i, airlineCodes.size() - 1);

        }
        boolean isAlreadyCrawed;
        if (mAirlines != null && mAirlines.size() > 0) {
            for (int j = 0; j < airlineCodes.size(); j++) {
                isAlreadyCrawed = false;
                for (Airline airline : mAirlines) {
                    if (airline.code.toLowerCase().equals(airlineCodes.get(j).toLowerCase())) {
                        isAlreadyCrawed = true;
                        break;
                    }

                }
                if (!isAlreadyCrawed)
                    getAirline(airlineCodes.get(j), j, airlineCodes.size() - 1);
            }
        }
    }

    public void getAirline(final String iataCode, final int currentIndex, final int lastIndex) {
        //craw data
        final Airline returnAirline;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    mDocument = Jsoup.connect("http://www.avcodes.co.uk/airlcoderes.asp")
                            .data("status", "Y")
                            .data("iataairl", iataCode)
                            .data("icaoairl", "")
                            .data("account", "")
                            .data("prefix", "")
                            .data("airlname", "")
                            .data("country", "")
                            .data("callsign", "")
                            .data("B1", "Submit")
                            .header("content-type", "application/x-www-form-urlencoded")
                            .post();

                    Elements center = mDocument.select("center");
                    //get airline name
                    String airlineName = center.select("td[class='tablebg']").text();

                    //get airline website
                    String airlineLinks = center.select("a[href]").get(0).text();

                    //get country name
                    Elements countryName = center.select("td").get(8).getElementsContainingText("Country");
                    String countryValue = countryName.text();
                    String countryValueFinal = countryValue.substring((countryValue.indexOf(":") + 2), countryValue.length());
                    //get country image link
                    Elements countryLinks = center.select("img[src]");
                    String countryImage = countryLinks.get(1).absUrl("src");

                    final Airline airline = new Airline(iataCode, airlineName, airlineLinks, countryValueFinal, countryImage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (currentIndex < lastIndex)
                                mAirlines.add(airline);
                            if (currentIndex == lastIndex) {
                                mAirlines.add(airline);
                                CacheManager.saveListCacheData(ARRAY_AIRLINE_KEY, mAirlines);
                            }

                        }
                    });
                    Log.d("xx", center.html());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
