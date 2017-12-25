package zmanim;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.app.minyaneto_android.location.LocationProvider;
import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;
import com.app.minyaneto_android.zmanim.ZmanimContract;
import com.app.minyaneto_android.zmanim.ZmanimPresenter;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ZmanimPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

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
        verifyAlos("04:52"); // actually expected 04:53
        verifyMisheyakir("05:36");
        verifyHenez("06:24");
        verifyShkiaa("16:40");
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

    private void verifyHenez(String expected) {
        verify(zmanimView).displayHenezHahama(captor.capture());
        assertEquals(expected, formatter.format(captor.getValue()));
    }

    private void verifyShkiaa(String expected) {
        verify(zmanimView).displayShkiaa(captor.capture());
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
        public LiveData<Location> getLocation() {
            MutableLiveData<Location> data = new MutableLiveData<>();
            data.postValue(getMockLocation());
            return data;
        }

        @NonNull
        private Location getMockLocation() {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(31.783);
            location.setLongitude(35.219);
            location.setAltitude(715);
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