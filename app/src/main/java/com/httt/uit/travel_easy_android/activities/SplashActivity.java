package com.httt.uit.travel_easy_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.R;

/**
 * Created by TuanAnh on 11/28/17.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 3000);

        //finish activity so it wont return here when user click back button
        finish();
    }
}
