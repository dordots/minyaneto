package com.app.minyaneto_android.models;

import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.minyan.Time;

/**
 * Created by User on 15/08/2017.
 */

public class Minyan {
    private String id;
    private PrayType prayType;
    private Time time;
    private PrayDayType prayDayType;

    public Minyan() {}

    public Minyan(String id, PrayType prayType, Time time, PrayDayType prayDayType) {
        this.id = id;
        this.prayType = prayType;
        this.time = time;
        this.prayDayType = prayDayType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PrayType getPrayType() {
        return prayType;
    }

    public void setPrayType(PrayType prayType) {
        this.prayType = prayType;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public PrayDayType getPrayDayType() {
        return prayDayType;
    }

    public void setPrayDayType(PrayDayType prayDayType) {
        this.prayDayType = prayDayType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Minyan)) return false;

        Minyan minyan = (Minyan) o;

        if (id != null ? !id.equals(minyan.id) : minyan.id != null) return false;
        if (prayType != minyan.prayType) return false;
        if (!time.equals(minyan.time)) return false;
        return prayDayType == minyan.prayDayType;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + prayType.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + prayDayType.hashCode();
        return result;
    }
}
