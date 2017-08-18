package com.app.minyaneto_android.entities;

/**
 * Created by User on 15/08/2017.
 */

public class Minyan {
    private String id;
    private String name;
    private String day;
    private String time;
    private boolean repeat;

    public Minyan(String name, String day, String time, boolean repeat) {
        this.name = name;
        this.day = day;
        this.time = time;
        this.repeat = repeat;
    }

    public Minyan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
