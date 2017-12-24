package com.app.minyaneto_android.ui.acivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.client.CustomJSONObjectRequest;
import com.app.minyaneto_android.models.client.HelpJsonParser;
import com.app.minyaneto_android.models.client.JSONObjectRequestHandlerInterface;
import com.app.minyaneto_android.models.client.VolleyRequestQueueSingleton;
import com.app.minyaneto_android.models.geo.Geocoded;
import com.app.minyaneto_android.restApi.RequestHelper;
import com.app.minyaneto_android.ui.fragments.AddMinyanFragment;
import com.app.minyaneto_android.ui.fragments.MapFragment;
import com.app.minyaneto_android.ui.fragments.AboutFragment;
import com.app.minyaneto_android.ui.fragments.AddSynagogueFragment;
import com.app.minyaneto_android.ui.fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.ui.fragments.SynagoguesFragment;
import com.app.minyaneto_android.utilities.fragment.ActivityRunning;
import com.app.minyaneto_android.utilities.fragment.FragmentHelper;
import com.app.minyaneto_android.utilities.user.Alerts;
import com.app.minyaneto_android.zmanim.ZmanimFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MapFragment.OnFragmentInteractionListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        NavigationHelper.OnMenuItemSelectListener,
        AddSynagogueFragment.AddSynagogueListener,
        SynagogueDetailsFragment.WantCahngeFragmentListener,
        SynagoguesFragment.OnSynagoguesListener,
        AboutFragment.AboutListener, AddMinyanFragment.AddMinyanListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Fragment> liveFragments = new ArrayList<>();

    private NavigationHelper mNavigationHelper;

    private boolean doubleBackToExitPressedOnce = false;

    private FragmentHelper mFragmentHelper;

    public MapFragment mapFragment;

    public SynagoguesFragment synagoguesFragment;

    private boolean isShowSynagoguesFragment = true;
    public ArrayList<Synagogue> synagogues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        initVars();

        initFragments();

    }

    private void initFragments() {

        initMapFragment();

        initSynagoguesFragment();
    }

    private void initSynagoguesFragment() {

        if (synagoguesFragment == null)

            synagoguesFragment = SynagoguesFragment.newInstance();
        mFragmentHelper.replaceFragment(R.id.MA_container, synagoguesFragment, SynagoguesFragment.TAG, null);

    }

    private void initMapFragment() {

        if (mapFragment == null)

            mapFragment = MapFragment.newInstance();

        mFragmentHelper.addFragment(R.id.MA_container_map, mapFragment, MapFragment.TAG, null);
    }


    private void initVars() {

        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());

        mNavigationHelper = new NavigationHelper(this, this);

    }

    @Override
    public void onMenuSelectHome() {

        isShowSynagoguesFragment = true;

        invalidateOptionsMenu();

        initSynagoguesFragment();

        if (mapFragment != null) {

            mapFragment.onRefreshMap();
        }

    }


    @Override
    public void onMenuSelectAddSynagogue() {

        isShowSynagoguesFragment = false;

        // TODO: 20/12/2017 change to add

        mFragmentHelper.replaceFragment(R.id.MA_container, AddSynagogueFragment.getInstance(), AddSynagogueFragment.TAG, null);

    }

    @Override
    public void onMenuSelectAbout() {

        mFragmentHelper.addFragment(R.id.MA_main_container, AboutFragment.getInstance(), AboutFragment.TAG, AboutFragment.TAG);

    }

    @Override
    public void onMenuSelectZmanim() {

        mFragmentHelper.addFragment(R.id.MA_main_container, new ZmanimFragment(), ZmanimFragment.TAG, ZmanimFragment.TAG);

    }


    @Override
    public void onShowSynagogueDetails(Synagogue synagogue) {

        mFragmentHelper.replaceFragment(R.id.MA_main_container, SynagogueDetailsFragment.newInstance(synagogue), SynagogueDetailsFragment.TAG, SynagogueDetailsFragment.TAG);

    }

    @Override
    public void onSetActionBarTitle(String title) {

        if (getSupportActionBar() != null)

            getSupportActionBar().setTitle(title);

    }

    @Override
    public void onUpdateMarkers(ArrayList<Synagogue> mSynagogues) {

        mapFragment.updateMarkers(mSynagogues);

    }

    @Override
    public void onMoveCamera(LatLng latLng, int position) {

        mapFragment.moveCamera(latLng);

        mapFragment.showInfoMarker(position);

    }

    @Override
    public void onOpenRoute(LatLng geo) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + geo.latitude + "," + geo.longitude));

        startActivity(intent);

    }

    @Override
    public void onUpdateMarker(LatLng latLng) {

        mapFragment.updateMarker(latLng);

    }

    @Override
    public void onUpdateSynagogues(LatLng latLng) {

        if (isShowSynagoguesFragment) {

            // TODO: לשלוח שאילתה לשרת לפי מיקום ולקבל רשימת בתי כנסת

            String url = "http://minyaneto.startach.com/v1/synagogues/?max_hits=20&top_left=" + 33.2326675 + "," + 34.0780113 +
                    "&bottom_right=" + 29.3842887 + "," + 35.8924053;
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

                @Override
                public String processReceivedData(JSONObject jsObj) {
                    Log.d("json_subtree", "processReceivedData");
                    synagogues = new ArrayList<>();
                    try {
                        List<JSONObject> listObjs = HelpJsonParser.parseJsonData(jsObj, "synagogues");

                        for (JSONObject obj : listObjs) {
                            try {
                                synagogues.add(new Synagogue(obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return "Done";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return "Error";
                }

                @Override
                public boolean executeCommands(String processedData) {
                    Log.d("json_subtree", "executeCommands");
                    if (processedData == "Done") {
                        int i = 0;
                        for (Synagogue synagogue : synagogues) {
                            getDistance(synagogue.getGeo().latitude, synagogue.getGeo().longitude,
                                    mapFragment.getLastLocation().getLatitude(), mapFragment.getLastLocation().getLongitude(), i);
                            i++;
                            if (i == 3)
                                continue;
                        }
                        synagoguesFragment.updateSynagogues(synagogues);

                        return true;
                    }
                    return false;
                }
            };
            cjsobj.setmJsonRequestHandler(analyzer);
            cjsobj.addToRequestQueue(VolleyRequestQueueSingleton.getInstance(this));
            //synagogues = SynagougeFictiveData.getFictiveSynagouges(location);
            //updateAdapter();


        }
    }

    public void getDistance(final double lat1, final double lon1, final double lat2, final double lon2, final int i) {

        RequestHelper.getDistance(MainActivity.this, lat1, lon1, lat2, lon2, new Response.Listener<Geocoded>() {
            @Override
            public void onResponse(Geocoded response) {

                // TODO: 07 דצמבר 2017 insert the real app key for this and add case for walking and for if the distance more than hour
                if (response.getStatus().equals("OK"))

                    synagogues.get(i).setDistanceFromLocation(Double.parseDouble(response.getRoutes().get(0).getLegs().get(0).getDistance().getText().split(" ")[0]) * 1000);
                //   response.getRoutes().get(0).getLegs().get(0).getDuration().getText());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }


    @Override
    public void onMarkerClick(int position) {
        // TODO: CR david - change boolean for tag
        if (isShowSynagoguesFragment) {

            synagoguesFragment.scrollToSynagoguePosition(position);

        }
    }

    @Override
    public void onGetDistanse(double meters, String drivingTime) {

        if (isShowSynagoguesFragment) {

// TODO: 20/12/2017
        }
    }

    @Override
    public void onWantToAddAMinyan() {

        mFragmentHelper.replaceFragment(R.id.MA_main_container, AddMinyanFragment.newInstance(), AddMinyanFragment.TAG, AddMinyanFragment.TAG);

    }

    @Override
    public void onBackPressed() {

        if (!mNavigationHelper.closeDrawer()) {

            if (mFragmentHelper.getFragmentsSize() > 2) {

                super.onBackPressed();

            } else {
                closeWithDoubleClick();
            }
        } else {

            super.onBackPressed();

        }
    }


    private void closeWithDoubleClick() {

        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();

            Intent intent = new Intent(Intent.ACTION_MAIN);

            intent.addCategory(Intent.CATEGORY_HOME);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            this.doubleBackToExitPressedOnce = true;

            return;
        }

        doubleBackToExitPressedOnce = true;

        Toast.makeText(this, R.string.click_back_again, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionbar_refresh:

                mapFragment.onRefreshMap();

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (Fragment fragment : liveFragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Alerts.ACTION_CODE_OPEN_GPS_SETTINGS) {


            return;

        }
        for (Fragment fragment : liveFragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.action_bar_menu, menu);

        menu.findItem(R.id.actionbar_refresh).setVisible(true);

        return true;
    }


}
