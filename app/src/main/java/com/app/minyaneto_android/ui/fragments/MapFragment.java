package com.app.minyaneto_android.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.directions.FetchUrl;
import com.app.minyaneto_android.directions.RouteListener;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.time.DateUtility;
import com.app.minyaneto_android.utilities.LocationHelper;
import com.app.minyaneto_android.utilities.Permissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by david vardi.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLongClickListener, RouteListener {

    public static final String TAG = MapFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_RESOLUTION_REQUIRED = 123;
    private static final int MAX_DISTANCE_FROM_LAST_LOCATION = 2000;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    // Location updates intervals in sec
    private static final int UPDATE_INTERVAL = 10000; // 10 sec
    private static final int FATEST_INTERVAL = 5000; // 5 sec
    private static final int DISPLACEMENT = 10; // 10 meters
    LatLngBounds latLngBounds;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private List<Marker> synagoguesMarkers;
    private double lastZoom = -1;
    private LatLng lastLatLng = null;
    private OnFragmentInteractionListener mListener;
    public SupportMapFragment mMapFragment;
    private Marker mAddSynagogueMarker;
    private Polyline mRoutePolyline;

    public static MapFragment newInstance() {

        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOnCreate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        init();

        return view;
    }

    private void initOnCreate() {

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    public void onRefreshMap() {

        mMapFragment.getMapAsync(this);

    }

    public LatLng[] onGetBounds() {
        LatLng[] latLngs = new LatLng[2];

        latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        latLngs[0] = latLngBounds.northeast;
        latLngs[1] = latLngBounds.southwest;
        return latLngs;
    }


    private void init() {

        mMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.FM_map);

        mMapFragment.getMapAsync(this);

        synagoguesMarkers = new ArrayList<>();


        handleLocationSetting();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSynagoguesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateMarker(LatLng latLng, String title) {
        if (mAddSynagogueMarker != null) {
            mAddSynagogueMarker.remove();
        }

        mAddSynagogueMarker = mMap.addMarker(new MarkerOptions().position(

                latLng)

                .title(title)

                .draggable(true)

                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        moveCamera(latLng);
    }

    public void updateMarker(Place place) {
        updateMarker(place.getLatLng(), place.getName().toString());
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (mAddSynagogueMarker != null) {
            String url = LocationHelper.getDirectionsUrl(mAddSynagogueMarker.getPosition(), latLng);
            FetchUrl FetchUrl = new FetchUrl(this);

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        }

        if (mListener != null) {
            mListener.onMapLongClick(latLng);
        }
    }

    @Override
    public void onRoutePolylineReceived(PolylineOptions polyline) {
        if (mRoutePolyline != null) {
            mRoutePolyline.remove();
        }

        mRoutePolyline = mMap.addPolyline(polyline);
    }

    public interface OnFragmentInteractionListener {

        void onUpdateSynagogues(LatLng latLng);

        void onMarkerClick(int position);

        void onGetDistanse(double meters, String drivingTime);

        void onMapLongClick(LatLng latLng);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {

            mGoogleApiClient.connect();
        }
        checkPermissions(true);

    }

    public boolean checkPermissions(boolean askIfNotGranted) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (askIfNotGranted)
                ActivityCompat.requestPermissions(getActivity(), new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null)

            stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {

            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        mMap.setOnMapLongClickListener(this);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @Override
            public void onCameraIdle() {

                CameraPosition cameraPosition = mMap.getCameraPosition();

                if (cameraPosition.zoom < 13.0) {

                    for (Marker m : synagoguesMarkers) {
                        setMarkerVisible(false);
                    }
                } else {

                    if (Math.floor(cameraPosition.zoom) > Math.floor(lastZoom)) {
                        setMarkerVisible(true);
                    }
                }

                lastZoom = cameraPosition.zoom;

                if (lastZoom < 13.0)
                    return;

                LatLng pos = mMap.getCameraPosition().target;

                if (calculateDistance(lastLatLng, pos) > MAX_DISTANCE_FROM_LAST_LOCATION) {

                    lastLatLng = pos;

                    //updateSynagogues(pos);
                }
            }

        });
        displayLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PERMISSIONS_REQUEST_RESOLUTION_REQUIRED) {

            if (resultCode != 0) {

                onRefreshMap();

            } else {

                LatLng myLoc = new LatLng(31.7780628, 35.2353691);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));

                updateCurrentLocation(myLoc);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleLocationSetting();
                }

                break;
            }
        }
    }

    private void handleLocationSetting() {

        if (!Permissions.checkPermissionForGPS(getActivity()))
            return;

        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = false;

        if (service != null) {

            enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }


        if (!enabled) {

            displayLocationSettingsRequest(getActivity());
        }
    }

    private void displayLocationSettingsRequest(Context context) {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();

        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(10000);

        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();

                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.i(TAG, "All location settings are satisfied.");
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), PERMISSIONS_REQUEST_RESOLUTION_REQUIRED);
                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void updateCurrentLocation(LatLng mLocation) {

        if (mLocation == null || mLocation.equals(lastLatLng)) return;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLocation)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                // .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(mLocation));

        enableMyLocationIcon();

        lastLatLng = mLocation;

        mListener.onUpdateSynagogues(mLocation);
    }

    public void updateMarkers(ArrayList<Synagogue> synagogues) {
        if (mMap == null) return;

        mMap.clear();

        synagoguesMarkers = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        for (Synagogue synagogue : synagogues) {

            Marker m = mMap.addMarker(new MarkerOptions().position(
                    synagogue.getGeo())
                    .title(synagogue.getName() + " - " + synagogue.getNosach())
                    //.snippet(synagogue.getMinyans().size() > 0 ? format.format(getCurrentMinyan(synagogue.getMinyans())) : "")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            synagoguesMarkers.add(m);
        }

    }

    private Date getCurrentMinyan(ArrayList<Minyan> minyans) {
        Collections.sort(minyans, new Comparator<Minyan>() {
            public int compare(Minyan minyan1, Minyan minyan2) {
                Date date1 = DateUtility.getDate(minyan1, getContext());
                Date date2 = DateUtility.getDate(minyan2, getContext());
                return date1.compareTo(date2);
            }
        });
        return DateUtility.getDate(minyans.get(0), getContext());
    }

    private long calculateDistance(LatLng location1, LatLng location2) {
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();

        int i = 0;

        for (Marker marker1 : synagoguesMarkers) {

            if (marker1.getPosition().equals(marker.getPosition())) {

                mListener.onMarkerClick(i);

                return false;
            }
            i++;
        }
        return false;
    }

    private void enableMyLocationIcon() {
        if (mMap == null)
            return;

        try {

            mMap.setMyLocationEnabled(true);

            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        } catch (SecurityException ex) {
            // Location Permission did not granted.
        }
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mGoogleApiClient == null)
            return;
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            updateCurrentLocation(new LatLng(latitude, longitude));
            mRequestingLocationUpdates = false;

        } else {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        mLocationRequest.setFastestInterval(FATEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {

        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if (resultCode != ConnectionResult.SUCCESS) {

            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {

                GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
            return;

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected())
            return;

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {

            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        // Assign the new location
        mLastLocation = location;

        // Displaying the new location on UI
        displayLocation();
    }

    public Location getLastLocation() {

        return mLastLocation;
    }

    public void moveCamera(LatLng lng) {

        CameraPosition cameraPosition = new CameraPosition.Builder()

                .target(lng)
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void showInfoMarker(int pos) {

        synagoguesMarkers.get(pos).showInfoWindow();

    }

    public void setMarkerVisible(boolean isVisible) {
        for (Marker m : synagoguesMarkers) {
            m.setVisible(isVisible);
        }
    }
}
