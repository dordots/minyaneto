package com.app.minyaneto_android.location;

import com.app.minyaneto_android.models.domain.Synagogue;
import com.google.android.gms.maps.model.LatLng;

public class LocationUtility {

  public static long calculateDistance(LatLng location1, LatLng location2) {
    if(null==location1 || null==location2){
      return -1;
    }
    double dLat = Math.toRadians(location1.latitude - location2.latitude);
    double dLon = Math.toRadians(location1.longitude - location2.longitude);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(location2.latitude))
        * Math.cos(Math.toRadians(location1.latitude)) * Math.sin(dLon / 2)
        * Math.sin(dLon / 2);
    double c = 2 * Math.asin(Math.sqrt(a));
    long distanceInMeters = Math.round(6371000 * c);
    return distanceInMeters;
  }
}
