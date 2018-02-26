package zmanim;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.location.Location;
import android.location.LocationManager;

import com.app.minyaneto_android.zmanim.ZmanimCalendarProvider;
import com.app.minyaneto_android.zmanim.ZmanimContract;
import com.app.minyaneto_android.zmanim.ZmanimPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.text.SimpleDateFormat;
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
    private SimpleDateFormat formatter;
    private ArgumentCaptor<Date> captor;
    private ZmanimContract.View zmanimView;

    @Before
    public void setUp() throws Exception {
        calendarProvider = new MockZmanimCalendarProvider();
        formatter = new SimpleDateFormat("HH:mm");
        captor = ArgumentCaptor.forClass(Date.class);
    }

    @Test
    public void onNullLocationDisplayNoLocation() throws Exception {
        // Arrange
        zmanimView = spy(ZmanimContract.View.class);
        ZmanimPresenter presenter =
                new ZmanimPresenter(calendarProvider, null, TimeZone.getTimeZone("GMT+2:00"), zmanimView);

        // Act
        presenter.showZmanim();

        // Assert
        verify(zmanimView).displayNoLocationFound();
    }

    @Test
    public void getLocationAndShowRelatedZmanim() throws Exception {
        // Arrange
        zmanimView = spy(ZmanimContract.View.class);
        ZmanimPresenter presenter =
                new ZmanimPresenter(calendarProvider, getMockLocation(), TimeZone.getTimeZone("GMT+2:00"), zmanimView);

        // Act
        presenter.showZmanim();

        // Assert
        verifyAlos("04:52"); // actually expected 04:53
        verifyMisheyakir("05:36");
        verifyHenez("06:24");
        verifyShkiaa("16:40");
        verifyTzais("17:02");
    }

    private Location getMockLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(31.783);
        location.setLongitude(35.219);
        location.setAltitude(715);
        return location;
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

}