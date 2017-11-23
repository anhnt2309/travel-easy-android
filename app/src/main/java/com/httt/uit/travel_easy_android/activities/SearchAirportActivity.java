package com.httt.uit.travel_easy_android.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.arlib.floatingsearchview.FloatingSearchView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.adapters.AutocompleteAdapter;
import com.httt.uit.travel_easy_android.manager.ApiManager;
import com.httt.uit.travel_easy_android.manager.CacheManager;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.request.MyDataCallback;
import com.httt.uit.travel_easy_android.utils.StringUtils;
import com.joanzapata.iconify.widget.IconTextView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;

/**
 * Created by TuanAnh on 11/5/17.
 */

public class SearchAirportActivity extends AppCompatActivity {
    public static final String RESULT_MODEL = "RESULT_MODEL";
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;


    private FloatingSearchView searchView;
    private ShineButton shineButton;
    private ShineButton sbVN;
    private ListView lvFound;
    private ListView lvNearBy;
    private ArrayList<AutoCompleteAirport> mModels;
    private ArrayList<AutoCompleteAirport> mNearModels;
    private AutocompleteAdapter foundAdapter;
    private AutocompleteAdapter nearByAdapter;
    private LocationManager mLocationManager;
    private IconTextView itvNear;
    private IconTextView itvFound;
    private TextView tvVN;
    private boolean isNewInput = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                LGSnackbarManager.show(LGSnackBarTheme.SnackbarStyle.WARNING, "Vui lòng cấp quyền truy cập địa  điểm cho ứng dụng để chúng tôi có thể tìm các sân bay quanh bạn");
//                requestLocationPermission();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_airport);
        LGSnackbarManager.prepare(getApplicationContext(),
                LGSnackBarThemeManager.LGSnackbarThemeName.SHINE);
        overridePendingTransition(R.anim.appearance_go_in, R.anim.appearance_go_out);
        initUI();
        initEvent();
        searchViewSetup();
        displayGuide();
        getNearestAirports();

    }

    public void initUI() {
        searchView = (FloatingSearchView) findViewById(R.id.fsv_search);
        shineButton = (ShineButton) findViewById(R.id.shine_button);
        lvFound = (ListView) findViewById(R.id.lv_found_airport);
        lvNearBy = (ListView) findViewById(R.id.lv_nearby_airport);
        sbVN = (ShineButton) findViewById(R.id.shine_button_VN);
        itvFound = (IconTextView) findViewById(R.id.itv_found);
        itvNear = (IconTextView) findViewById(R.id.itv_near);
        tvVN = (TextView) findViewById(R.id.tv_VN);
    }

    public void initEvent() {
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
        mModels = new ArrayList<>();
        foundAdapter = new AutocompleteAdapter(SearchAirportActivity.this, 0, mModels, false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lvFound != null)
                    lvFound.setAdapter(foundAdapter);
            }
        });

        mNearModels = new ArrayList<>();
        nearByAdapter = new AutocompleteAdapter(SearchAirportActivity.this, 0, mNearModels, true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lvNearBy != null)
                    lvNearBy.setAdapter(nearByAdapter);
            }
        });

        sbVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchView.getQuery();
                performAutocomplete(query);
            }
        });

        lvFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mModels == null || mModels.size() == 0)
                    return;
                ShineButton sbCheck = view.findViewById(R.id.shine_button);
                sbCheck.callOnClick();
                AutoCompleteAirport model = mModels.get(i);
                String modelString = new Gson().toJson(model);
                Intent data = new Intent();
                data.putExtra(RESULT_MODEL, modelString);
                setResult(RESULT_OK, data);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishMyActivity();
                    }
                }, 600);

            }
        });


        lvNearBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mNearModels == null || mNearModels.size() == 0)
                    return;
                ShineButton sbCheck = view.findViewById(R.id.shine_button);
                sbCheck.callOnClick();
                AutoCompleteAirport model = mNearModels.get(i);
                String modelString = new Gson().toJson(model);
                Intent data = new Intent();
                data.putExtra(RESULT_MODEL, modelString);
                setResult(RESULT_OK, data);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishMyActivity();
                    }
                }, 600);
            }
        });
        tvVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sbVN.callOnClick();
            }
        });
    }

    private void searchViewSetup() {
        if (this.searchView == null)
            return;

        Drawable mBackgroundDrawable = new ColorDrawable(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            this.searchView.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            this.searchView.setBackground(mBackgroundDrawable);
        }

        searchView.setDismissOnOutsideClick(true);
        searchView.setShowSearchKey(true);

        this.searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                performAutocomplete(newQuery);
            }
        });
        this.searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onFocusCleared() {
                mModels.clear();
                refreshFoundListView();

            }
        });
    }

    public void finishMyActivity() {

        finish();
        overridePendingTransition(R.anim.appearance_back_in, R.anim.appearance_back_out);
    }

    @Override
    public void onBackPressed() {
        finishMyActivity();
    }

    public void refreshFoundListView() {
        if (mModels == null || mModels.size() == 0) {
            itvFound.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(SearchAirportActivity.this, android.R.anim.fade_in);

            itvFound.setAnimation(animation);
        } else {
            itvFound.setVisibility(View.GONE);
            itvFound.setAnimation(AnimationUtils.loadAnimation(SearchAirportActivity.this, android.R.anim.fade_out));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foundAdapter.notifyDataSetChanged();
            }
        });
    }

    public void refreshNearListView() {
        if (mNearModels == null || mNearModels.size() == 0) {
            itvNear.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(SearchAirportActivity.this, android.R.anim.fade_in);

            itvNear.setAnimation(animation);
        } else {
            itvNear.setVisibility(View.GONE);
            itvNear.setAnimation(AnimationUtils.loadAnimation(SearchAirportActivity.this, android.R.anim.fade_out));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nearByAdapter.notifyDataSetChanged();
            }
        });
    }

    public void performAutocomplete(String query) {
        String countryCode = null;
        if (sbVN.isChecked())
            countryCode = "VN";
        mModels.clear();

        refreshFoundListView();

        ApiManager.getAutoCompleteAirport(SearchAirportActivity.this, query, countryCode, autoCompleteCallback);

    }

    public MyDataCallback<ArrayList<AutoCompleteAirport>> autoCompleteCallback = new MyDataCallback<ArrayList<AutoCompleteAirport>>() {
        @Override
        public void success(ArrayList<AutoCompleteAirport> models) {
            if (models == null || models.size() == 0)
                return;
            for (AutoCompleteAirport model : models) {
                StringUtils.parseAutoCompleteModel(model);
                Log.d("abc", "" + model.city_name + "    " + model.airport_name);
            }
            mModels.clear();
            mModels.addAll(models);
            refreshFoundListView();
        }

        @Override
        public void failure(Throwable t) {
            t.printStackTrace();
        }
    };

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(SearchAirportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return;
        } else {
            getCurrentLocation();
        }
    }


    public void getCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            callNearByArportApi(location);
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    public void getNearestAirports() {
        requestLocationPermission();

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            callNearByArportApi(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void callNearByArportApi(Location location) {
        ApiManager.getNearestAirports(SearchAirportActivity.this, location.getLatitude(), location.getLongitude(), new MyDataCallback<ArrayList<AutoCompleteAirport>>() {
            @Override
            public void success(ArrayList<AutoCompleteAirport> models) {
                if (models == null)
                    return;

                Log.e("a1234", "" + models.size());
                mLocationManager.removeUpdates(locationListener);
                for (AutoCompleteAirport model : models) {
                    if (model.city_name != null || !model.city_name.equals("")) {
                        model.setHasCityName(true);
                    }
                    if (model.airport != null)
                        model.value = model.airport;
                }
                mNearModels.clear();
                mNearModels.addAll(models);
                refreshNearListView();
            }

            @Override
            public void failure(Throwable t) {

            }
        });
    }


    public void displayGuide() {
        String isShown = CacheManager.getStringCacheData(MainActivity.GUIDE_SCREEN_SEARCH);
        if (isShown != null)
            return;

        new TapTargetSequence(SearchAirportActivity.this)
                .targets(
                        TapTarget.forView(findViewById(R.id.grpSearchView), "Nhập tên sân bay cần tìm ở đây.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.transparent)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.grpOnly), "Chọn ở đây nếu bạn chỉ muốn tìm sân bay của Việt Nam.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.app_primary_color)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.grpNear), "Danh sách sân bay gần bạn sẽ được hiển thị ở đây. \nBấm vào một sân bay để chọn.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.app_primary_color)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.grpFound), "Danh sách sân bay tìm được sẽ được hiển thị ở đây. \nBấm vào một sân bay để chọn.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.app_primary_color)
                                .targetCircleColor(R.color.divider_color)
                                .textColor(android.R.color.white)
                                .cancelable(false)
                                .drawShadow(true),
                        TapTarget.forView(findViewById(R.id.shine_button), "Bấm vào đây để trở về màn hình chính")
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
                        CacheManager.saveStringCacheData(MainActivity.GUIDE_SCREEN_SEARCH, MainActivity.GUIDE_SHOWN_VALUE);
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
}
