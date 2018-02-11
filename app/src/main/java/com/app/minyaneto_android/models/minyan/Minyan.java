package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Minyan {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("time")
    @Expose
    private String stringTime;

    private PrayType prayType;
    private PrayTime prayTime;
    private PrayDayType prayDayType;
    private Date lastUpdate;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
        setPrayDayType(day);
    }

    private void setPrayDayType(String day) {
        try {
            prayDayType = PrayDayType.getType(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setPrayType(name);
    }

    private void setPrayType(String name) {
        try {
            prayType = PrayType.getType(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrayType getPrayType() {
        return prayType;
    }

    public void setPrayType(PrayType prayType) {
        this.prayType = prayType;
    }

    public void setTime(String stringTime) {
        this.stringTime = stringTime;
        String[] parts;
        if (stringTime.contains(":")) {
            parts = stringTime.split(":");
            this.prayTime = new PrayTime(new ExactTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        } else if (stringTime.contains("#")) {
            parts = stringTime.split("#");
            this.prayTime = new PrayTime(new RelativeTime(RelativeTimeType.valueOf(parts[0]), Integer.parseInt(parts[1])));
        }
    }

    public PrayDayType getPrayDayType() {
        return prayDayType;
    }

    public void setPrayDayType(PrayDayType prayDayType) {
        this.prayDayType = prayDayType;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        String nounPray = "תפילת";
        String nounInDays = "ביום";
        return String.format("%s %s %s %s %s", nounPray, prayType.name(),
                nounInDays, prayDayType.name(), stringTime);
    }

    public void refreshData() {
        setTime(stringTime);
        setPrayDayType(day);
        setPrayType(name);
    }

    public void setRelativeTime(RelativeTime time) {
        this.prayTime = new PrayTime(time);
        this.stringTime = time.getRelativeTimeType().name() + "#" + time.getOffset();
    }

    public void setExactTime(ExactTime time) {
        this.prayTime = new PrayTime(time);
        this.stringTime = time.getHour() + ":" + time.getMinutes();
    }

    public PrayTime getPrayTime() {
        return prayTime;
    }
}
