package com.app.minyaneto_android.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

  public static String getAddressLineFromLatLng(Context context, LatLng latLng) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());

    try {
      List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
      if (addresses == null || addresses.isEmpty()) {
        return "";
      }

      Address address = addresses.get(0);
      return address.getAddressLine(0);
    } catch (Exception e) {
      return "";
    }
  }
}
