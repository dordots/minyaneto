package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;

import java.text.SimpleDateFormat;
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
        return toCalendar(weekDay).getTime();
    }

    private Calendar toCalendar(WeekDay weekDay) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, weekDay.ordinal());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal;
    }

    @Override
    public String toString() {
        String nounAt =  MainActivity.resources.getString(R.string.nouns_at);
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm");
        String time = format.format(toDate(WeekDay.values()[new Date().getDay()]));
        return nounAt + " " + time;
    }
}
