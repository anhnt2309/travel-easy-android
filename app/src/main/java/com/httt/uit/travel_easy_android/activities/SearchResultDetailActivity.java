package com.httt.uit.travel_easy_android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.fragment.ScrollViewFragment;

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
    int pageCount = 3;


    Toolbar toolbar;
    HollyViewPager hollyViewPager;
    Document mDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_detail_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);

        //craw data
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final StringBuilder builder = new StringBuilder();
                    mDocument = Jsoup.connect("http://www.avcodes.co.uk/airlcoderes.asp")
                            .data("status", "Y")
                            .data("iataairl", "BL")
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

                    String airlineName = center.select("td[class='tablebg']").text();

                    String airlineLinks = center.select("a[href]").get(0).text();
                    Elements countryLinks = center.select("img[src]");
                    String  countryImage = countryLinks.get(1).absUrl("src");
                    Log.d("xx", center.html());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        if (mDocument != null) {
            Log.d("xx", mDocument.body().html());
        }
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.alerter_padding_half));
        hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {
                return ((page + 4) % 10) / 10f;
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
                return "TITLE " + position;
            }
        });
    }
}
