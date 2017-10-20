package com.davidremington.stormy.activities;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.davidremington.stormy.R;
import com.davidremington.stormy.exceptions.LocationNotFoundException;
import com.davidremington.stormy.exceptions.NullForecastException;
import com.davidremington.stormy.fragments.AlertDialogFragment;
import com.davidremington.stormy.models.Forecast;
import com.davidremington.stormy.models.Currently;
import com.davidremington.stormy.models.Location;
import com.davidremington.stormy.services.ForecastService;
import com.davidremington.stormy.services.GeoCoderService;
import com.davidremington.stormy.utils.Preferences;
import com.davidremington.stormy.utils.Utils;
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

import static com.davidremington.stormy.utils.Constants.CACHED_LOCATION;
import static java.util.Locale.US;

public class ForecastActivity extends AppCompatActivity {

    @BindView(R.id.temperatureLabel) TextView temperatureLabel;
    @BindView(R.id.timeLabel) TextView timeLabel;
    @BindView(R.id.locationLabel) TextView locationLabel;
    @BindView(R.id.iconImageView) ImageView iconImageView;
    @BindView(R.id.humidityLabel) TextView humidityLabel;
    @BindView(R.id.humidityValue) TextView humidityValue;
    @BindView(R.id.precipLabel) TextView precipLabel;
    @BindView(R.id.precipValue) TextView precipValue;
    @BindView(R.id.summaryTextView) TextView summaryTextView;
    @BindView(R.id.refreshImageView) ImageView refreshImageView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private Forecast forecast;
    private Location location;
    private static Gson sGson = new Gson();
    private static ForecastService sForecastService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String forecastJSON = extras.getString("forecastData");
        String locationJSON = extras.getString("location");
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.INVISIBLE);
        forecast = sGson.fromJson(forecastJSON, Forecast.class);
        location = sGson.fromJson(locationJSON, Location.class);
        sForecastService = ForecastService.getInstance();
        updateDisplay();
    }

    private void updateDisplay() {
        Currently current = forecast.currently;
        Double precipPercentage = Utils.decimalToPercentage(current.precipProbability);
        Drawable drawable = getResources().getDrawable(Utils.getIconId(current.icon));
        int temperature = Utils.roundToInt(current.temperature);
        String time = Utils.getFormattedTime(current.time, String.format("GMT%s", forecast.offset));
        temperatureLabel.setText(String.valueOf(temperature));
        timeLabel.setText(String.format(US, "At %s it will be", time));
        humidityValue.setText(String.valueOf(current.humidity));
        precipValue.setText(String.valueOf(Utils.roundToInt(precipPercentage)));
        summaryTextView.setText(current.summary);
        iconImageView.setImageDrawable(drawable);
        locationLabel.setText(location.name);
    }

    @OnClick(R.id.refreshImageView)
    public void refreshInformation() {
        try {
            toggleRefresh();
            String locationText = Preferences.getSettingsParam(CACHED_LOCATION);
            location = GeoCoderService.getLocation(
                    locationText,
                    this);
            if (location != null) {
                LatLng coordinates = location.point;
                sForecastService.getForecast(coordinates.latitude, coordinates.longitude, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        alertUserOfError();
                        toggleRefresh();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if (response.isSuccessful()) {
                                String forecastData = response.body().string();
                                Timber.v(forecastData);
                                forecast = sGson.fromJson(forecastData, Forecast.class);
                                runOnUiThread(() -> toggleRefresh());
                                runOnUiThread(() -> updateDisplay());
                            } else {
                                alertUserOfError();
                                runOnUiThread(() -> toggleRefresh());
                            }
                        } catch (IOException
                                | JsonParseException e) {
                            Timber.e(e);
                            runOnUiThread(() -> toggleRefresh());
                        }
                    }
                });
            } else {
                alertUserOfError();
                toggleRefresh();
            }
        } catch ( NullForecastException
                | NullPointerException
                | LocationNotFoundException e) {
            Timber.e(e);
            alertUserOfError();
            toggleRefresh();
        }
    }

    private void toggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void alertUserOfError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
