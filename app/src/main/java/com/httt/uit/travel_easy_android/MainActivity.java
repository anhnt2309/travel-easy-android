
package com.httt.uit.travel_easy_android;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.httt.uit.travel_easy_android.adapters.LocationViewPagerAdapter;
import com.httt.uit.travel_easy_android.animators.ChatAvatarsAnimator;
import com.httt.uit.travel_easy_android.animators.InSyncAnimator;
import com.httt.uit.travel_easy_android.animators.RocketAvatarsAnimator;
import com.httt.uit.travel_easy_android.animators.RocketFlightAwayAnimator;
import com.redbooth.WelcomeCoordinatorLayout;
import com.savvi.rangedatepicker.CalendarPickerView;


import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.chenupt.springindicator.SpringIndicator;

public class MainActivity extends AppCompatActivity {
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    @BindView(R.id.coordinator)
    WelcomeCoordinatorLayout coordinatorLayout;
    private RocketAvatarsAnimator rocketAvatarsAnimator;
    private ChatAvatarsAnimator chatAvatarsAnimator;
    private RocketFlightAwayAnimator rocketFlightAwayAnimator;
    private InSyncAnimator inSyncAnimator;

    private ViewPager mFromViewPager;
    private ViewPager mToViewPager;
    private SpringIndicator mFromIndicator;
    private SpringIndicator mToIndicator;
    private CalendarPickerView mCalendarPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();
    }

    private void initializePages() {
        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.addPage(R.layout.welcome_page_1,
                R.layout.welcome_page_2,
                R.layout.welcome_page_3
        );
//        mFromViewPager = coordinatorLayout.findViewById(R.id.viewpager_from);
//        mToViewPager = coordinatorLayout.findViewById(R.id.viewpager_to);
//        mFromIndicator = coordinatorLayout.findViewById(R.id.from_indicator);
//        mToIndicator = coordinatorLayout.findViewById(R.id.to_indicator);
//        mCalendarPickerView = coordinatorLayout.findViewById(R.id.calendar_view);
//
//        LocationViewPagerAdapter locationViewPagerAdapter = new LocationViewPagerAdapter(getSupportFragmentManager());
//        mFromViewPager.setAdapter(locationViewPagerAdapter);
//        mToViewPager.setAdapter(locationViewPagerAdapter);
//        mFromIndicator.setViewPager(mFromViewPager);
//        mToIndicator.setViewPager(mToViewPager);
//
//        mCalendarPickerView.init(new Date(), new Date(2099, 12, 30))
//                .inMode(CalendarPickerView.SelectionMode.RANGE)
//                .withSelectedDate(new Date());
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(7);
//
//        mCalendarPickerView.deactivateDates(list);

    }

    private void initializeListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {
                    case 0:
                        if (rocketAvatarsAnimator == null) {
                            rocketAvatarsAnimator = new RocketAvatarsAnimator(coordinatorLayout);
                            rocketAvatarsAnimator.play();
                        }
                        break;
                    case 1:
                        if (chatAvatarsAnimator == null) {
                            chatAvatarsAnimator = new ChatAvatarsAnimator(coordinatorLayout);
                            chatAvatarsAnimator.play();
                        }
                        break;
                    case 2:
                        if (inSyncAnimator == null) {
                            inSyncAnimator = new InSyncAnimator(coordinatorLayout);
                            inSyncAnimator.play();
                        }
                        break;
//                    case 3:
//                        if (rocketFlightAwayAnimator == null) {
//                            rocketFlightAwayAnimator = new RocketFlightAwayAnimator(coordinatorLayout);
//                            rocketFlightAwayAnimator.play();
//                        }
//                        break;
                }
            }
        });
    }

    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();
        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.page1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.page2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.page3, getTheme());
        final int colorPage4 = ResourcesCompat.getColor(resources, R.color.page4, getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3, colorPage4);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

//    @OnClick(R.id.skip)
//    void skip() {
//        coordinatorLayout.setCurrentPage(coordinatorLayout.getNumOfPages() - 1, true);
//    }
}
