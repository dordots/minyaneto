package com.app.minyaneto_android.fragments.main_screen_fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.app.minyaneto_android.acivities.MainActivity;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.fragments.synagogue_details_fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.models.client.CustomJSONObjectRequest;
import com.app.minyaneto_android.models.client.GenericJsonParser;
import com.app.minyaneto_android.models.client.JSONObjectRequestHandlerInterface;
import com.app.minyaneto_android.models.client.ModelObject;
import com.app.minyaneto_android.models.client.VolleyRequestQueueSingleton;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.WeekDay;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.utilities.SynagougeFictiveData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static android.content.Context.LOCATION_SERVICE;

public class MainScreenFragment extends Fragment implements OnMapReadyCallback,
        OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener, ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_RESOLUTION_REQUIRED = 123;
    private static final int MAX_DISTANCE_FROM_LAST_LOCATION = 2000;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    private RecyclerView mRecyclerView;
    private List<Synagogue> synagogues;

    private static ChangeFragment changeFragment;
    private GoogleMap mMap;
    private List<Marker> synagoguesMarkers;

    SynagogueAdapter adapter;
    private double lastZoom = -1;
    private LatLng lastLatLng = null;
    private static MainScreenFragment _instance;

    public interface ChangeFragment {
        void OnChangeFragment(Fragment fragment);
    }

    public static void setChangeFragment(ChangeFragment changeFragment) {
        MainScreenFragment.changeFragment = changeFragment;
    }

    public MainScreenFragment() {/*Required empty public constructor*/}

    public static MainScreenFragment getInstance(Activity context) {
        if (_instance == null) {
            _instance = new MainScreenFragment();
        }
        if (context instanceof MainActivity) {
            ((MainActivity) context)
                    .setActionBarTitle(context.getResources().getString(R.string.main_screen_fragment));
        }
        return _instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synagoguesMarkers = new ArrayList<>();
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

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        checkPermissions(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity())
                    .setActionBarTitle(getResources().getString(R.string.main_screen_fragment));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        handleLocationSetting();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @Override
            public void onCameraIdle() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                if (cameraPosition.zoom < 13.0) {
                    mMap.clear();
                } else {
                    if (Math.floor(cameraPosition.zoom) > Math.floor(lastZoom)) {
                        updateMarkers();
                    }
                }
                lastZoom = cameraPosition.zoom;
                if (lastZoom < 13.0)
                    return;
                LatLng pos = mMap.getCameraPosition().target;
                if (calculateDistance(lastLatLng, pos) > MAX_DISTANCE_FROM_LAST_LOCATION) {
                    lastLatLng = pos;
                    updateSynagogues(pos);
                }
            }

        });
        displayLocation();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    private void updateCurrentLocation(LatLng mLocation) {
        if (mLocation == null) return;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLocation)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                // .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        enableMyLocationIcon();
        lastLatLng = mLocation;
        updateSynagogues(mLocation);
    }

    private void updateSynagogues(final LatLng location) {
        /*String url = "http://minyaneto.startach.com/v1/synagogues/?top_left=42.0000000,-72.0000000&bottom_right=40.0000000,-74.0000000";
        CustomJSONObjectRequest<String> cjsobj = new CustomJSONObjectRequest<String>(Request.Method.GET, url, null);

        JSONObjectRequestHandlerInterface<String> analyzer = new JSONObjectRequestHandlerInterface<String>() {
            @Override
            public boolean isProcessReceivedDataImplemented() {
                return true;
            }

            @Override
            public boolean isExecuteCommandsImplemented() {
                return true;
            }
//{"synagogues":[{
// "classes":false,     "comments":"",
// "geo":{"lat":"40.654807","lon":"-74.17735"},
// "minyans":[{"day":"sunday","name":"mincha","time":"13:45:00"},
// {"day":"monday","name":"mincha","time":"13:45:00"},
// {"day":"tuesday","name":"mincha","time":"13:45:00"},
// {"day":"wednesday","name":"mincha","time":"13:45:00"},
// {"day":"thursday","name":"mincha","time":"13:45:00"}],
// "nosach":"Sefard","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"200 Murray Street , Elizabeth, USA","classes":true,"comments":"","geo":{"lat":"40.662402","lon":"-74.222289"},"minyans":[{"day":"sunday","name":"shachrit","time":"08:30:00"},{"day":"saturday","name":"shachrit","time":"09:00:00"}],"name":"Adath Jeshurun","nosach":"Ashkenaz","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"153 Bellevue Street, Elizabeth, USA","classes":false,"comments":"","geo":{"lat":"40.66088","lon":"-74.23651"},"minyans":[{"day":"monday","name":"shachrit","time":"06:25:00"},{"day":"thursday","name":"shachrit","time":"06:25:00"},{"day":"sunday","name":"shachrit","time":"08:00:00"},{"day":"rosh-chodesh","name":"shachrit","time":"06:15:00"},{"day":"tuesday","name":"shachrit","time":"06:30:00"},{"day":"wednesday","name":"shachrit","time":"06:30:00"},{"day":"friday","name":"shachrit","time":"06:30:00"},{"day":"saturday","name":"shachrit","time":"08:30:00"},{"day":"friday","name":"mincha_kabalat_shabat","time":"19:00:00"}],"name":"Bais Yitzchok","nosach":"Ashkenaz","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"330 Elmora Avenue , Elizabeth, USA","classes":true,"comments":"Ashkenaz and Eidot haMizrach. For all other times, see http:\/\/thejec.org\/weekly-schedule","geo":{"lat":"40.667202","lon":"-74.23399"},"minyans":[{"day":"monday","name":"shachrit","time":"06:40:00"},{"day":"thursday","name":"shachrit","time":"06:40:00"},{"day":"monday","name":"shachrit","time":"08:00:00"},{"day":"thursday","name":"shachrit","time":"08:00:00"},{"day":"sunday","name":"shachrit","time":"07:30:00"},{"day":"sunday","name":"shachrit","time":"08:30:00"},{"day":"rosh-chodesh","name":"shachrit","time":"08:00:00"},{"day":"rosh-chodesh","name":"shachrit","time":"06:30:00"},{"day":"tuesday","name":"shachrit","time":"08:00:00"},{"day":"wednesday","name":"shachrit","time":"08:00:00"},{"day":"friday","name":"shachrit","time":"08:00:00"},{"day":"tuesday","name":"shachrit","time":"06:50:00"},{"day":"wednesday","name":"shachrit","time":"06:50:00"},{"day":"friday","name":"shachrit","time":"06:50:00"}],"name":"JEC Elmora \/ Main Shul","nosach":"Multiple","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"1391 North Ave., Elizabeth, USA","classes":false,"comments":"This is only a minyan mincha during the yeshiva zman","geo":{"lat":"40.6825308","lon":"-74.2131845"},"minyans":[{"day":"sunday","name":"mincha","time":"15:05:00"},{"day":"monday","name":"mincha","time":"15:05:00"},{"day":"tuesday","name":"mincha","time":"15:05:00"},{"day":"wednesday","name":"mincha","time":"15:05:00"},{"day":"thursday","name":"mincha","time":"15:05:00"},{"day":"sunday","name":"maariv","time":"21:25:00"},{"day":"monday","name":"maariv","time":"21:25:00"},{"day":"tuesday","name":"maariv","time":"21:25:00"},{"day":"wednesday","name":"maariv","time":"21:25:00"},{"day":"thursday","name":"maariv","time":"21:25:00"}],"name":"Yeshiva Beer Yitzchok","nosach":" ","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"1391 North Ave., Elizabeth, USA","classes":false,"comments":"We are 5 minutes away from Newark Airport.  Come chap a seder if theres a plane delay or come daven with us. There are no Yeshiva minyanim during Bein HaZmanim. ","geo":{"lat":"40.682282","lon":"-74.213231"},"minyans":[{"day":"monday","name":"shachrit","time":"08:00:00"},{"day":"thursday","name":"shachrit","time":"08:00:00"},{"day":"sunday","name":"shachrit","time":"08:00:00"},{"day":"sunday","name":"mincha","time":"15:05:00"},{"day":"monday","name":"mincha","time":"15:05:00"},{"day":"tuesday","name":"mincha","time":"15:05:00"},{"day":"wednesday","name":"mincha","time":"15:05:00"},{"day":"thursday","name":"mincha","time":"15:05:00"},{"day":"sunday","name":"maariv","time":"21:15:00"},{"day":"monday","name":"maariv","time":"21:15:00"},{"day":"tuesday","name":"maariv","time":"21:15:00"},{"day":"wednesday","name":"maariv","time":"21:15:00"},{"day":"thursday","name":"maariv","time":"21:15:00"},{"day":"rosh-chodesh","name":"shachrit","time":"08:00:00"},{"day":"tuesday","name":"shachrit","time":"08:00:00"},{"day":"wednesday","name":"shachrit","time":"08:00:00"},{"day":"friday","name":"shachrit","time":"08:00:00"}],"name":"Yeshivas Be`er Yitzchok \/ Kollel of Elizabeth","nosach":"Ashkenaz","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"7 Slater Dr., Elizabeth, USA","classes":false,"comments":"Cor. Trunball St., park at the employee \/ visitors parking. ring bell","geo":{"lat":"40.652607","lon":"-74.17913"},"minyans":[{"day":"sunday","name":"mincha","time":"14:15:00"},{"day":"monday","name":"mincha","time":"14:15:00"},{"day":"tuesday","name":"mincha","time":"14:15:00"},{"day":"wednesday","name":"mincha","time":"14:15:00"},{"day":"thursday","name":"mincha","time":"14:15:00"}],"name":"Adorama Warehouse ","nosach":"Sefard","parking":null,"sefer-tora":null,"wheelchair-accessible":null},{"address":"1391 North Avenue, Elizabeth, USA","classes":true,"comments":"Ashkenaz and Eidot haMizrach. For all other times, see http:\/\/schedule.thejec.org","geo":{"lat":"40.68214","lon":"-74.21334"},"minyans":[{"day":"monday","name":"shachrit","time":"06:20:00"},{"day":"thursday","name":"shachrit","time":"06:20:00"},{"day":"monday","name":"shachrit","time":"07:00:00"},{"day":"thursday","name":"shachrit","time":"07:00:00"},{"day":"monday","name":"shachrit","time":"08:00:00"},{"day":"thursday","name":"shachrit","time":"08:00:00"},{"day":"sunday","name":"shachrit","time":"06:45:00"},{"day":"sunday","name":"shachrit","time":"08:00:00"},{"day":"sunday","name":"mincha","time":"15:05:00"},{"day":"monday","name":"mincha","time":"15:05:00"},{"day":"tuesday","name":"mincha","time":"15:05:00"},{"day":"wednesday","name":"mincha","time":"15:05:00"},{"day":"thursday","name":"mincha","time":"15:05:00"},{"day":"tuesday","name":"shachrit","time":"07:00:00"},{"day":"wednesday","name":"shachrit","time":"07:00:00"},{"day":"friday","name":"shachrit","time":"07:00:00"},{"day":"tuesday","name":"shachrit","time":"06:25:00"},{"day":"wednesday","name":"shachrit","time":"06:25:00"},{"day":"friday","name":"shachrit","time":"06:25:00"},{"day":"saturday","name":"shachrit","time":"08:45:00"},{"day":"tuesday","name":"shachrit","time":"08:00:00"},{"day":"wednesday","name":"shachrit","time":"08:00:00"},{"day":"friday","name":"shachrit","time":"08:00:00"}],"name":"JEC Adath Israel\/North Avenue shul","nosach":"Multiple","parking":null,"sefer-tora":null,"wheelchair-accessible":null}]}

            //https://stackoverflow.com/questions/36291420/parsing-json-data-into-model-objects-in-java
            @Override
            public String processReceivedData(JSONObject jsObj) {
                Log.d("json_subtree", "processReceivedData");
                JSONArray _synagoges = null;
                try {
                    _synagoges = jsObj.getJSONArray("synagogues");
                    List<Synagogue> listCourses = new ArrayList<Synagogue>();
                    List<JSONObject> listObjs = parseJsonData(jsObj,"teachers");
                    for (JSONObject c: listObjs) {
                        Synagogue s = new Synagogue();
                        s.setName(c.getString("name"));
                        s.setAddress(c.getString("address"));
                       // s.s
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                //parser json
                return null;
            }

            @Override
            public boolean executeCommands(String processedData) {
                Log.d("json_subtree", "executeCommands");
                if (processedData == "Done") {
                    int i = 0;
                    for (Synagogue synagogue : synagogues) {
                        getDistance(synagogue.getGeo().latitude, synagogue.getGeo().longitude,
                                mLastLocation.getLatitude(), mLastLocation.getLongitude(), i);
                        i++;
                    }

                    updateAdapter();
                    return true;
                }
                return false;
            }
        };
        cjsobj.setmJsonRequestHandler(analyzer);
        cjsobj.addToRequestQueue(VolleyRequestQueueSingleton.getInstance(getContext()));
       */

        synagogues = SynagougeFictiveData.getFictiveSynagouges(location);
        updateAdapter();



    }

    public static List<JSONObject> parseJsonData(JSONObject obj, String pattern)throws JSONException {

        List<JSONObject> listObjs = new ArrayList<JSONObject>();
        JSONArray geodata = obj.getJSONArray (pattern);
        for (int i = 0; i < geodata.length(); ++i) {
            final JSONObject site = geodata.getJSONObject(i);
            listObjs.add(site);
        }
        return listObjs;
    }

    private void updateAdapter() {
        //mMap.setInfoWindowAdapter(new SynagogueInfoWindowAdapter(getContext()));
        Collections.sort(synagogues, new Comparator<Synagogue>() {
            public int compare(Synagogue o1, Synagogue o2) {
                if (o1.getDistanceFromLocation() == o2.getDistanceFromLocation())
                    return 0;
                return o1.getDistanceFromLocation() < o2.getDistanceFromLocation() ? -1 : 1;
            }
        });
        updateMarkers();

        adapter = new SynagogueAdapter(synagogues, getContext());
        adapter.setMyClickListener(new SynagogueAdapter.SynagogueClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == -1) return;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(synagogues.get(position).getGeo())      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                synagoguesMarkers.get(position).showInfoWindow();
            }

            @Override
            public void onGoToWazeClick(int position) {
                if (position == -1) return;
                LatLng loc = synagogues.get(position).getGeo();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + loc.latitude + "," + loc.longitude));
                startActivity(intent);
            }

            @Override
            public void onShowDetailsClick(int position) {
                if (position == -1) return;
                if (changeFragment != null)
                    changeFragment.OnChangeFragment(SynagogueDetailsFragment.newInstance(synagogues.get(position), new SynagogueDetailsFragment.WantCahngeFragmentListener() {
                        @Override
                        public void onWantToAddAMinyan(Fragment fragment) {
                            changeFragment.OnChangeFragment(fragment);
                        }
                    }));
            }
        });
        mRecyclerView.setAdapter(adapter);


    }

    public void updateMarkers() {
        if (mMap == null) return;
        mMap.clear();
        synagoguesMarkers = new ArrayList<>();
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm");
        for (Synagogue sy : synagogues) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(sy.getGeo())
                    .title(sy.getName() + " - " + sy.getNosach())
                    .snippet(format.format(getCurrentMinyan(sy.getMinyans())) /*+ ":" +
                             sy.getNosach() + ":" +
                             sy.isSefer_tora() + ":" +
                             sy.isClasses() + ":" +
                             sy.isParking() + ":" +
                             sy.isWheelchair_accessible()*/)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            synagoguesMarkers.add(m);
        }
    }

    private Date getCurrentMinyan(ArrayList<Minyan> myMinyans) {
        Date now = new Date();
        Collections.sort(myMinyans, new Comparator<Minyan>() {
            public int compare(Minyan o1, Minyan o2) {
                Date date1 = o1.getTime().toDate(WeekDay.values()[new Date().getDay()]);
                Date date2 = o2.getTime().toDate(WeekDay.values()[new Date().getDay()]);
                if (date1.equals(date2))
                    return 0;
                return date1.before(date2) ? -1 : 1;
            }
        });
        return myMinyans.get(0).getTime().toDate(WeekDay.values()[new Date().getDay()]);
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
                //TODO select the current item in the list
                mRecyclerView.getLayoutManager().scrollToPosition(i);

                return false;
            }
            i++;
        }
        return false;
    }

    Handler handler = new Handler();

    public void getDistance(final double lat1, final double lon1, final double lat2, final double lon2, final int index) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    final String parsedDistance = distance.getString("text");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synagogues.get(index).setDistanceFromLocation(1000 * Double.parseDouble(parsedDistance.split(" ")[0]));
                            if (index == synagogues.size() - 1) {
                                updateAdapter();
                            }
                        }
                    });
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enableMyLocationIcon() {
        if (mMap == null)
            return;
        boolean permissionsStatus = checkPermissions(true);
        try {
            mMap.setMyLocationEnabled(permissionsStatus);
            mMap.getUiSettings().setMyLocationButtonEnabled(permissionsStatus);
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
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
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
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

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
}
