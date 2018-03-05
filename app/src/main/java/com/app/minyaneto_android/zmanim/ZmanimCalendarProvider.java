package com.app.minyaneto_android.zmanim;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

public class ZmanimCalendarProvider {

  public ComplexZmanimCalendar getCzc(GeoLocation location) {
    return new ComplexZmanimCalendar(location);
  }
}
