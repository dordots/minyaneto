package zmanim;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.app.minyaneto_android.location.LocationProvider;

import java.util.TimeZone;

public class MockLocationProvider implements LocationProvider {
    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone("GMT+2:00");
    }

    @Override
    public LiveData<Location> getLocation() {
        MutableLiveData<Location> data = new MutableLiveData<>();
        data.postValue(getMockLocation());
        return data;
    }

    @NonNull
    private Location getMockLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(31.783);
        location.setLongitude(35.219);
        location.setAltitude(715);
        return location;
    }
}
