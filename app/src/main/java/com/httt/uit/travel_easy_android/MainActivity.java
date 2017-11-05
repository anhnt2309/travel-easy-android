
package com.httt.uit.travel_easy_android;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.TimelineView;
import com.google.gson.JsonElement;
import com.httt.uit.travel_easy_android.adapters.ClassSpinnerAdapter;
import com.httt.uit.travel_easy_android.animators.ChatAvatarsAnimator;
import com.httt.uit.travel_easy_android.animators.InSyncAnimator;
import com.httt.uit.travel_easy_android.animators.RocketAvatarsAnimator;
import com.httt.uit.travel_easy_android.animators.RocketFlightAwayAnimator;
import com.httt.uit.travel_easy_android.manager.ApiManager;
import com.httt.uit.travel_easy_android.model.FlightClass;
import com.httt.uit.travel_easy_android.request.MyDataCallback;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.redbooth.WelcomeCoordinatorLayout;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.savvi.rangedatepicker.CalendarPickerView;


import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import github.chenupt.springindicator.SpringIndicator;
import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;
import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;
import tyrantgit.explosionfield.ExplosionField;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    public static final int ROUND_TRIP_DATE_REQUEST = 1234;
    public static final int ONE_WAY_DATE_REQUEST = 1235;
    public static final String FLIGHT_TYPE_CODE = "FLIGHT_TYPE_CODE";

    public static final int ONE_WAY_TYPE = 1236;
    public static final int ROUND_TRIP_TYPE = 1237;

    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    @BindView(R.id.coordinator)
    WelcomeCoordinatorLayout coordinatorLayout;
    private RocketAvatarsAnimator rocketAvatarsAnimator;
    private ChatAvatarsAnimator chatAvatarsAnimator;
    private RocketFlightAwayAnimator rocketFlightAwayAnimator;
    private InSyncAnimator inSyncAnimator;
    private TextView tvAppName;
    private TextView tvCommandScene2;
    private Spinner spClass;
    private DatePickerTimeline datepicker;
    private RadioRealButtonGroup rdbType;
    private ExplosionField explosionField;
    private RelativeLayout lnReturnDate;
    private RelativeLayout lnDepartDate;
    private LinearLayout grpDate;
    private IconTextView tvDayDepart;
    private IconTextView tvDayReturn;
    private TextView tvDateDepart;
    private TextView tvDateReturn;
    private StepperTouch spAdults;
    private StepperTouch spChildrens;
    private StepperTouch spInfants;

    private int rbId = R.id.rb_round_trip;
    private int spInfantMax = 0;
    private Date mDepartDate;
    private Date mReturnDate;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        LGSnackbarManager.prepare(getApplicationContext(),
                LGSnackBarThemeManager.LGSnackbarThemeName.SHINE);
        Iconify.with(new FontAwesomeModule());

        ButterKnife.bind(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();
        explosionField = ExplosionField.attach2Window(MainActivity.this);
//        ApiManager.getLocationInfo(this, "DLI", new MyDataCallback<JsonElement>() {
//            @Override
//            public void success(JsonElement jsonElement) {
//                Log.d("abc", "" + jsonElement);
//            }
//
//            @Override
//            public void failure(Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        finishMyActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ROUND_TRIP_DATE_REQUEST) {
            if (resultCode == RESULT_OK) {

                Long departDateLong = data.getLongExtra(PickDateActivity.DEPART_DATE_DATE, 0);
                Long returnDateLong = data.getLongExtra(PickDateActivity.RETURN_DATE_DATE, 0);
                if (departDateLong == 0 || returnDateLong == 0)
                    return;
                Date departDate = new Date();
                departDate.setTime(departDateLong);
                mDepartDate = departDate;
                Date returnDate = new Date();
                returnDate.setTime(returnDateLong);
                mReturnDate = returnDate;

                displayDepartureDate(departDate);
                displayReturnDate(returnDate);
            }
        }

        if (requestCode == ONE_WAY_DATE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Long departDateLong = data.getLongExtra(PickDateActivity.DEPART_DATE_DATE, 0);
                if (departDateLong == 0)
                    return;
                Date departDate = new Date();
                departDate.setTime(departDateLong);
                mDepartDate = departDate;
                displayDepartureDate(departDate);
            }
        }
    }

    public void finishMyActivity() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initializePages() {
        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.addPage(R.layout.welcome_page_1,
                R.layout.welcome_page_2,
                R.layout.welcome_page_3

        );
        tvAppName = (TextView) findViewById(R.id.custom_switcher);
        tvCommandScene2 = (TextView) findViewById(R.id.txt_command);
        spClass = (Spinner) findViewById(R.id.sp_class);
        datepicker = (DatePickerTimeline) findViewById(R.id.datepicker);
        rdbType = (RadioRealButtonGroup) findViewById(R.id.rbg_fly_type);
        lnReturnDate = (RelativeLayout) findViewById(R.id.ln_return_date);
        lnDepartDate = (RelativeLayout) findViewById(R.id.rl_departure_date);
        tvDateDepart = (TextView) findViewById(R.id.tv_date_depart);
        tvDateReturn = (TextView) findViewById(R.id.tv_date_return);
        tvDayDepart = (IconTextView) findViewById(R.id.tv_day_depart);
        tvDayReturn = (IconTextView) findViewById(R.id.tv_day_return);
        spAdults = (StepperTouch) findViewById(R.id.stepper_adults);
        spChildrens = (StepperTouch) findViewById(R.id.stepper_childrens);
        spInfants = (StepperTouch) findViewById(R.id.stepper_infants);

        grpDate = (LinearLayout) findViewById(R.id.grp_date);

        initEvents();


    }

    private void initEvents() {
        grpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PickDateActivity.class);
                if (mDepartDate != null)
                    intent.putExtra(PickDateActivity.DEPART_DATE_DATE, mDepartDate.getTime());
                if (rbId == R.id.rb_round_trip) {
                    if (mReturnDate != null)
                        intent.putExtra(PickDateActivity.RETURN_DATE_DATE, mReturnDate.getTime());
                    intent.putExtra(FLIGHT_TYPE_CODE, ROUND_TRIP_TYPE);
                    startActivityForResult(intent, ROUND_TRIP_DATE_REQUEST);
                }
                if (rbId == R.id.rb_one_way) {
                    intent.putExtra(FLIGHT_TYPE_CODE, ONE_WAY_TYPE);
                    startActivityForResult(intent, ONE_WAY_DATE_REQUEST);
                }
                overridePendingTransition(0, 0);
            }
        });

        rdbType.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (button.getId() == R.id.rb_one_way) {
                    rbId = R.id.rb_one_way;
                    mReturnDate = null;
                    explosionField.explode(lnReturnDate);
                    lnReturnDate.setVisibility(View.GONE);
                    lnDepartDate.setBackground(getResources().getDrawable(R.drawable.shadow_bg_color_full));
                }
                if (button.getId() == R.id.rb_round_trip) {
                    rbId = R.id.rb_round_trip;
                    if (explosionField != null)
                        explosionField.clear();
                    displayReturnDate(mReturnDate);
                    lnDepartDate.setBackground(getResources().getDrawable(R.drawable.shadow_bg_color));
                    //get View Back
                    lnReturnDate.animate().setDuration(150).setStartDelay(150).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).start();
                    lnReturnDate.setVisibility(View.VISIBLE);
                    Animation animation = getFadeInAnimation();
                    spClass.setAnimation(animation);
                }
            }
        });


        ArrayList<FlightClass> flightClasses = new ArrayList<>();
        flightClasses.add(FlightClass.ecoClass);
        flightClasses.add(FlightClass.preEcoClass);
        flightClasses.add(FlightClass.bussClass);
        flightClasses.add(FlightClass.fiClass);

        ClassSpinnerAdapter classSpinnerAdapter = new ClassSpinnerAdapter(this, 0, flightClasses);
        spClass.setAdapter(classSpinnerAdapter);
        spClass.setSelection(0);

        spAdults.stepper.setMin(1);
        spAdults.stepper.setMax(12);
        spAdults.stepper.setValue(1);
        spAdults.stepper.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int i, boolean b) {
                int currentAdults = spAdults.stepper.getValue();
                if (currentAdults < spInfantMax) {
                    spInfants.stepper.setMax(currentAdults);
                    spInfantMax = currentAdults;
                    spInfants.stepper.setValue(currentAdults);
                    return;
                }
                spInfants.stepper.setMax(currentAdults);
                spInfantMax = spAdults.stepper.getValue();

            }
        });

        spChildrens.stepper.setMin(0);
        spChildrens.stepper.setMax(12);

        spInfants.stepper.setMin(0);
        spInfants.stepper.setMax(1);
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
                if (tvAppName != null)
                    if (tvAppName.getVisibility() == View.VISIBLE) {
                        tvAppName.setVisibility(View.GONE);
                        tvAppName.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
                    }
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {

                    case 0:
                        if (tvAppName != null)
                            if (tvAppName.getVisibility() == View.GONE) {
                                tvAppName.setVisibility(View.VISIBLE);
                            }
                        if (rocketAvatarsAnimator == null) {
                            rocketAvatarsAnimator = new RocketAvatarsAnimator(coordinatorLayout);
                            rocketAvatarsAnimator.play();
                        }
                        break;
                    case 1:
                        if (tvAppName != null)
                            if (tvAppName.getVisibility() == View.VISIBLE) {
                                tvAppName.setVisibility(View.GONE);
                            }

                        if (chatAvatarsAnimator == null) {
                            chatAvatarsAnimator = new ChatAvatarsAnimator(coordinatorLayout);
                            chatAvatarsAnimator.play();
                            tvCommandScene2.setVisibility(View.VISIBLE);
                            tvCommandScene2.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
                        }
                        break;
                    case 2:
                        if (tvAppName != null)
                            if (tvAppName.getVisibility() == View.VISIBLE) {
                                tvAppName.setVisibility(View.GONE);
                            }
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
//        final int colorPage4 = ResourcesCompat.getColor(resources, R.color.page4, getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    private Animation getFadeInAnimation() {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        animation.setDuration(500);
        return animation;
    }

    public void displayDepartureDate(Date date) {
        if (date == null) {
            String reset = getResources().getString(R.string.fa_custom_pick_date);
            tvDayDepart.setText(reset);
            PickDateActivity.setViewAnimation(this, tvDayDepart, android.R.anim.fade_in);
            tvDateDepart.setVisibility(View.GONE);
            return;
        }
        String day = DateUtils.getDay(date);
        String dateMonth = DateUtils.getDayMonth(date);
        tvDayDepart.setText(day);
        PickDateActivity.setViewAnimation(this, tvDayDepart, android.R.anim.fade_in);

        tvDateDepart.setVisibility(View.VISIBLE);
        tvDateDepart.setText(dateMonth);
        PickDateActivity.setViewAnimation(this, tvDateDepart, android.R.anim.fade_in);
    }

    public void displayReturnDate(Date date) {
        if (date == null) {
            String reset = getResources().getString(R.string.fa_custom_pick_date);
            tvDayReturn.setText(reset);
            PickDateActivity.setViewAnimation(this, tvDayDepart, android.R.anim.fade_in);
            tvDateReturn.setVisibility(View.GONE);
            return;
        }
        String day = DateUtils.getDay(date);
        String dateMonth = DateUtils.getDayMonth(date);
        tvDayReturn.setText(day);
        PickDateActivity.setViewAnimation(this, tvDayReturn, android.R.anim.fade_in);

        tvDateReturn.setVisibility(View.VISIBLE);
        tvDateReturn.setText(dateMonth);
        PickDateActivity.setViewAnimation(this, tvDateReturn, android.R.anim.fade_in);
    }
}
