package com.app.minyaneto_android.models.domain;

import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.PrayTime;

import java.time.DayOfWeek;

public class MinyanScheduleDomain {
    private DayOfWeek dayOfWeek;
    private PrayType prayType;
    private PrayTime prayTime;

    public MinyanScheduleDomain(DayOfWeek dayOfWeek, PrayType prayType, PrayTime prayTime) {
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
