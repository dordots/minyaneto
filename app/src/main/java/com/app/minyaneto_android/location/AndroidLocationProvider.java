package com.app.minyaneto_android.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.TimeZone;

public class AndroidLocationProvider implements LocationProvider {

    private final Context context;
    private final Location location;

    public AndroidLocationProvider(Context context) {
        this.context = context;
        LocationManager lm = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (location == null) {
//            longitude = (35.214);
//            latitude = 31.768;
//        }
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
