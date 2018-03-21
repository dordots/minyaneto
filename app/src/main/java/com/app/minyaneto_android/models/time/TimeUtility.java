package com.app.minyaneto_android.models.time;

import android.location.Location;
import android.support.annotation.NonNull;
import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.models.domain.MinyanSchedule;
import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

public class TimeUtility {

  public static ExactTime extractSpecificTime(PrayTime prayTime, Location location) {
    return extractSpecificTime(prayTime, new ZmanimCalendarProvider(), location);
  }

  public static ExactTime extractSpecificTime(PrayTime prayTime, ZmanimCalendarProvider provider,
      Location location) {
    if (prayTime.isRelative()) {
      ComplexZmanimCalendar calendar = provider.getCzc(toGeo(location));
      RelativeTime time = prayTime.getRelativeTime();
      RelativeTimeType type = time.getRelativeTimeType();
      Date date = getDateRelatively(calendar, type);
      int offset = time.getOffset();
      int hour = getHour(date, offset);
      int minutes = getMinutes(date, offset);
      return new ExactTime(hour, minutes);
    } else {
      return prayTime.getExactTime();
    }
  }

  private static int getHour(Date date, int offset) {
    int minWithoutOffset = date.getHours() * 60 + date.getMinutes();
    int totalMin = minWithoutOffset + offset;
    return totalMin / 60;
  }

  public static int getMinutes(Date date, int offset) {
    int totalMin = date.getMinutes() + offset;
    int min = totalMin % 60;
    int positiveMin = (min + 60) % 60;
    return positiveMin;
  }

  private static Date getDateRelatively(ComplexZmanimCalendar calendar,
      RelativeTimeType relativeTimeType) {
    switch (relativeTimeType) {
      case DAWN:
        return calendar.getAlos19Point8Degrees();
      case SUNRISE:
        return calendar.getSunrise();
      case SUNSET:
        return calendar.getSunset();
      case STARS_OUT:
        return calendar.getTzaisGeonim5Point95Degrees();
      default:
        return new Date();
    }
  }

  @NonNull
  private static GeoLocation toGeo(Location location) {
    GeoLocation geoLocation = new GeoLocation("",
        location.getLatitude(),
        location.getLongitude(),
        TimeZone.getTimeZone("GMT+2:00"));
    if (location.hasAltitude()) {
      geoLocation.setElevation(location.getAltitude());
    }
    return geoLocation;
  }

  public static String getTimes(List<MinyanSchedule> minyans, Date date) {
    if (null == date) {
      date = new Date();
    }
    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
    StringBuilder result = new StringBuilder();
    ArrayList<String> myResult = new ArrayList<>();
    for (MinyanSchedule minyan : minyans) {
      //TODO calculate real time -like rosh hodesh..
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      if (date.getDay() != minyan.getWeekDay().ordinal()) {
        continue;
      }
      cal.set(Calendar.DAY_OF_WEEK, minyan.getWeekDay().ordinal() + 1);
      ExactTime exactTime = extractSpecificTime(minyan.getPrayTime(),
          LocationRepository.getInstance().getLastKnownLocation());
      cal.set(Calendar.HOUR_OF_DAY, exactTime.getHour());
      cal.set(Calendar.MINUTE, exactTime.getMinutes());
      Date f = cal.getTime();
      if (minyan.getWeekDay().ordinal() == date.getDay() && f.after(date)) {
        result.append(" ,").append(format.format(f));
        myResult.add(format.format(f));
      }
    }
    final Date finalDate = date;
    Collections.sort(myResult, new Comparator<String>() {
      public int compare(String o1, String o2) {
        Date date1 = finalDate;
        date1.setHours(Integer.parseInt(o1.split(":")[0]));
        date1.setMinutes(Integer.parseInt(o1.split(":")[1]));

        Date date2 = finalDate;
        date2.setHours(Integer.parseInt(o2.split(":")[0]));
        date2.setMinutes(Integer.parseInt(o2.split(":")[1]));

        if (date1.equals(date2)) {
          return 0;
        }
        return date1.before(date2) ? -1 : 1;
      }
    });
    return myResult.toString().substring(1, myResult.toString().length() - 1);//result;
  }
}