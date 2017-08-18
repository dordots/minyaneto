package com.app.minyaneto_android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.app.minyaneto_android.entities.Minyan;
import com.app.minyaneto_android.entities.Synagogue;
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
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainScreenFragment.OnMapInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment implements OnMapReadyCallback, OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_RESOLUTION_REQUIRED = 123;

    private OnMapInteractionListener mListener;
    private GoogleMap mMap;
    private static MainScreenFragment _instance;

    private FusedLocationProviderClient mFusedLocationClient;
    private RecyclerView mRecyclerView;

    private List<Synagogue> synagogues;


    public MainScreenFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainScreenFragment newInstance() {
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
    public void onStart() {
        super.onStart();
        checkPermissions();
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
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

    private void handleLocationSetting() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSIONS_REQUEST_RESOLUTION_REQUIRED) {
            if (resultCode != 0) {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
                mapFragment.getMapAsync(this);
            } else {
                LatLng myLoc = new LatLng(31.7780628, 35.2353691);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
                updateCurrentLocation(myLoc);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private double lastZoom = -1;


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

        // Add a marker in Sydney and move the camera
        /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        enableMyLocationIcon(true);
        */
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
                        } else {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    findFirstLocation();
                                }
                            }, 1000);
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
        enableMyLocationIcon(true);
        updateSynagogues(mLocation);
    }

    private void updateSynagogues(final LatLng location) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        synagogues.clear();

        Synagogue s = new Synagogue("ירושלים קרית משה", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל משה", new LatLng(location.latitude + 0.001, location.longitude - 0.001), "ספרד", false, true, true, true);
        ArrayList<Minyan> minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "12:00", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים רמות", "לעלות בכניסה ג לקומה 2", "בית מנחם", new LatLng(location.latitude + 0.001, location.longitude + 0.001), "עדות המזרח", false, true, false, true);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "12:05", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים גבעת שאול", "כניסה ב", "אהל רבקה", new LatLng(location.latitude - 0.001, location.longitude - 0.001), "חב'ד", true, true, false, false);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "13:05", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים גבעת שאול", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה", new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז", false, true, true, false);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "12:05", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים בית הכרם", "לעלות בכניסה ג לקומה 2", "אהל לאה ורחל", new LatLng(location.latitude + 0.0037, location.longitude + 0.00281), "עדות המזרח", true, false, true, true);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "13:35", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים רמות", "כניסה ב", "כרם התימנים", new LatLng(location.latitude + 0.0041, location.longitude - 0.001), "ספרד", true, false, false, true);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "12:30", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים קרית משה", "לרדת 2 קומות", "מיימון", new LatLng(location.latitude - 0.0015, location.longitude + 0.005), "תימני", false, true, false, true);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "12:55", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        s = new Synagogue("ירושלים בית הדפוס", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה", new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז", false, true, true, false);
        minyens = new ArrayList<>();
        minyens.add(new Minyan("mincha", "sunday", "14:05", true));
        s.setMyMinyans(minyens);
        synagogues.add(s);

        updateMarkers();
        mMap.setInfoWindowAdapter(new SynagogueInfoWindowAdapter(getContext()));
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
            public void onItemClick(int position, View v) {
                if (position == -1) return;
                Synagogue Synagogue = synagogues.get(position);
                Toast.makeText(getActivity(), Synagogue.getName(), Toast.LENGTH_SHORT).show();
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
                    .title(sy.getName())
                    .snippet(sy.getAddress() + ":" + sy.getNosach() + ":" + sy.isSefer_tora() + ":" + sy.isClasses() + ":" + sy.isParking() + ":" + sy.isWheelchair_accessible())
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

    private void enableMyLocationIcon(boolean requestIfNotGranted) {
        if (mMap == null)
            return;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (requestIfNotGranted) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                // Message that the functionality won't work until the location permission achieved.

            }
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMapInteractionListener {

    }
}
