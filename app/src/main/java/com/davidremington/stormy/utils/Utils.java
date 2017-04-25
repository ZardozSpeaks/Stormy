package com.davidremington.stormy.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static String getFormattedTime(Long timestamp, String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        return formatter.format(new Date(timestamp * 1000));
    }
}
