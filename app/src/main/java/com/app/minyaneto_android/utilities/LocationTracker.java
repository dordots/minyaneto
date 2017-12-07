package com.app.minyaneto_android.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class LocationTracker implements LocationListener {

	public static final String SAVED_LAT = "SAVED_LAT";

	public static final String SAVED_LNG = "SAVED_LNG";

	public static final String SAVED_GPS = "savedGps";

	public static final float NO_LOCATION = 0;

	private static final int NUM_TO_SAVE = 5;

	private static final String TAG = LocationTracker.class.getSimpleName();

	private static String mLogTag = "LocationTracker";

	private static Criteria mLocationCriteria = new Criteria();

	private static LocationTracker instance = null;

	private final ConfigFile mConfigFile;

	private long mLocationUpdatesMsec = 0;

	private float mLocationUpdatesMeters = 0;

	private long mNetworkLocationUpdatesMsec = TimeUnit.SECONDS.toMillis(10);

	private float mNetworkLocationUpdatesMeters = 0;

	private boolean mAlwaysUseGps = true;

	private boolean mRetrieveAddress = false;

	private int update;

	private Context mContext;

	private LocationManager mLocationManager;

	private volatile Location mLastlocation = null;

	private Location mBeforeLastLocation = null;

	private HashSet<LocationTask> tasks = new HashSet<>();

	private boolean GpsFix;

	private NetworkListener mNetworkListener;

//    private GpsFixListener listener;

	private LocationTracker(Context context, String logTag) {

		mContext = context;
		mLogTag = logTag;
		mConfigFile = new ConfigFile(context);
		initLocationManager();
		saveLocation(getLastOrCachelocation());
	}

	public static LocationTracker getInstance(Context context, String logTag) {

		if (instance == null) {
			instance = new LocationTracker(context, logTag);
		}
		return instance;
	}

	public static LocationTracker getInstance(Context context) {

		if (instance == null) {
			instance = new LocationTracker(context, TAG);
		}
		return instance;
	}

	public long getLocationUpdatesMsec() {

		return mLocationUpdatesMsec;
	}

	public void setLocationUpdatesMsec(long locationUpdatesMsec) {

		mLocationUpdatesMsec = locationUpdatesMsec;
	}


	public float getLocationUpdatesMeters() {

		return mLocationUpdatesMeters;
	}

	public void setLocationUpdatesMeters(float locationUpdatesMeters) {

		mLocationUpdatesMeters = locationUpdatesMeters;
	}


	public void setOnLocationUpdateListener(LocationTask locationTask) {

		tasks.add(locationTask);
	}

	public boolean startRequestLocation() {

		initLocationManager();
		if (mLocationManager == null) {
			return false;
		}
		removeUpdate();
		if (mAlwaysUseGps)
			requestLocation(LocationManager.GPS_PROVIDER, mLocationUpdatesMsec, mLocationUpdatesMeters);
		else
			requestByCriteria();
		Log.i(mLogTag, "startRequestLocation gps");
		return true;
	}

	private void requestByCriteria() {

		if (disableLocation()) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}

		mLocationManager.requestLocationUpdates(mLocationUpdatesMsec, mLocationUpdatesMeters, mLocationCriteria,
				this, null);
	}

	private boolean disableLocation() {

		return Build.VERSION.SDK_INT >= 23 &&
				mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
	}

	private void requestLocation(String provider, long mLocationUpdatesMsec, float mLocationUpdatesMeters) {

		if (disableLocation()) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
//        listener = new GpsFixListener(this, mLocationUpdatesMsec);
//        mLocationManager.addGpsStatusListener(listener);
		mLocationManager.requestLocationUpdates(provider, mLocationUpdatesMsec,
				mLocationUpdatesMeters, this);
	}

	private void requestLocation(String provider, long mLocationUpdatesMsec, float mLocationUpdatesMeters,LocationListener listener) {

		if (disableLocation()) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
//        listener = new GpsFixListener(this, mLocationUpdatesMsec);
//        mLocationManager.addGpsStatusListener(listener);
		mLocationManager.requestLocationUpdates(provider, mLocationUpdatesMsec,
				mLocationUpdatesMeters, listener);
	}

	public boolean start() {

		Log.i(mLogTag, "try startRequestLocation gps");
		return isRunningGpsListener() || startRequestLocation();
	}

	public boolean forceStart() {

		Log.i(mLogTag, "force startRequestLocation gps");
		return startRequestLocation();
	}

	public void stop() {

		if (isRunningGpsListener())
			stopRequestLocation();
	}

	public void setOneShotNetworkRequest(final LocationTask networkListener) {

		String networkProvider = LocationManager.NETWORK_PROVIDER;
		initLocationManager();
		if (canReqestNetwork(networkProvider)) {
			requestLocation(networkProvider, 0, 0, new NetworkListener(networkListener,true));
			Log.i(mLogTag, "setNetworkRequest");
		}
	}

	public void setNetworkRequest(LocationTask networkListener) {

		String networkProvider = LocationManager.NETWORK_PROVIDER;
		initLocationManager();
		if (canReqestNetwork(networkProvider)) {
			removeUpdate();
			mNetworkListener = new NetworkListener(networkListener,false);
			requestLocation(networkProvider, 0, 0, mNetworkListener);
			Log.i(mLogTag, "setNetworkRequest");
		}
	}

	public void removeNetworkListener(){

		if(mNetworkListener != null)
			removeUpdate(mNetworkListener);
	}

	private void initLocationManager() {

		if (mLocationManager == null)
			mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

	}

	private boolean canReqestNetwork(String networkProvider) {

		boolean needReqestNetwork = true;
		if (mLastlocation != null) {
			needReqestNetwork = !mLastlocation.getProvider().equals(networkProvider);
		}
		return needReqestNetwork && mLocationManager != null && mLocationManager.isProviderEnabled(networkProvider);
	}

	private void stopRequestLocation() {

		removeUpdate();
		mLocationManager = null;
		mLastlocation = null;// TODO check if is OK
	}

	private void removeUpdate() {

		if (disableLocation()) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
//            mLocationManager.removeGpsStatusListener();TODO uncomment
		}
	}


	private void removeUpdate(LocationListener listener) {

		if (disableLocation()) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(listener);
//            mLocationManager.removeGpsStatusListener();TODO uncomment
		}
	}


	public Location getLastlocation() {

		return mLastlocation;
	}

	public Location getLastOrCachelocation() {

		Location lastLocation = mLastlocation;
		if (lastLocation == null) {
			lastLocation = getCacheLocation(LocationManager.GPS_PROVIDER);
		}
		return lastLocation;
	}

	public boolean gpsEnable() {

		boolean enable = false;
		if (mLocationManager != null)
			enable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return enable;
	}

	public boolean networkEnable() {

		boolean enable = false;
		if (mLocationManager != null)
			enable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return enable;
	}

	public boolean isProviderEnable() {

		initLocationManager();
		Log.d("test gps", "gps = " + gpsEnable() + " network = " + networkEnable());
		return gpsEnable() || networkEnable();
	}

	public Location getCacheLocation(String provider) {

		Location myLocation = null;
		if (mLocationManager != null) {
			Location lastlocation = getLastKnownLocation(provider);
			if (lastlocation == null) {
				lastlocation = getLastKnownLocation(getProvider(provider));

				if (lastlocation == null) {
					lastlocation = getSavedLocation();
				}

			}
			// if (isUpToDate(lastlocation))
			myLocation = lastlocation;
		}
		return myLocation;
	}

	private void saveLocation(Location location) {

		if (location == null)
			return;

		if (update % NUM_TO_SAVE == 0) {
			mConfigFile.setProperty(SAVED_LAT, (float) location.getLatitude());
			mConfigFile.setProperty(SAVED_LNG, (float) location.getLongitude());
		}
		update++;
	}

	private Location getSavedLocation() {

		Location location = new Location(SAVED_GPS);
		location.setLatitude(mConfigFile.getProperty(SAVED_LAT, NO_LOCATION));
		location.setLongitude(mConfigFile.getProperty(SAVED_LNG, NO_LOCATION));

		return location.getLatitude() == NO_LOCATION ? null : location;
	}


	private String getProvider(String provider) {

		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			provider = LocationManager.GPS_PROVIDER;
		}
		return provider;
	}

	public Location getLastKnownLocation(String provider) {

		if (disableLocation()) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return null;
		}
		return mLocationManager.getLastKnownLocation(provider);
	}

	public String getProvider() {

		return mLastlocation.getProvider();
	}

	@Override
	public void onLocationChanged(Location loc) {

		mBeforeLastLocation = mLastlocation;
		mLastlocation = loc;
//        if(listener != null)listener.setLastTime(System.currentTimeMillis());
		callBackLocation(getLastlocation());
		saveLocation(loc);
	}

	void callBackLocation(Location location) {

		for (LocationTask task : tasks)
			if (task != null)
				task.onLocationUpdate(location);
	}

	@Override
	public void onProviderDisabled(String provider) {

		checkProvider(provider);
	}

	private void checkProvider(String provider) {

		if (isGps(provider)) {
			mLastlocation = null;
		}
	}

	private boolean isGps(String provider) {

		return provider.equals(LocationManager.GPS_PROVIDER);
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public boolean isRunningGpsListener() {

		Log.i(mLogTag, mLastlocation != null ? " Provider " + mLastlocation.getProvider() : " null");
		return mLastlocation != null && mLastlocation.getProvider().equals(LocationManager.GPS_PROVIDER);
	}

	public Location getBeforeLastLocation() {

		return mBeforeLastLocation;
	}

	public void removeLocationListener() {

		removeUpdate();
	}


	public interface LocationTask {

		void onLocationUpdate(Location location);
	}


	private class NetworkListener implements LocationListener {

		private final LocationTask networkListener;

		private boolean oneShot;

		public NetworkListener(LocationTask networkListener,boolean oneShot) {

			this.networkListener = networkListener;
			this.oneShot = oneShot;
		}

		@Override
		public void onLocationChanged(Location location) {

			networkListener.onLocationUpdate(location);

			if(oneShot)removeUpdate(this);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	}
}