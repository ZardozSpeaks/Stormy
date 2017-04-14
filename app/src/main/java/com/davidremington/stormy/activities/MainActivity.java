package com.davidremington.stormy.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.davidremington.stormy.fragments.AlertDialogFragment;
import com.davidremington.stormy.models.Forecast;
import com.davidremington.stormy.services.ForecastService;
import com.davidremington.stormy.utils.NullForecastError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;


import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static ForecastService sForecastService;
    private static Gson sGson;
    private Forecast mForcast;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.getLocationButton) Button locationButton;
    @Bind(R.id.locationEditText) EditText locationText;
    @Bind(R.id.errorTextView) TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sGson = new Gson();
        sForecastService = ForecastService.getInstance();
    }

    @OnClick(R.id.getLocationButton)
    public void getForecast(View view) {
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
                                Log.v(TAG, forecastData);
                                mForcast = sGson.fromJson(forecastData, Forecast.class);
                            } else {
                                alertUserOfError();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Exception: ", e);
                        } catch (JsonParseException e) {
                            Log.e(TAG, "Exception: ", e);
                        }
                    }
            });
        } catch (NullForecastError e) {
            errorTextView.setText(getString(R.string.no_location_found));
        } catch (NullPointerException e) {
            errorTextView.setText(getString(R.string.no_results));
        }
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
