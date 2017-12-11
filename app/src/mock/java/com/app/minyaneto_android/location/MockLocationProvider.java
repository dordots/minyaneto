package com.app.minyaneto_android.location;

import java.util.TimeZone;

public class MockLocationProvider implements LocationProvider {
    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone("GMT+2:00");
    }

    @Override
    public double getLatitude() {
        return 31.768;
    }

    @Override
    public double getLongitude() {
        return 35.214;
    }
}
