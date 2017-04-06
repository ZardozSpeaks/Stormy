package com.davidremington.stormy.services;

import android.util.Log;

import com.davidremington.stormy.models.Forecast;
import com.davidremington.stormy.utils.Constants;
import com.davidremington.stormy.utils.NullForecastError;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastService {

    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();
    private static ForecastService instance = null;

    private static final String ROOT_FORECAST_URL = "https://api.darksky.net/forecast";
    private static final String TAG = ForecastService.class.getSimpleName();
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

    public Forecast[] getForecast(double latitude, double longitude) throws NullForecastError {
        final Forecast[] forecast = new Forecast[1];
        Request request = new Request.Builder()
                .url(getForecastUrl(latitude, longitude))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()){
                        String forecastData = response.body().string();
                        Log.v(TAG, forecastData);
                        forecast[0] = gson.fromJson(forecastData, Forecast.class);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception: ", e);
                } catch (JsonParseException e) {
                    Log.e(TAG, "Exception: ", e);
                }
            }
        });

        waitOnResponse(forecast);
        return forecast;
    }

    private String getForecastUrl(double latitude, double longitude) {
        return String.format("%s/%s/%s,%s", ROOT_FORECAST_URL, API_KEY, latitude, longitude);
    }

    private void waitOnResponse(Forecast[] forecast) {
        int counter = 0;
        while(forecast[0] == null && counter < 10){
            try {
                Thread.sleep(1000);
                counter ++;
            } catch (InterruptedException e){
                Log.e(TAG, "Exception: ", e);
            }
        }
    }
}
