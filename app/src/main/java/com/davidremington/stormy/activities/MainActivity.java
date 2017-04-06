package com.davidremington.stormy.stormy.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.davidremington.stormy.stormy.services.ForecastService;
import com.davidremington.stormy.stormy.utils.NullForecastError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static Gson gson = new Gson();
    private static ForecastService forecastService;

    @BindView(R.id.getLocationButton) Button locationButton;
    @BindView(R.id.locationEditText) EditText locationText;
    @BindView(R.id.errorTextView) TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        forecastService = ForecastService.getInstance();
    }

    public void getForecast() {
        LatLng coordinates = getLocationCoordinates();
        try {
            forecastService.getForecast(coordinates.latitude, coordinates.longitude);
        } catch (NullForecastError e) {

        }
    }

    private LatLng getLocationCoordinates() {
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        LatLng point = null;

        try {
            addresses = coder.getFromLocationName(locationText.getText().toString(), 1);
            if(addresses == null) {
                return null;
            }
            Address location = addresses.get(0);
            point = new LatLng((double) (location.getLatitude()),
                              ((double) (location.getLongitude())));
        } catch (IOException e) {
            return null;
        }
        return point;
    }

}
