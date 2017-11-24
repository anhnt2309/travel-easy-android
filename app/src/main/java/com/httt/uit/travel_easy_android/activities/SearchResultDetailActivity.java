package com.httt.uit.travel_easy_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.google.gson.Gson;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.fragment.ScrollViewFragment;
import com.httt.uit.travel_easy_android.model.Airline;
import com.httt.uit.travel_easy_android.model.Itineraries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TuanAnh on 11/20/17.
 */

public class SearchResultDetailActivity extends AppCompatActivity {
    public static final String RESULT_DETAIL_MODEL_KEY = "RESULT_DETAIL_MODEL_KEY";
    int pageCount = 3;
    int outboundSize = 0;
    int inboundSize = 0;
    int fareSize = 1;

    private HollyViewPager hollyViewPager;
    private Document mDocument;
    private Itineraries mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_detail_activity);
        initUI();
        getData();
        initHollyViewPager();


    }

    public void initUI() {
        hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);
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
                //if(position%2==0)
                //    return new RecyclerViewFragment();
                //else
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
    }

    public void getAirline(final String iataCode) {
        //craw data
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

                    Airline airline = new Airline(airlineName, airlineLinks, countryValueFinal, countryImage);
                    Log.d("xx", center.html());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
