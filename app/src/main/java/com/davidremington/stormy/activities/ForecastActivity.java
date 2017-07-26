package com.davidremington.stormy.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidremington.stormy.models.Forecast;
import com.davidremington.stormy.utils.ApplicationContextProvider;
import com.google.gson.Gson;

import butterknife.ButterKnife;

public class ForecastActivity extends AppCompatActivity {

    private Forecast mForecast;
    private static Gson sGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        ApplicationContextProvider.setContext(context);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);
        mForecast = sGson.fromJson(extras.getString("forecastData"), Forecast.class);
    }
}
