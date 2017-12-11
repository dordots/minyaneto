package zmanim;

import android.location.Location;

import com.app.minyaneto_android.location.LocationProvider;
import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;
import com.app.minyaneto_android.zmanim.ZmanimContract;
import com.app.minyaneto_android.zmanim.ZmanimPresenter;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ZmanimPresenterTest {

    private ZmanimCalendarProvider calendarProvider;
    private LocationProvider locationProvider;
    private SimpleDateFormat formatter;
    private ArgumentCaptor<Date> captor;
    private ZmanimContract.View zmanimView;

    @Before
    public void setUp() throws Exception {
        calendarProvider = new MockZmanimCalendarProvider();
        locationProvider = new MockLocationProvider();
        formatter = new SimpleDateFormat("HH:mm");
        captor = ArgumentCaptor.forClass(Date.class);
    }

    @Test
    public void getLocationAndShowRelatedZmanim() throws Exception {
        // Arrange
        zmanimView = spy(ZmanimContract.View.class);
        ZmanimPresenter presenter =
                new ZmanimPresenter(calendarProvider, locationProvider, zmanimView);

        // Act
        presenter.showZmanim();

        // Assert
        verifyAlos("04:52");
        verifyMisheyakir("05:36");
        verifyTzais("17:02");
    }

    private void verifyAlos(String expected) {
        verify(zmanimView).displayAlosHashahar(captor.capture());
        assertEquals(expected, formatter.format(captor.getValue()));
    }

    private void verifyMisheyakir(String expected) {
        verify(zmanimView).displayMisheyakir(captor.capture());
        assertEquals(expected, formatter.format(captor.getValue()));
    }

    private void verifyTzais(String expected) {
        verify(zmanimView).displayTzaisHakochavim(captor.capture());
        assertEquals(expected, formatter.format(captor.getValue()));
    }

    public static class MockLocationProvider implements LocationProvider {
        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getTimeZone("GMT+2:00");
        }

        @Override
        public Location getLocation() {
            Location location = mock(Location.class);
            when(location.getLatitude()).thenReturn(31.768);
            when(location.getLongitude()).thenReturn(35.214);
            return location;
        }
    }

    public static class MockZmanimCalendarProvider extends ZmanimCalendarProvider {
        @Override
        public ComplexZmanimCalendar getCzc(GeoLocation location) {
            ComplexZmanimCalendar calendar = new ComplexZmanimCalendar(location);
            calendar.getCalendar().set(2017, Calendar.DECEMBER, 11);
            return calendar;
        }
    }
}