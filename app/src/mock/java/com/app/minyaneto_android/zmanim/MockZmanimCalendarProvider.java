package com.app.minyaneto_android.zmanim;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Calendar;

public class MockZmanimCalendarProvider extends ZmanimCalendarProvider {
    @Override
    public ComplexZmanimCalendar getCzc(GeoLocation location) {
        ComplexZmanimCalendar calendar = new ComplexZmanimCalendar(location);
        calendar.getCalendar().set(2017, Calendar.DECEMBER, 11);
        return calendar;
    }
}
