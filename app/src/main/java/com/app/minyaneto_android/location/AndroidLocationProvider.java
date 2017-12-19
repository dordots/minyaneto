package com.app.minyaneto_android.location;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.TimeZone;

public class AndroidLocationProvider implements LocationProvider {

    private final MutableLiveData<Location> liveLocation;

    public AndroidLocationProvider(Context context) {
        liveLocation = new MutableLiveData<>();
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (lm != null) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        liveLocation.postValue(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
            }
        }
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public LiveData<Location> getLocation() {
        return liveLocation;
    }
}
