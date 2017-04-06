package com.davidremington.stormy.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.davidremington.stormy.services.ForecastService;
import com.davidremington.stormy.utils.NullForecastError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static Gson gson = new Gson();
    private static ForecastService forecastService;

    @Bind(R.id.getLocationButton) Button locationButton;
    @Bind(R.id.locationEditText) EditText locationText;
    @Bind(R.id.errorTextView) TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        forecastService = ForecastService.getInstance();
    }

    @OnClick(R.id.getLocationButton)
    public void getForecast(View view) {
        LatLng coordinates = getLocationCoordinates();
        try {
            forecastService.getForecast(coordinates.latitude, coordinates.longitude);
        } catch (NullForecastError e) {
            errorTextView.setText(getString(R.string.no_location_found));
        }
    }

    private LatLng getLocationCoordinates() {
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        LatLng point;

        try {
            addresses = coder.getFromLocationName(locationText.getText().toString(), 5);
            if(addresses == null) {
                return null;
            }
            Address location = addresses.get(0);
            point = new LatLng(location.getLatitude(),
                              (location.getLongitude()));
        } catch (IOException e) {
            return null;
        }
        return point;
    }

}
