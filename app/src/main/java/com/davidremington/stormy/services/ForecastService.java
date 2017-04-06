package com.davidremington.stormy.stormy.services;

import com.davidremington.stormy.stormy.models.Forecast;
import com.davidremington.stormy.stormy.utils.Constants;
import com.davidremington.stormy.stormy.utils.NullForecastError;
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
    private static final String API_KEY = Constants.API_KEY;

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
