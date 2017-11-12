package com.httt.uit.travel_easy_android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;

/**
 * Created by TuanAnh on 11/4/17.
 */

public class PickDateActivity extends AppCompatActivity {
    public static final String DEPART_DATE_DATE = "DEPART_DATE_DATE";
    public static final String RETURN_DATE_DATE = "RETURN_DATE_DATE";
    private CalendarPickerView mCalendarPickerView;
    private ShineButton shineButton;
    private Date departDate;
    private Date returnDate;
    private IconTextView tvDepart;
    private IconTextView tvDepart2;
    private IconTextView tvReturn;
    private RelativeLayout rlRoundTrip;
    private RelativeLayout rlOneWay;
    private boolean isRoundTrip = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_date_layout);
        LGSnackbarManager.prepare(getApplicationContext(),
                LGSnackBarThemeManager.LGSnackbarThemeName.SHINE);
        overridePendingTransition(R.anim.appearance_go_in, R.anim.appearance_go_out);
        Iconify.with(new FontAwesomeModule());
        final Calendar next3Year = Calendar.getInstance();
        next3Year.add(Calendar.YEAR, 30);

        final Calendar currentYear = Calendar.getInstance();


        initView();
        initEvent();

        Intent intent = getIntent();
        if (intent == null)
            return;
        int type = intent.getIntExtra(MainActivity.FLIGHT_TYPE_CODE, 0);
        if (type == 0)
            return;
        if (type == MainActivity.ONE_WAY_TYPE) {
            isRoundTrip = false;
            oneWayUI();
            mCalendarPickerView.init(currentYear.getTime(), next3Year.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withSelectedDate(new Date());
        }
        if (type == MainActivity.ROUND_TRIP_TYPE) {
            isRoundTrip = true;
            roundTripUI();
            mCalendarPickerView.init(currentYear.getTime(), next3Year.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDate(new Date());
        }

        Long departTime = intent.getLongExtra(DEPART_DATE_DATE, 0);
        if (departTime == 0)
            return;
        Date departure = new Date();
        departure.setTime(departTime);
        departDate = departure;
        mCalendarPickerView.selectDate(departure);
        if (isRoundTrip)
            displayDepartDate(departure);
        if (!isRoundTrip)
            displayDepartDate2(departure);

        Long returnTime = intent.getLongExtra(RETURN_DATE_DATE, 0);
        if (returnTime == 0) {
            oneWayUI();
            return;
        }
        Date returnD = new Date();
        returnD.setTime(returnTime);
        returnDate = returnD;

        displayReturnDate(returnD);
        mCalendarPickerView.selectDate(returnD);

    }

    public void initView() {
        mCalendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        shineButton = (ShineButton) findViewById(R.id.shine_button);
        tvDepart = (IconTextView) findViewById(R.id.tv_date_depart);
        tvReturn = (IconTextView) findViewById(R.id.tv_date_return);
        tvDepart2 = (IconTextView) findViewById(R.id.tv_date_depart2);
        rlOneWay = (RelativeLayout) findViewById(R.id.rl_one_way);
        rlRoundTrip = (RelativeLayout) findViewById(R.id.rl_round_trip);
    }

    public void initEvent() {

        //must have to prevent crash
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);

        mCalendarPickerView.deactivateDates(list);

        mCalendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (departDate != null && returnDate == null) {
                    displayReturnDate(date);
                }

                if (departDate == null && returnDate == null) {
                    if (isRoundTrip)
                        displayDepartDate(date);
                    if (!isRoundTrip)
                        displayDepartDate2(date);
                }

            }

            @Override
            public void onDateUnselected(Date date) {
                resetReturnDate();
                resetDepartDate();


            }
        });


        shineButton.init(this);
        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishMyActivity();
                    }
                }, 600);

            }
        });
    }

    public void finishMyActivity() {
        if (isRoundTrip) {
            if (departDate == null || returnDate == null) {
                LGSnackbarManager.show(LGSnackBarTheme.SnackbarStyle.ERROR, "Chưa chọn đủ ngày!!! Vui lòng chọn lại");
                shineButton.setChecked(false);
                return;
            }
        }
        if (!isRoundTrip) {
            if (departDate == null) {
                LGSnackbarManager.show(LGSnackBarTheme.SnackbarStyle.ERROR, "Chưa chọn đủ ngày!!! Vui lòng chọn lại");
                shineButton.setChecked(false);
                return;
            }
        }

        Intent data = new Intent();
        data.putExtra(DEPART_DATE_DATE, departDate.getTime());
        if (returnDate != null)
            data.putExtra(RETURN_DATE_DATE, returnDate.getTime());
        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(R.anim.appearance_back_in, R.anim.appearance_back_out);
    }

    @Override
    public void onBackPressed() {
        finishMyActivity();
    }

    public static void setViewAnimation(Context context, View view, int animation) {
        view.setAnimation(AnimationUtils.loadAnimation(context, animation));
    }

    public void displayDepartDate(Date date) {
        roundTripUI();
        departDate = date;
        String displayDate = DateUtils.getDateDisplay(date);
        tvDepart.setText(displayDate);
        setViewAnimation(PickDateActivity.this, tvDepart, android.R.anim.fade_in);
    }

    public void displayDepartDate2(Date date) {
        oneWayUI();
        departDate = date;
        String displayDate = DateUtils.getDateDisplay(date);
        tvDepart2.setText(displayDate);
        setViewAnimation(PickDateActivity.this, tvDepart2, android.R.anim.fade_in);
    }

    public void displayReturnDate(Date date) {

        returnDate = date;
        String displayDate = DateUtils.getDateDisplay(date);
        tvReturn.setText(displayDate);
        setViewAnimation(PickDateActivity.this, tvReturn, android.R.anim.fade_in);
    }

    public void resetDepartDate() {
        departDate = null;
        tvDepart.setText(getResources().getString(R.string.fa_custom_pick_date));
        setViewAnimation(PickDateActivity.this, tvDepart, android.R.anim.fade_out);

    }

    public void resetReturnDate() {
        returnDate = null;
        tvReturn.setText(getResources().getString(R.string.fa_custom_pick_date));
        setViewAnimation(PickDateActivity.this, tvReturn, android.R.anim.fade_out);
    }

    public void roundTripUI() {
        rlOneWay.setVisibility(View.GONE);
        rlRoundTrip.setVisibility(View.VISIBLE);
    }

    public void oneWayUI() {
        rlOneWay.setVisibility(View.VISIBLE);
        rlRoundTrip.setVisibility(View.GONE);
    }
}
