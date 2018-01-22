package zmanim;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.app.minyaneto_android.models.minyan.RelativeTime;
import com.app.minyaneto_android.models.minyan.RelativeTimeType;
import com.app.minyaneto_android.models.minyan.Time;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class RelativeTimeTest {

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
        Time time = createRelativeTime(RelativeTimeType.DAWN, -10);

        int hour = time.getHour();
        int minutes = time.getMinutes();

        // DAWN is 04:52
        assertEquals(4, hour);
        assertEquals(42, minutes);
    }

    @Test
    public void sunrise() throws Exception {
        Time time = createRelativeTime(RelativeTimeType.SUNRISE, 13);

        int hour = time.getHour();
        int minutes = time.getMinutes();

        // SUNRISE is 06:24
        assertEquals(6, hour);
        assertEquals(37, minutes);
    }

    @Test
    public void sunset() throws Exception {
        Time time = createRelativeTime(RelativeTimeType.SUNSET, 75);

        int hour = time.getHour();
        int minutes = time.getMinutes();

        // SUNSET is 16:40
        assertEquals(17, hour);
        assertEquals(55, minutes);
    }

    @Test
    public void starsOut() throws Exception {
        Time time = createRelativeTime(RelativeTimeType.STARS_OUT, -150);

        int hour = time.getHour();
        int minutes = time.getMinutes();

        // STARS_OUT is 17:02
        assertEquals(14, hour);
        assertEquals(32, minutes);
    }

    @NonNull
    private RelativeTime createRelativeTime(RelativeTimeType relativeTimeType, int offset) {
        return new RelativeTime(relativeTimeType, offset, calendarProvider, location);
    }
}