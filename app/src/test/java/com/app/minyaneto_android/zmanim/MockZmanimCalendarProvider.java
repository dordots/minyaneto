package com.app.minyaneto_android.zmanim;

import java.util.Calendar;
import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

public class MockZmanimCalendarProvider extends ZmanimCalendarProvider {


  //check check check

  @Override
  public ComplexZmanimCalendar getCzc(GeoLocation location) {
    ComplexZmanimCalendar calendar = new ComplexZmanimCalendar(location);
    calendar.getCalendar().set(2017, Calendar.DECEMBER, 11);
    return calendar;
  }
}
