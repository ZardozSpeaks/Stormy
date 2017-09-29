package com.davidremington.stormy.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidremington.stormy.R;
import com.davidremington.stormy.models.Forecast;
import com.google.gson.Gson;

import butterknife.ButterKnife;

public class ForecastActivity extends AppCompatActivity {

    private Forecast mForecast;
    private static Gson sGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String forecastJSON = extras.getString("forecastData");
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);
        mForecast = sGson.fromJson(forecastJSON, Forecast.class);
    }
}
