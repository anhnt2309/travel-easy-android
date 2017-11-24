
package com.httt.uit.travel_easy_android;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.google.gson.Gson;
import com.httt.uit.travel_easy_android.activities.PickDateActivity;
import com.httt.uit.travel_easy_android.activities.SearchAirportActivity;
import com.httt.uit.travel_easy_android.activities.SearchResultActivity;
import com.httt.uit.travel_easy_android.activities.SearchResultDetailActivity;
import com.httt.uit.travel_easy_android.adapters.ClassSpinnerAdapter;
import com.httt.uit.travel_easy_android.animators.ChatAvatarsAnimator;
import com.httt.uit.travel_easy_android.animators.InSyncAnimator;
import com.httt.uit.travel_easy_android.animators.RocketAvatarsAnimator;
import com.httt.uit.travel_easy_android.animators.RocketFlightAwayAnimator;
import com.httt.uit.travel_easy_android.manager.ApiManager;
import com.httt.uit.travel_easy_android.manager.CacheManager;
import com.httt.uit.travel_easy_android.manager.HistoryManager;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.FlightClass;
import com.httt.uit.travel_easy_android.model.FlightResults;
import com.httt.uit.travel_easy_android.model.History;
import com.httt.uit.travel_easy_android.request.MyDataCallback;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.redbooth.WelcomeCoordinatorLayout;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.tapadoo.alerter.Alerter;


