package com.app.minyaneto_android.models.minyan;

import java.util.Calendar;
import java.util.Date;

public class ExactTime implements Time {

    private int hour;
    private int minute;

    public ExactTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    private void setHour(int hour) {
        if (hour > 23 || hour < 0)
            return;
        this.hour = hour;
    }


    private void setMinute(int minute) {
        if (minute > 60 || minute < 0)
            return;
        this.minute = minute;
    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public int getMinutes() {
        return minute;
    }

    @Override
    public Date toDate(WeekDay weekDay) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, weekDay.ordinal());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTime();
    }
}
