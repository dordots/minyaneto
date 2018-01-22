package com.app.minyaneto_android.models.minyan;

import android.location.Location;
import android.support.annotation.NonNull;

import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Date;
import java.util.TimeZone;


public class RelativeTime implements Time {

    private final RelativeTimeType relativeTimeType;
    private final int offset;
    private final ComplexZmanimCalendar calendar;

    public RelativeTime(RelativeTimeType relativeTimeType, int offset, ZmanimCalendarProvider provider, Location location) {
        this.relativeTimeType = relativeTimeType;
        this.offset = offset;
        if (location == null) {
            calendar = null;
        } else {
            calendar = provider.getCzc(toGeo(location));
        }
    }

    public RelativeTime(RelativeTimeType relativeTimeType, int offset, Location location) {
        this(relativeTimeType, offset, new ZmanimCalendarProvider(), location);
    }

    public RelativeTime(RelativeTimeType relativeTimeType, int offset) {
        this(relativeTimeType, offset, null);
    }

    @Override
    public int getHour() {
        if (calendar == null) return 0;
        int minWithoutOffset = getDateRelatively().getHours() * 60 + getDateRelatively().getMinutes();
        int totalMin = minWithoutOffset + offset;
        return totalMin / 60;
    }

    @Override
    public int getMinutes() {
        if (calendar == null) return 0;
        int totalMin = getDateRelatively().getMinutes() + offset;
        int min = totalMin % 60;
        int positiveMin = (min + 60) % 60;
        return positiveMin;
    }

    @Override
    public Date toDate(WeekDay weekDay) {
        return new ExactTime(getHour(), getMinutes()).toDate(weekDay);
    }

    @Override
    public String toString() {
        String nounMinutes = "דקות";
        String nounAfterOrBefore = offset > 0 ? "אחרי" : "לפני";
        String nounThe = offset < 0 ? "ה-" : "";
        String nounAt = "ב-";
        if (offset == 0) {
            return String.format("%s%s", nounAt, relativeTimeType.toString());
        }
        return String.format("%d %s %s %s %s", offset, nounMinutes, nounAfterOrBefore, nounThe,
                relativeTimeType.toString());
    }

    private Date getDateRelatively() {
        switch (this.relativeTimeType) {
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
    private GeoLocation toGeo(Location location) {
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
