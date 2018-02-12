package com.app.minyaneto_android.models.time;

import android.location.Location;
import android.support.annotation.NonNull;

import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Date;
import java.util.TimeZone;

public class TimeUtility {

    public static ExactTime extractSpecificTime(PrayTime prayTime, Location location) {
        return extractSpecificTime(prayTime, new ZmanimCalendarProvider(), location);
    }

    public static ExactTime extractSpecificTime(PrayTime prayTime, ZmanimCalendarProvider provider, Location location) {
        if (prayTime.isRelative()) {
            ComplexZmanimCalendar calendar = provider.getCzc(toGeo(location));
            RelativeTime time = prayTime.getRelativeTime();
            RelativeTimeType type = time.getRelativeTimeType();
            Date date = getDateRelatively(calendar, type);
            int offset = time.getOffset();
            int hour = getHour(date, offset);
            int minutes = getMinutes(date, offset);
            return new ExactTime(hour, minutes);
        } else {
            return prayTime.getExactTime();
        }
    }

    private static int getHour(Date date, int offset) {
        int minWithoutOffset = date.getHours() * 60 + date.getMinutes();
        int totalMin = minWithoutOffset + offset;
        return totalMin / 60;
    }

    public static int getMinutes(Date date, int offset) {
        int totalMin = date.getMinutes() + offset;
        int min = totalMin % 60;
        int positiveMin = (min + 60) % 60;
        return positiveMin;
    }

    private static Date getDateRelatively(ComplexZmanimCalendar calendar, RelativeTimeType relativeTimeType) {
        switch (relativeTimeType) {
            case DAWN:
                return calendar.getAlos19Point8Degrees();
            case SUNRISE:
                return calendar.getSunrise();
            case SUNSET:
                return calendar.getSunset();
            case STARS_OUT:
                return calendar.getTzaisGeonim5Point95Degrees();
            default:
                return new Date();
        }
    }

    @NonNull
    private static GeoLocation toGeo(Location location) {
        GeoLocation geoLocation = new GeoLocation("",
                location.getLatitude(),
                location.getLongitude(),
                TimeZone.getTimeZone("GMT+2:00"));
        if (location.hasAltitude()) {
            geoLocation.setElevation(location.getAltitude());
        }
        return geoLocation;
    }

}