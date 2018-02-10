package com.app.minyaneto_android.models.time;

import java.util.Locale;

public class ExactTime {
    private int hour;
    private int minutes;

    public ExactTime(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minutes);
        return "ב- " + time;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHour() {
        return hour;
    }
}
