package com.app.minyaneto_android.directions;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by aviv on 14/02/2018.
 */

public interface RouteListener {

    void onRoutePolylineReceived(PolylineOptions polyline);
}
