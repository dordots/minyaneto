package com.app.minyaneto_android.zmanim;

import com.app.minyaneto_android.Injection;
import com.app.minyaneto_android.location.LocationProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ZmanimPresenterTest {

    private ZmanimCalendarProvider calendarProvider;
    private LocationProvider locationProvider;
    private SimpleDateFormat formatter;
    private ArgumentCaptor<Date> captor;
    private ZmanimContract.View zmanimView;

    @Before
    public void setUp() throws Exception {
        calendarProvider = Injection.getZmanimCalendarProvider();
        locationProvider = Injection.getLocationProvider(null);
        formatter = new SimpleDateFormat("HH:mm");
        captor = ArgumentCaptor.forClass(Date.class);
    }

    @Test
    public void getLocationAndShowRelatedZmanim() throws Exception {
        // Arrange
        zmanimView = spy(ZmanimContract.View.class);
        ZmanimPresenter presenter = new ZmanimPresenter(calendarProvider, locationProvider, zmanimView);

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
}