package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinyanScheduleData {

    @SerializedName("day")
    @Expose
    private String weekDay;

    @SerializedName("name")
    @Expose
    private String prayType;

    @SerializedName("time")
    @Expose
    private String stringTime;

    public String getWeekDay() {
        return weekDay;
    }

    public String getPrayType() {
        return prayType;
    }

    public String getStringTime() {
        return stringTime;
    }

    @Override
    public String toString() {
        return "MinyanScheduleData{" +
                "weekDay='" + weekDay + '\'' +
                ", prayType='" + prayType + '\'' +
                ", stringTime='" + stringTime + '\'' +
                '}';
    }
}