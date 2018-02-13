package com.app.minyaneto_android.models.time;

import android.location.Location;

import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.models.minyan.Minyan;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    public static Date getDate(Minyan minyan) {
        Location location = LocationRepository.getInstance().getLastKnownLocation();
        ExactTime time = TimeUtility.extractSpecificTime(minyan.getPrayTime(), location);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, minyan.getPrayDayType().ordinal() + 1);
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        return calendar.getTime();
    }
}
