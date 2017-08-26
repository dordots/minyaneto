package com.app.minyaneto_android.fragments.main_screen_fragments;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.minyaneto_android.SynagogueAdapter;
import com.app.minyaneto_android.acivities.MainActivity;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.fragments.synagogue_details_fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.map.SynagogueInfoWindowAdapter;
import com.app.minyaneto_android.models.Synagogue;
import com.app.minyaneto_android.utilities.SynagougeFictiveData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class MainScreenFragment extends Fragment implements OnMapReadyCallback, OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_RESOLUTION_REQUIRED = 123;

    private RecyclerView mRecyclerView;
    private List<Synagogue> synagogues;

    private GoogleMap mMap;

    private double lastZoom = -1;

    private FusedLocationProviderClient mFusedLocationClient;

    private static MainScreenFragment _instance;

    public MainScreenFragment() {/*Required empty public constructor*/}

    // TODO: Rename and change types and number of parameters
    public static MainScreenFragment getInstance() {
        if (_instance == null) {
            _instance = new MainScreenFragment();
        }
        return _instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        synagogues = new ArrayList<>();
        if (getActivity() instanceof MainActivity) {
            final OnMapReadyCallback currentOnMapReadyCallback = this;
            ((MainActivity) getActivity()).setRefreshClickListener(new MainActivity.RefreshMapDataClickListener() {
                @Override
                public void onClickRefreshIcon() {
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
                    mapFragment.getMapAsync(currentOnMapReadyCallback);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        handleLocationSetting();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermissions(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSIONS_REQUEST_RESOLUTION_REQUIRED) {
            if (resultCode != 0) {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
                mapFragment.getMapAsync(this);
            } else {
                LatLng myLoc = new LatLng(31.7780628, 35.2353691);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
                updateCurrentLocation(myLoc);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                if (cameraPosition.zoom <13.0) {
                    mMap.clear();
                } else {
                    if(Math.floor(cameraPosition.zoom)<=Math.floor(lastZoom)) {
                        return;
                    }
                    updateMarkers();
                }
                lastZoom=cameraPosition.zoom;

            }
        });
        findFirstLocation();

        // TODO: Is this code needed?
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @Override
            public void onCameraIdle() {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //enableMyLocationIcon(false);
                    handleLocationSetting();
                }
                return;
            }
        }
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

    private void handleLocationSetting() {
        if (!checkPermissions(false))
            return;

        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            displayLocationSettingsRequest(getActivity());
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
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

    private void findFirstLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            updateCurrentLocation(myLoc);
                        }
                    }
                });

    }

    private void updateCurrentLocation(LatLng mLocation) {
        if (mLocation == null) return;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLocation)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        enableMyLocationIcon();
        updateSynagogues(mLocation);
    }

    private void updateSynagogues(final LatLng location) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        synagogues = SynagougeFictiveData.getFictiveSynagouges(location);

        updateMarkers();
        //mMap.setInfoWindowAdapter(new SynagogueInfoWindowAdapter(getContext()));
        Collections.sort(synagogues, new Comparator<Synagogue>() {
            public int compare(Synagogue o1, Synagogue o2) {
                double dis1 = calculateDistance(o1.getGeo(), location);
                double dis2 = calculateDistance(o2.getGeo(), location);
                if (dis1 == dis2)
                    return 0;
                return dis1 < dis2 ? -1 : 1;
            }
        });
        final SynagogueAdapter adapter = new SynagogueAdapter(synagogues, location);
        adapter.setMyClickListener(new SynagogueAdapter.SynagogueClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == -1) return;
                Synagogue synagogue = synagogues.get(position);
                android.support.v4.app.DialogFragment f= SynagogueDetailsFragment.newInstance(synagogue);
                f.show(getFragmentManager(),"SynagogueDetailsFragment");
            }

            @Override
            public void onItemLongClick(int position, View v) {
               /* try {
                    if (position == -1) return;
                    synagogues.remove(position);
                    adapter.notifyItemRemoved(position);
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                */
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    public void updateMarkers() {
        mMap.clear();
        for (Synagogue sy : synagogues) {
            mMap.addMarker(new MarkerOptions()
                    .position(sy.getGeo())
                    .title(sy.getName()+" - "+sy.getNosach())
                    .snippet(sy.getAddress() /*+ ":" +
                             sy.getNosach() + ":" +
                             sy.isSefer_tora() + ":" +
                             sy.isClasses() + ":" +
                             sy.isParking() + ":" +
                             sy.isWheelchair_accessible()*/)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
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
        return false;
    }

    private void enableMyLocationIcon() {
        if (mMap == null)
            return;
        boolean permissionsStatus = checkPermissions(true);
        try{
            mMap.setMyLocationEnabled(permissionsStatus);
            mMap.getUiSettings().setMyLocationButtonEnabled(permissionsStatus);
        } catch(SecurityException ex){
            // Location Permission did not granted.
        }
    }
}
