package com.davidremington.stormy.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.davidremington.stormy.models.Forecast;
import com.davidremington.stormy.utils.ApplicationContextProvider;
import com.davidremington.stormy.utils.Constants;
import com.davidremington.stormy.utils.NullForecastError;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ForecastService {

    private static OkHttpClient sClient = new OkHttpClient();
    private static ForecastService sInstance = null;

    private static final String ROOT_FORECAST_URL = "https://api.darksky.net/forecast";
    private static final String API_KEY = Constants.API_KEY;

    private ForecastService() {
        //method to prevent instantiation
    }

    public static ForecastService getInstance() {
        if(sInstance == null) {
            sInstance = new ForecastService();
        }
        return sInstance;
    }

    public void getForecast(double latitude, double longitude, Callback callback) throws NullForecastError {
        final Forecast[] forecast = new Forecast[1];
        Request request = new Request.Builder()
                .url(getForecastUrl(latitude, longitude))
                .build();
        Call call = sClient.newCall(request);
        if(isNetworkAvailable()) {
            call.enqueue(callback);
        } else {
            callback.onFailure(call, new IOException());
        }
    }


    private String getForecastUrl(double latitude, double longitude) {
        return String.format("%s/%s/%s,%s", ROOT_FORECAST_URL, API_KEY, latitude, longitude);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                ApplicationContextProvider.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
