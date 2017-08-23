package com.app.minyaneto_android;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.TimeZone;


/**
 * add matay??
 */

public class DailyTimes {
    private LatLng place;
    private Date day;
    TimeZone timeZone;
    DailyTimes(LatLng p, Date d)
    {
        place=p;
        day=d;
        if (null == d)
        {
            day = new Date(); //initialized to now
        }
        timeZone = TimeZone.getDefault();

    }

    Date GetSunrise(int minute_offset)
    {

    }

    Date GetSunset(int minnute_offset)
    {

    }

}
/*
import net.sourceforge.zmanim.*;
        import net.sourceforge.zmanim.util.*;
        import java.util.TimeZone;
public class SimpleZmanim{
    public static void main(String [] args) {
        String locationName = "Lakewood, NJ";
        double latitude = 40.096; //latitude of Lakewood, NJ
        double longitude = -74.222; //longitude of Lakewood, NJ
        double elevation = 0; //optional elevation
        //use a Valid Olson Database timezone listed in java.util.TimeZone.getAvailableIDs()
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        //create the location object
        GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
        //create the ZmanimCalendar
        ZmanimCalendar zc = new ZmanimCalendar(location);
        //optionally set the internal calendar
        //zc.getCalendar().set(1969, Calendar.FEBRUARY, 8);
        System.out.println("Today's Zmanim for " + locationName);
        System.out.println("Sunrise: " + zc.getSunrise()); //output sunrise
        System.out.println("Sof Zman Shema GRA: " + zc.getSofZmanShmaGRA()); //output Sof Zman Shema GRA
        System.out.println("Sunset: " + zc.getSunset()); //output sunset
    }
}*/
