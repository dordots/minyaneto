package com.app.minyaneto_android;

import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;

public class Injection {
    public static ZmanimCalendarProvider getZmanimCalendarProvider() {
        return new ZmanimCalendarProvider();
    }
}
