package com.app.minyaneto_android.location;

import android.location.Location;

/**
 * Created by admin on 11/02/2018.
 */

public class LocationRepository {
    private static LocationRepository instance = new LocationRepository();
    private Location lastKnownLocation;

    public static LocationRepository getInstance() {
        return instance;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }
}
