package com.app.minyaneto_android;

import android.content.Context;

import com.app.minyaneto_android.location.AndroidLocationProvider;
import com.app.minyaneto_android.location.LocationProvider;
import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;

public class Injection {
    private static Context context;

    public static ZmanimCalendarProvider getZmanimCalendarProvider() {
        return new ZmanimCalendarProvider();
    }

    public static LocationProvider getLocationProvider(Context context) {
        return new AndroidLocationProvider(context);
    }

    public static LocationProvider getLocationProvider() {
        return new AndroidLocationProvider(context);
    }

    public static void setContext(Context context) {
        Injection.context = context;
    }
}
