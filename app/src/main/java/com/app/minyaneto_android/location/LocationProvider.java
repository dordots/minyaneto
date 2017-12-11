package com.app.minyaneto_android.location;

import android.arch.lifecycle.LiveData;
import android.location.Location;

import java.util.TimeZone;

public interface LocationProvider {
    TimeZone getTimeZone();

    LiveData<Location> getLocation();
}
