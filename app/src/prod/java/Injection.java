package com.app.minyaneto_android;

import android.content.Context;

import com.app.minyaneto_android.location.AndroidLocationProvider;
import com.app.minyaneto_android.location.LocationProvider;
import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;

public class Injection {
    public static ZmanimCalendarProvider getZmanimCalendarProvider() {
        return new ZmanimCalendarProvider();
    }

    public static LocationProvider getLocationProvider(Context context) {
        return new AndroidLocationProvider(context);
    }
}
