package com.app.minyaneto_android.models.domain;

import com.app.minyaneto_android.data.WeekDay;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.PrayTime;


public class MinyanSchedule {

  private WeekDay weekDay;
  private PrayType prayType;
  private PrayTime prayTime;

  public MinyanSchedule(WeekDay weekDay, PrayType prayType, PrayTime prayTime) {
    this.weekDay = weekDay;
    this.prayType = prayType;
    this.prayTime = prayTime;
  }

  public WeekDay getWeekDay() {
    return weekDay;
  }

  public PrayType getPrayType() {
    return prayType;
  }

  public PrayTime getPrayTime() {
    return prayTime;
  }
}
