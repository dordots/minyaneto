package com.app.minyaneto_android.zmanim;

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
        GeoLocation location = new GeoLocation("",
                locationProvider.getLatitude(),
                locationProvider.getLongitude(),
                locationProvider.getTimeZone());
        ComplexZmanimCalendar czc = zmanimCalendarProvider.getCzc(location);
        zmanimView.displayAlosHashahar(czc.getAlos19Point8Degrees());
        zmanimView.displayMisheyakir(czc.getMisheyakir11Degrees());
        zmanimView.displayTzaisHakochavim(czc.getTzaisGeonim5Point95Degrees());
    }
}