import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;
import me.grantland.widget.AutofitTextView;
import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;
import tyrantgit.explosionfield.ExplosionField;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    public static final int ROUND_TRIP_DATE_REQUEST = 1234;
    public static final int ONE_WAY_DATE_REQUEST = 1235;
    public static final int ORIGIN_AIRPORT_REQUEST = 1238;
    public static final int DESTINATION_AIRPORT_REQUEST = 1239;
    public static final String FLIGHT_TYPE_CODE = "FLIGHT_TYPE_CODE";
    public static final String DEFAULT_CURRENCY = "VND";
    public static final int DEFAULT_ROUND_TRIP = 1;
    public static final int DEFAULT_ONE_WAY = 0;

    public static final String ORIGIN_AIRPORT_MODEL = "ORIGIN_AIRPORT_MODEL";
    public static final String DEPART_DATE_MODEL = "DEPART_DATE_MODEL";
    public static final String DESTINATION_AIRPORT_MODEL = "DESTINATION_AIRPORT_MODEL";
    public static final String RETURN_DATE_MODEL = "RETURN_DATE_MODEL";
    public static final String FLIGHT_TYPE_STRING = "FLIGHT_TYPE_STRING";
    public static final String FLIGHT_ADULT_STRING = "FLIGHT_ADULT_STRING";
    public static final String FLIGHT_CHILDREN_STRING = "FLIGHT_CHILDREN_STRING";
    public static final String FLIGHT_INFANT_STRING = "FLIGHT_INFANT_STRING";
    public static final String FLIGHT_CLASS_MODEL = "FLIGHT_CLASS_MODEL";
    public static final String FLIGHT_CURRENCY_STRING = "FLIGHT_CURRENCY_STRING";
    public static final String FLIGHT_RESULT_MODEL = "FLIGHT_RESULT_MODEL";

    public static final String GUIDE_SCREEN2 = "GUIDE_SCREEN2";
    public static final String GUIDE_SCREEN3 = "GUIDE_SCREEN3";
    public static final String GUIDE_SCREEN_SEARCH = "GUIDE_SCREEN_SEARCH";
    public static final String GUIDE_SCREEN_DATE = "GUIDE_SCREEN_DATE";
    public static final String GUIDE_SCREEN_RESULT = "GUIDE_SCREEN_RESULT";
    public static final String GUIDE_SCREEN_FILTER = "GUIDE_SCREEN_FILTER";

    public static final String GUIDE_SHOWN_VALUE = "YES";

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

    private RelativeLayout grpOrigin;
    private AutofitTextView tvOriginAiportName;
    private TextView tvOriginCity;
    private TextView tvOriginCode;
    private ImageView imgOriginCity;

    private RelativeLayout grpDestination;
    private AutofitTextView tvDestinationAirportName;
    private TextView tvDestinationCity;
    private TextView tvDestinationCode;
    private ImageView imgDestinationCity;
    private ShineButton sbSwitch;

    private TextView tvOriginCode3;
    private AutofitTextView tvOriginAirport3;
    private TextView tvDestinationCode3;
    private AutofitTextView tvDestinationAirport3;
    private FloatingActionButton doneButton;

    private LinearLayout grpLoadingScreen;
    private LinearLayout grpDividerDate;

    private int rbId = R.id.rb_round_trip;
    private int spInfantMax = 0;
    private Date mDepartDate;
    private Date mReturnDate;
    private AutoCompleteAirport mOriginAirport;
    private AutoCompleteAirport mDestinationAirport;
    private int adult;
    private int children;
    private int infant;
    private FlightClass selectedClass;
    HistoryManager history =new HistoryManager(this);

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
        BackHistory(history);

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

        if (requestCode == ORIGIN_AIRPORT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String modelString = data.getStringExtra(SearchAirportActivity.RESULT_MODEL);
                if (modelString == null || modelString.equals(""))
                    return;
                AutoCompleteAirport model = new Gson().fromJson(modelString, AutoCompleteAirport.class);
                if (model == null)
                    return;
                displayOriginAirport(model);
                mOriginAirport = model;
            }
        }

        if (requestCode == DESTINATION_AIRPORT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String modelString = data.getStringExtra(SearchAirportActivity.RESULT_MODEL);
                if (modelString == null || modelString.equals(""))
                    return;
                AutoCompleteAirport model = new Gson().fromJson(modelString, AutoCompleteAirport.class);
                if (model == null)
                    return;
                displayDestinationAirport(model);
                mDestinationAirport = model;
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
//        datepicker = (DatePickerTimeline) findViewById(R.id.datepicker);
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
        doneButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        sbSwitch = (ShineButton) findViewById(R.id.shine_button_switch);

        grpLoadingScreen = (LinearLayout) findViewById(R.id.grpLoadingScreen);
        grpLoadingScreen.setVisibility(View.GONE);

        grpDividerDate = (LinearLayout) findViewById(R.id.divider_date);
        tvOriginCode3 = (TextView) findViewById(R.id.txt_from_code);
        tvOriginAirport3 = (AutofitTextView) findViewById(R.id.txt_from_city);
        tvDestinationCode3 = (TextView) findViewById(R.id.txt_to_code);
        tvDestinationAirport3 = (AutofitTextView) findViewById(R.id.txt_to_city);

        grpDate = (LinearLayout) findViewById(R.id.grp_date);
        initAirportHolder();
        initEvents();
    }

    private void initAirportHolder() {
        grpOrigin = (RelativeLayout) findViewById(R.id.grpOrigin);
        tvOriginAiportName = (AutofitTextView) findViewById(R.id.txt_airport_name);
        tvOriginCity = (TextView) findViewById(R.id.txt_city_name);
        tvOriginCode = (TextView) findViewById(R.id.tv_depart_code);
        imgOriginCity = (ImageView) findViewById(R.id.img_city);

        grpDestination = (RelativeLayout) findViewById(R.id.grpDestination);
        tvDestinationAirportName = (AutofitTextView) findViewById(R.id.txt_airport_name_return);
        tvDestinationCity = (TextView) findViewById(R.id.txt_city_name_return);
        tvDestinationCode = (TextView) findViewById(R.id.tv_return_code);
        imgDestinationCity = (ImageView) findViewById(R.id.img_city_return);
    }

    private void initEvents() {
        initAirportEvent();
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
//                overridePendingTransition(0, 0);
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
                    grpDividerDate.setVisibility(lnReturnDate.getVisibility());
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
                    grpDividerDate.setVisibility(lnReturnDate.getVisibility());
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

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOriginAirport == null || mDestinationAirport == null) {
                    if (mOriginAirport == null)
                        Alerter.create(MainActivity.this)
                                .setTitle("Lỗi")
                                .setText("Bạn chưa chọn SÂN BAY ĐI. Bấm vào đây để chọn!!!!")
                                .enableSwipeToDismiss()
                                .setTitleAppearance(R.style.text_title)
                                .setTextAppearance(R.style.text_subtitle_color)
                                .setBackgroundColorRes(R.color.color_red) // or setBackgroundColorInt(Color.CYAN)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        grpOrigin.callOnClick();
                                        if (Alerter.isShowing())
                                            Alerter.hide();
                                    }
                                })
                                .show();
                    if (mDestinationAirport == null)
                        Alerter.create(MainActivity.this)
                                .setTitle("Lỗi")
                                .setText("Bạn chưa chọn SÂN BAY ĐẾN. Bấm vào đây để chọn!!!!")
                                .setTitleAppearance(R.style.text_title)
                                .setTextAppearance(R.style.text_subtitle_color)
                                .enableSwipeToDismiss()
                                .setBackgroundColorRes(R.color.color_red) // or setBackgroundColorInt(Color.CYAN)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        grpDestination.callOnClick();
                                        if (Alerter.isShowing())
                                            Alerter.hide();
                                    }
                                })
                                .show();
                    return;
                }
                adult = spAdults.stepper.getValue();
                children = spChildrens.stepper.getValue();
                infant = spInfants.stepper.getValue();
                selectedClass = (FlightClass) spClass.getSelectedItem();
                String selectedClassString = selectedClass.getClassSearch();

                //round trip
                if (rbId == R.id.rb_round_trip) {
                    if (mDepartDate == null || mReturnDate == null) {
                        Alerter.create(MainActivity.this)
                                .setTitle("Lỗi")
                                .setText("Bạn chưa chọn THỜI GIAN CHO CHUYẾN BAY. Bấm vào đây để chọn!!!!")
                                .enableSwipeToDismiss()
                                .setTitleAppearance(R.style.text_title)
                                .setTextAppearance(R.style.text_subtitle_color)
                                .setBackgroundColorRes(R.color.color_red) // or setBackgroundColorInt(Color.CYAN)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        grpDate.callOnClick();
                                        if (Alerter.isShowing())
                                            Alerter.hide();
                                    }
                                })
                                .show();
                        return;
                    }
                    String departDate = DateUtils.getSearchDate(mDepartDate);
                    String returnDate = DateUtils.getSearchDate(mReturnDate);
                    history.addHistory(mOriginAirport);
                    history.addHistory(mDestinationAirport);
                    //call api
                    grpLoadingScreen.setVisibility(View.VISIBLE);
                    ApiManager.getSearchResult(MainActivity.this, mOriginAirport.value, mDestinationAirport.value, departDate, returnDate, adult, children, infant, DEFAULT_CURRENCY, selectedClassString, resultsCallBack);
                }

                //one-way
                if (rbId == R.id.rb_one_way) {
                    if (mDepartDate == null) {
                        Alerter.create(MainActivity.this)
                                .setTitle("Lỗi")
                                .setText("Bạn hưa chọn THỜI GIAN CHO CHUYẾN BAY. Bấm vào đây để chọn!!!!")
                                .enableSwipeToDismiss()
                                .setTitleAppearance(R.style.text_title)
                                .setTextAppearance(R.style.text_subtitle_color)
                                .setBackgroundColorRes(R.color.color_red) // or setBackgroundColorInt(Color.CYAN)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        grpDate.callOnClick();
                                        if (Alerter.isShowing())
                                            Alerter.hide();
                                    }
                                })
                                .show();
                        return;
                    }
                    history.addHistory(mOriginAirport);
                    history.addHistory(mDestinationAirport);
                    //call api
                    String departDate = DateUtils.getSearchDate(mDepartDate);
                    grpLoadingScreen.setVisibility(View.VISIBLE);
                    ApiManager.getSearchResult(MainActivity.this, mOriginAirport.value, mDestinationAirport.value, departDate, "", adult, children, infant, DEFAULT_CURRENCY, selectedClassString, resultsCallBack);
                }
            }
        });

        sbSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sbSwitch.setChecked(false);
                if (mOriginAirport == null && mDestinationAirport == null)
                    return;
                if (mOriginAirport == null && mDestinationAirport != null) {
                    AutoCompleteAirport tempModel1 = null;
                    tempModel1 = mDestinationAirport;
                    mDestinationAirport = mOriginAirport;
                    mOriginAirport = tempModel1;
                    displayOriginAirport(mOriginAirport);
                    displayDestinationAirport(mDestinationAirport);
                    return;
                }
                AutoCompleteAirport tempModel = null;
                tempModel = mOriginAirport;
                mOriginAirport = mDestinationAirport;
                mDestinationAirport = tempModel;
                displayOriginAirport(mOriginAirport);
                displayDestinationAirport(mDestinationAirport);


            }
        });

    }

    public void initAirportEvent() {
        grpOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchAirportActivity.class);
                startActivityForResult(intent, ORIGIN_AIRPORT_REQUEST);
            }
        });

        grpDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchAirportActivity.class);
                startActivityForResult(intent, DESTINATION_AIRPORT_REQUEST);
            }
        });
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
                        displayScreen2Guide();
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
                        displayScreen3Guide();
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

    public void displayOriginAirport(AutoCompleteAirport model) {
        if (model == null) {
            tvOriginCode.setText(getResources().getString(R.string.code_holder));
            tvOriginAiportName.setText(getResources().getString(R.string.choose_airport));
            tvOriginCity.setText(getResources().getString(R.string.city_holder));
            return;
        }
        if (tvOriginCity == null || tvOriginAiportName == null || tvOriginCode == null || imgOriginCity == null)
            return;
        tvOriginCode3.setText(model.value);
        tvOriginAirport3.setText(model.airport_name);
        if (model.hasCityName) {

            tvOriginCity.setVisibility(View.VISIBLE);
            imgOriginCity.setVisibility(View.VISIBLE);


            tvOriginCode.setText(model.value);
            tvOriginAiportName.setText(model.airport_name);
            tvOriginCity.setText(model.city_name);


        } else {
            tvOriginCity.setVisibility(View.GONE);
            imgOriginCity.setVisibility(View.GONE);
            tvOriginCode.setText(model.value);
            tvOriginAiportName.setText(model.city_name);
        }
    }

    public void displayDestinationAirport(AutoCompleteAirport model) {
        if (model == null) {
            tvDestinationCode.setText(getResources().getString(R.string.code_holder));
            tvDestinationAirportName.setText(getResources().getString(R.string.choose_airport));
            tvDestinationCity.setText(getResources().getString(R.string.city_holder));
            return;
        }
        if (tvDestinationAirportName == null || tvDestinationCity == null || tvDestinationCode == null || imgDestinationCity == null)
            return;

        tvDestinationCode3.setText(model.value);
        tvDestinationAirport3.setText(model.airport_name);
        if (model.hasCityName) {
            tvDestinationCity.setVisibility(View.VISIBLE);
            imgDestinationCity.setVisibility(View.VISIBLE);

            tvDestinationCode.setText(model.value);
            tvDestinationAirportName.setText(model.airport_name);
            tvDestinationCity.setText(model.city_name);


        } else {
            tvDestinationCity.setVisibility(View.GONE);
            imgDestinationCity.setVisibility(View.GONE);

            tvDestinationCode.setText(model.value);
            tvDestinationAirportName.setText(model.airport_name);
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    // Callback
    //---------------------------------------------------------------------------------------------------------------------------------------------

    MyDataCallback<FlightResults> resultsCallBack = new MyDataCallback<FlightResults>() {
        @Override
        public void success(FlightResults flightResults) {
            if (flightResults == null)
                return;

            startResultActivity(flightResults);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    grpLoadingScreen.setVisibility(View.GONE);
                }
            }, 500);


        }

        @Override
        public void failure(Throwable t) {

            t.printStackTrace();
        }
    };

    public void startResultActivity(FlightResults flightResults) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra(ORIGIN_AIRPORT_MODEL, new Gson().toJson(mOriginAirport));
        intent.putExtra(DESTINATION_AIRPORT_MODEL, new Gson().toJson(mDestinationAirport));
        intent.putExtra(DEPART_DATE_MODEL, new Gson().toJson(mDepartDate));
        intent.putExtra(RETURN_DATE_MODEL, new Gson().toJson(mReturnDate));
        intent.putExtra(FLIGHT_CLASS_MODEL, new Gson().toJson(selectedClass));
        intent.putExtra(FLIGHT_RESULT_MODEL, new Gson().toJson(flightResults));
        intent.putExtra(FLIGHT_ADULT_STRING, adult);
        intent.putExtra(FLIGHT_CHILDREN_STRING, children);
        intent.putExtra(FLIGHT_INFANT_STRING, infant);
        if (rbId == R.id.rb_round_trip)
            intent.putExtra(FLIGHT_TYPE_STRING, DEFAULT_ROUND_TRIP);
        if (rbId == R.id.rb_one_way)
            intent.putExtra(FLIGHT_TYPE_STRING, DEFAULT_ONE_WAY);
        intent.putExtra(FLIGHT_CURRENCY_STRING, DEFAULT_CURRENCY);

        startActivity(intent);

    }

    public void displayScreen2Guide() {
        String isShown = CacheManager.getStringCacheData(GUIDE_SCREEN2);
        if (isShown != null)
            return;
        new TapTargetSequence(MainActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.grpOrigin), "Bấm vào đây để chọn sân bay đi")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.grpDestination), "Bấm vào đây để chọn sân bay đến")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.shine_button_switch), "Bấm vào đây để đổi giữa điểm đi và điểm đến")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.app_primary_color)
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
                        CacheManager.saveStringCacheData(GUIDE_SCREEN2, GUIDE_SHOWN_VALUE);
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

    public void displayScreen3Guide() {
        String isShown = CacheManager.getStringCacheData(GUIDE_SCREEN3);
        if (isShown != null)
            return;
        new TapTargetSequence(MainActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.rbg_fly_type), "Bấm vào đây để chọn loại chuyến bay bạn muốn. ")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.sp_class), "Bấm vào đây để chọn hạng bay.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.grp_date), "Bấm vào đây chọn thời gian cho chuyến bay.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.grpPassenger), "Chọn số lượng hành khách cho chuyến bay tại đây.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true).targetCircleColor(R.color.white),
                        TapTarget.forView(findViewById(R.id.floatingActionButton), "Bấm vào đây để bắt đầu tìm kiếm chuyến bay của bạn.")
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
                        CacheManager.saveStringCacheData(GUIDE_SCREEN3, GUIDE_SHOWN_VALUE);
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
    public void BackHistory(HistoryManager a){
        int i=a.getHistoryCount();
        if(i==0)return;
        History history=a.getHistoryById(i-1);
        History history1=a.getHistoryById(i);
        if(history==null||history1==null)return;
        mOriginAirport=new AutoCompleteAirport(history.getValue(),history.getAirport(),history.getCity_name());
        mDestinationAirport=new AutoCompleteAirport(history1.getValue(),history1.getAirport(),history1.getCity_name());
        if(mOriginAirport==null||mDestinationAirport==null)return;
        displayOriginAirport(mOriginAirport);
         displayDestinationAirport(mDestinationAirport);
    }
}
