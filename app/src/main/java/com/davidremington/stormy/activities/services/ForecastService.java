package com.davidremington.stormy.activities.services;


import com.davidremington.stormy.BuildConfig;
import com.davidremington.stormy.activities.models.Forecast;
import com.davidremington.stormy.activities.utils.NullForecastError;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastService {

    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();
    private static ForecastService instance = null;

    private static final String ROOT_FORECAST_URL = "https://api.darksky.net/forecast";
    private static final String API_KEY = BuildConfig.API_KEY;

    private ForecastService() {
        //method to prevent instantiation
    }

    public static ForecastService getInstance() {
        if(instance == null) {
            instance = new ForecastService();
        }
        return instance;
    }

    public ArrayList<Forecast> getForecast(double latitude, double longitude) throws NullForecastError {
        ArrayList<Forecast> ret = new ArrayList<>();
        Response response;
        Request request = new Request.Builder()
                .url(getForecastUrl(latitude, longitude))
                .build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (java.io.IOException e) {
            throw new NullForecastError();
        }
        System.out.println(response);
        return ret;
    }

    private String getForecastUrl(double latitude, double longitude) {
        return String.format("%s/%s/%s,%s", ROOT_FORECAST_URL, API_KEY, latitude, longitude);
    }
}
