package com.example.feverfinder.questions;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSLoc implements LocationListener {
    public void onLocationChanged(Location loc) {
        String message = String.format(
                "New Location \n Longitude: %1$s \n Latitude: %2$s",
                loc.getLongitude(), loc.getLatitude()
        );
        System.out.println(message);
    }
    public void onProviderDisabled(String arg0) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
