package com.app.minyaneto_android.zmanim;

import android.arch.lifecycle.Observer;
import android.location.Location;
import android.support.annotation.Nullable;

import com.app.minyaneto_android.location.LocationProvider;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

public class ZmanimPresenter implements ZmanimContract.UserActionsListener {

    private final ZmanimCalendarProvider zmanimCalendarProvider;
    private final LocationProvider locationProvider;
    private final ZmanimContract.View zmanimView;

    public ZmanimPresenter(ZmanimCalendarProvider zmanimCalendarProvider,
                           LocationProvider locationProvider,
                           ZmanimContract.View zmanimView) {
        this.zmanimCalendarProvider = zmanimCalendarProvider;
        this.locationProvider = locationProvider;
        this.zmanimView = zmanimView;
    }

    @Override
    public void showZmanim() {
        locationProvider.getLocation().observeForever(new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                if (location != null) {
                    ComplexZmanimCalendar czc = getCzc(location);
                    displayZmanim(czc);
                }
            }
        });
    }

    private ComplexZmanimCalendar getCzc(Location location) {
        GeoLocation geoLocation = new GeoLocation("",
                location.getLatitude(),
                location.getLongitude(),
                locationProvider.getTimeZone());
        return zmanimCalendarProvider.getCzc(geoLocation);
    }

    private void displayZmanim(ComplexZmanimCalendar czc) {
        zmanimView.displayAlosHashahar(czc.getAlos19Point8Degrees());
        zmanimView.displayMisheyakir(czc.getMisheyakir11Degrees());
        zmanimView.displayHenezHahama(czc.getSunrise());
        zmanimView.displayShkiaa(czc.getSunset());
        zmanimView.displayTzaisHakochavim(czc.getTzaisGeonim5Point95Degrees());
    }
}
