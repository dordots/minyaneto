package com.app.minyaneto_android.location;

import java.util.TimeZone;

public interface LocationProvider {
    TimeZone getTimeZone();

    double getLatitude();

    double getLongitude();
}
