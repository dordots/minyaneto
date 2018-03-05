package com.app.minyaneto_android.models.domain;

import com.app.minyaneto_android.models.minyan.PrayType;
import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

public class ConcreteMinyanDomain {

  private PrayType prayType;
  private String nosach;
  private Date time;
  private LatLng location;
  private String synagogueId;

  public ConcreteMinyanDomain(PrayType prayType,
      String nosach,
      Date time,
      LatLng location,
      String synagogueId) {

    this.prayType = prayType;
    this.nosach = nosach;
    this.time = time;
    this.location = location;
    this.synagogueId = synagogueId;
  }
}
