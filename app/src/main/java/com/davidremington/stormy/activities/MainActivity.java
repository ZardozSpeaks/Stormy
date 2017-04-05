package com.davidremington.stormy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.davidremington.stormy.R;
import com.davidremington.stormy.activities.services.ForecastService;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static Gson gson = new Gson();
    private static ForecastService forecastService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forecastService = ForecastService.getInstance();
    }
}
