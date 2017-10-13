package com.davidremington.stormy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.davidremington.stormy.BuildConfig;
import com.davidremington.stormy.R;
import com.davidremington.stormy.exceptions.LocationNotFoundException;
import com.davidremington.stormy.fragments.AlertDialogFragment;
import com.davidremington.stormy.models.Location;
import com.davidremington.stormy.services.ForecastService;
import com.davidremington.stormy.services.GeoCoderService;
import com.davidremington.stormy.utils.ApplicationContextProvider;
import com.davidremington.stormy.exceptions.NullForecastException;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private Location location;

    private static ForecastService sForecastService;
    private static Gson sGson = new Gson();

    @BindView(R.id.getLocationButton) Button getLocationButton;
    @BindView(R.id.locationEditText) EditText locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        ApplicationContextProvider.setContext(context);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sForecastService = ForecastService.getInstance();
        if(BuildConfig.DEBUG && Timber.forest().isEmpty()) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @OnClick(R.id.getLocationButton)
    public void getForecast() {
        try {
             location = GeoCoderService.getLocation(
                    locationText.getText().toString().toLowerCase(),
                    this);
            if (location != null) {
                LatLng coordinates = location.point;
                sForecastService.getForecast(coordinates.latitude, coordinates.longitude, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        alertUserOfError();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if (response.isSuccessful()) {
                                String forecastData = response.body().string();
                                Timber.v(forecastData);
                                sendDataToForecastActivity(MainActivity.this, forecastData);
                            } else {
                                alertUserOfError();
                            }
                        } catch (IOException
                                | JsonParseException e) {
                            Timber.e(e);
                        }
                    }
                });
            } else {
                alertUserOfError();
            }
        } catch ( NullForecastException
                | NullPointerException
                | LocationNotFoundException e) {
            Timber.e(e);
            alertUserOfError();
        }
    }


    private void sendDataToForecastActivity(Context context, String forecastData){
        Intent intent = new Intent(context, ForecastActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("forecastData", forecastData);
        bundle.putString("location", sGson.toJson(location));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void alertUserOfError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }


}
