package com.app.minyaneto_android.zmanim;

import android.location.Location;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.TimeZone;

public class ZmanimPresenter implements ZmanimContract.UserActionsListener {

    private final ZmanimCalendarProvider zmanimCalendarProvider;
    private final Location location;
    private final ZmanimContract.View zmanimView;
    private final TimeZone timeZone;

    public ZmanimPresenter(ZmanimCalendarProvider zmanimCalendarProvider,
                           Location location,
                           TimeZone timeZone, ZmanimContract.View zmanimView) {
        this.zmanimCalendarProvider = zmanimCalendarProvider;
        this.location = location;
        this.timeZone = timeZone;
        this.zmanimView = zmanimView;
    }

    @Override
    public void showZmanim() {
        if (location != null) {
            ComplexZmanimCalendar czc = getCzc(location, timeZone);
            displayZmanim(czc);
        } else {
            zmanimView.displayNoLocationFound();
        }
    }

    private ComplexZmanimCalendar getCzc(Location location, TimeZone timeZone) {
        GeoLocation geoLocation = new GeoLocation("",
                location.getLatitude(),
                location.getLongitude(),
                timeZone);
        if (location.hasAltitude()) {
            geoLocation.setElevation(location.getAltitude());
        }
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
