package com.app.minyaneto_android.location;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

public class LocationRepository {

  private static LocationRepository instance = new LocationRepository();
  private Location lastKnownLocation;

  private LocationRepository() {
  }

  public static LocationRepository getInstance() {
    return instance;
  }

  public Location getLastKnownLocation() {
    return lastKnownLocation;
  }

  public void setLastKnownLocation(Location lastKnownLocation) {
    this.lastKnownLocation = lastKnownLocation;
  }

  public LatLng getLastKnownLatLng() {
    Location location = getLastKnownLocation();
    if (null == location) {
      return null;
    }
    return new LatLng(location.getLatitude(), location.getLongitude());
  }
}
