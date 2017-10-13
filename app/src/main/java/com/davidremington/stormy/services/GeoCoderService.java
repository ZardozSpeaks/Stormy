package com.davidremington.stormy.services;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.davidremington.stormy.exceptions.LocationNotFoundException;
import com.davidremington.stormy.models.Location;
import com.davidremington.stormy.utils.Preferences;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import static com.davidremington.stormy.utils.Constants.CACHED_LOCATION;

public class GeoCoderService {

    public static Location getLocation(String locationName, Context context) throws LocationNotFoundException {
        cacheLocation(locationName);
        Geocoder coder = new Geocoder(context);
        List<Address> addresses;
        Location location = new Location();

        try {
            addresses = coder.getFromLocationName(locationName, 5);
            if(addresses == null) {
                return null;
            }
            Address city = addresses.get(0);
            location.name = String.format("%s, %s", city.getLocality(), city.getAdminArea());
            location.point = new LatLng(city.getLatitude(),
                    (city.getLongitude()));
        } catch (IOException e) {
            throw new LocationNotFoundException();
        }
        return location;
    }

    private static void cacheLocation(String locationName) {
        Preferences.setSettingsParam(CACHED_LOCATION, locationName);
    }

}
