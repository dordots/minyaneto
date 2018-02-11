package com.app.minyaneto_android.models.time;

import android.content.Context;
import android.location.Location;

import com.app.minyaneto_android.location.AndroidLocationProvider;
import com.app.minyaneto_android.models.minyan.Minyan;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    public static Date getDate(Minyan minyan, Context context) {
        Location location = new AndroidLocationProvider(context).getLocation().getValue();
        ExactTime time = TimeUtility.extractSpecificTime(minyan.getPrayTime(), location);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, minyan.getPrayDayType().ordinal() + 1);
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        return calendar.getTime();
    }
}
