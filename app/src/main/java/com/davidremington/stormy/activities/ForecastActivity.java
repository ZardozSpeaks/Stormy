package com.davidremington.stormy.activities;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import com.davidremington.stormy.R;
import com.davidremington.stormy.models.Forecast;
import com.davidremington.stormy.models.Hourly;
import com.davidremington.stormy.utils.Utils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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


    private Forecast forecast;
    private String location;
    private static Gson sGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String forecastJSON = extras.getString("forecastData");
        location = extras.getString("locality");
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);
        forecast = sGson.fromJson(forecastJSON, Forecast.class);
        updateDisplay();
    }

    private void updateDisplay() {
        try {
            Hourly current = forecast.currently;
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
            locationLabel.setText(location);
        } catch (ParseException e) {
            Timber.e(e.getMessage());
        }
    }

}
