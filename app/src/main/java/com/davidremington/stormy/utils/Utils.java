package com.davidremington.stormy.utils;


import com.davidremington.stormy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

public class Utils {

    public static String getFormattedTime(Long timestamp, String timezone) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        return formatter.format((timestamp * 1000));
    }

    public static int roundToInt(Double value) {
        return (int) Math.round(value);
    }

    public static Double decimalToPercentage(Double value) {
        return value * 100;
    }

    public static int getIconId(String iconName) {
        int iconId;
        switch(iconName) {
            case "clear-day":
                iconId = R.mipmap.clear_day;
                break;
            case "clear-night":
                iconId = R.mipmap.clear_night;
                break;
            case "rain":
                iconId = R.mipmap.rain;
                break;
            case "snow":
                iconId = R.mipmap.snow;
                break;
            case "sleet":
                iconId = R.mipmap.sleet;
                break;
            case "wind":
                iconId = R.mipmap.wind;
                break;
            case "fog":
                iconId = R.mipmap.fog;
                break;
            case "cloudy":
                iconId = R.mipmap.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.mipmap.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.mipmap.cloudy_night;
                break;
            default:
                iconId = R.mipmap.clear_day;
                Timber.e(String.format("unknown iconName %s passed to method", iconName));
                break;
        }
        return iconId;
    }
}
