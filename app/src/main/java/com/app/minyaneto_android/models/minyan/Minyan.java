
package com.app.minyaneto_android.models.minyan;

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
    private String time;

    private PrayType prayType;
    private Time the_time;
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
        }
        catch (Exception ex){
            //TODO something
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

    /*public String getTime() {
        return time;
    }*/

    public void setTime(String time) {
        this.time = time;
        String[] parts = time.split(":");
        if (parts.length>=2){
            ExactTime exactTime = new ExactTime(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
            this.the_time=exactTime;
        }
    }

    public PrayType getPrayType() {
        return prayType;
    }

    public void setPrayType(PrayType prayType) {
        this.prayType = prayType;
    }

    public Time getTime() {
        return the_time;
    }

    public void setTime(Time time) {
        this.the_time = time;
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
        if (!isValid())
            return "Minyan is not valid!";

        // TODO: CR david

        String nounPray = "תפילת";

        String nounInDays = "ביום";


        return String.format("%s %s %s %s %s", nounPray, prayType.name(),
                nounInDays, prayDayType.name(), time.toString());
    }

    public boolean isValid() {
        return true;
    }

    public void refreshData() {
        String[] parts = time.split(":");
        if (parts.length>=2){
            ExactTime exactTime = new ExactTime(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
            this.the_time=exactTime;
        }

        setPrayDayType(day);
        setPrayType(name);
    }
}
