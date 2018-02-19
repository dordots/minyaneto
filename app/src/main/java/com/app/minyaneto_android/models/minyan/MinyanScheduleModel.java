package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.models.time.PrayTime;

import java.time.DayOfWeek;

public class MinyanScheduleModel {
    private DayOfWeek dayOfWeek;
    private PrayType prayType;
    private PrayTime prayTime;

    public MinyanScheduleModel(DayOfWeek dayOfWeek, PrayType prayType, PrayTime prayTime) {
        this.dayOfWeek = dayOfWeek;
        this.prayType = prayType;
        this.prayTime = prayTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public PrayType getPrayType() {
        return prayType;
    }

    public PrayTime getPrayTime() {
        return prayTime;
    }
}
