package com.app.minyaneto_android.zmanim;

import static org.junit.Assert.assertEquals;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.app.minyaneto_android.models.time.TimeUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TimeUtilityTest {

  private Location location;
  private MockZmanimCalendarProvider calendarProvider;

  @Before
  public void setUp() throws Exception {
    location = new Location(LocationManager.GPS_PROVIDER);
    location.setLatitude(31.783);
    location.setLongitude(35.219);
    location.setAltitude(715);
    calendarProvider = new MockZmanimCalendarProvider();
  }

  @Test
  public void dawn() throws Exception {
    ExactTime time = extractSpecificTime(RelativeTimeType.DAWN, -10);

    int hour = time.getHour();
    int minutes = time.getMinutes();

    // DAWN is 04:52
    assertEquals(4, hour);
    assertEquals(42, minutes);
  }

  @Test
  public void sunrise() throws Exception {
    ExactTime time = extractSpecificTime(RelativeTimeType.SUNRISE, 13);

    int hour = time.getHour();
    int minutes = time.getMinutes();

    // SUNRISE is 06:24
    assertEquals(6, hour);
    assertEquals(37, minutes);
  }

  @Test
  public void sunset() throws Exception {
    ExactTime time = extractSpecificTime(RelativeTimeType.SUNSET, 75);

    int hour = time.getHour();
    int minutes = time.getMinutes();

    // SUNSET is 16:40
    assertEquals(17, hour);
    assertEquals(55, minutes);
  }

  @Test
  public void starsOut() throws Exception {
    ExactTime time = extractSpecificTime(RelativeTimeType.STARS_OUT, -150);

    int hour = time.getHour();
    int minutes = time.getMinutes();

    // STARS_OUT is 17:02
    assertEquals(14, hour);
    assertEquals(32, minutes);
  }

  @NonNull
  private ExactTime extractSpecificTime(RelativeTimeType relativeTimeType, int offset) {
    PrayTime prayTime = new PrayTime(new RelativeTime(relativeTimeType, offset));
    return TimeUtility.extractSpecificTime(prayTime, calendarProvider, location);
  }
}