package com.app.minyaneto_android.models.minyan;

import android.content.res.Resources;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;

import java.util.Date;

/**
 * Created by משה on 25/08/2017.
 */

public class RelativeTime implements Time {

    private RelativeTimeType relativeTimeType;
    private int offset;

    public RelativeTime(RelativeTimeType relativeTimeType, int offset) {
        this.relativeTimeType = relativeTimeType;
        this.offset = offset;
    }

    @Override
    public int getHour() {
        /*
        String locationName = "Lakewood, NJ";
        double latitude = 40.096; //latitude of Lakewood, NJ
        double longitude = -74.222; //longitude of Lakewood, NJ
        double elevation = 0; //optional elevation
        //use a Valid Olson Database timezone listed in java.util.TimeZone.getAvailableIDs()
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        //create the location object
        GeoLocation location1 = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
        //create the ZmanimCalendar
        ZmanimCalendar zc = new ZmanimCalendar(location1);
        //optionally set the internal calendar
        //zc.getCalendar().set(1969, Calendar.FEBRUARY, 8);
        String d = ("Today's Zmanim for " + locationName);
        d += "Sunrise: " + zc.getSunrise(); //output sunrise
        d += "Sof Zman Shema GRA: " + zc.getSofZmanShmaGRA(); //output Sof Zman Shema GRA
        d += "Sunset: " + zc.getSunset(); //output sunset
        */
        // Implementation depends on Kosher Java
        return 0;
    }

    @Override
    public int getMinutes() {
        // Implementation depends on Kosher Java
        return 0;
    }

    @Override
    public Date toDate(WeekDay weekDay) {
        // Implementation depends on Kosher Java

        //TODO create a ExactTime and return toDate
        ExactTime myTime=new ExactTime(getHour(),getMinutes());

        return myTime.toDate(weekDay);
    }

    @Override
    public String toString() {
        Resources res = MainActivity.resources;
        String nounMinutes =  res.getString(R.string.nouns_minutes);
        String nounAfterOrBefore =  offset > 0 ? res.getString(R.string.nouns_after): res.getString(R.string.nouns_before);
        String nounThe =  offset < 0 ? res.getString(R.string.nouns_the) : "";
        String nounAt =  res.getString(R.string.nouns_at);
        if (offset == 0) {
            return String.format("%s%s", nounAt, relativeTimeType.toString());
        }
        return String.format("%d %s %s %s %s", offset, nounMinutes, nounAfterOrBefore, nounThe,
                relativeTimeType.toString());
    }
}
