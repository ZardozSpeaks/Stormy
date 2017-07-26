package com.davidremington.stormy.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.davidremington.stormy.fragments.AlertDialogFragment;
import com.davidremington.stormy.services.ForecastService;
import com.davidremington.stormy.utils.ApplicationContextProvider;
import com.davidremington.stormy.utils.NullForecastError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonParseException;


import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

import static com.davidremington.stormy.utils.ApplicationContextProvider.getContext;

public class MainActivity extends AppCompatActivity {

    private static ForecastService sForecastService;

    @Bind(R.id.getLocationButton) Button locationButton;
    @Bind(R.id.locationEditText) EditText locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        ApplicationContextProvider.setContext(context);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sForecastService = ForecastService.getInstance();
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @OnClick(R.id.getLocationButton)
    public void getForecast() {
        LatLng coordinates = getLocationCoordinates();
        try {
            sForecastService.getForecast(coordinates.latitude, coordinates.longitude, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        alertUserOfError();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if(response.isSuccessful()){
                                String forecastData = response.body().string();
                                Timber.v(forecastData);
                                sendDataToForecastActivity(MainActivity.this, forecastData);
                            } else {
                                alertUserOfError();
                            }
                        } catch (IOException | JsonParseException e) {
                            Timber.e(e);
                        }
                    }
            });
        } catch (NullForecastError | NullPointerException e) {
            Timber.e(e);
            alertUserOfError();
        }
    }


    private void sendDataToForecastActivity(Context context, String forecastData){
        Intent intent = new Intent(context, ForecastActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("forecastData", forecastData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void alertUserOfError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private LatLng getLocationCoordinates() {
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        LatLng point;

        try {
            addresses = coder.getFromLocationName(locationText.getText().toString().toLowerCase(), 5);
            if(addresses == null) {
                return null;
            }
            Address location = addresses.get(0);
            point = new LatLng(location.getLatitude(),
                              (location.getLongitude()));
        } catch (IOException e) {
            alertUserOfError();
            return null;
        }
        return point;
    }
}
