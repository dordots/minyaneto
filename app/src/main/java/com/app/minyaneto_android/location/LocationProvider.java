package com.app.minyaneto_android.location;

import android.location.Location;

import java.util.TimeZone;

public interface LocationProvider {
    TimeZone getTimeZone();

    Location getLocation();
}
