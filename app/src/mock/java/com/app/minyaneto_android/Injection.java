package com.app.minyaneto_android;

import android.content.Context;

import com.app.minyaneto_android.location.LocationProvider;
import com.app.minyaneto_android.location.MockLocationProvider;
import com.app.minyaneto_android.zmanim.MockZmanimCalendarProvider;
import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;

public class Injection {
    public static ZmanimCalendarProvider getZmanimCalendarProvider() {
        return new MockZmanimCalendarProvider();
    }

    public static LocationProvider getLocationProvider(Context context) {
        return new MockLocationProvider();
    }
}
