package com.app.minyaneto_android.ui.acivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.minyaneto_android.Injection;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.geo.Geocoded;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.minyan.Time;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.synagogue.SynagogueArray;
import com.app.minyaneto_android.restApi.RequestHelper;
import com.app.minyaneto_android.ui.fragments.AboutFragment;
import com.app.minyaneto_android.ui.fragments.AddMinyanFragment;
import com.app.minyaneto_android.ui.fragments.AddSynagogueFragment;
import com.app.minyaneto_android.ui.fragments.MapFragment;
import com.app.minyaneto_android.ui.fragments.SearchFragment;
import com.app.minyaneto_android.ui.fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.ui.fragments.SynagoguesFragment;
import com.app.minyaneto_android.utilities.fragment.ActivityRunning;
import com.app.minyaneto_android.utilities.fragment.FragmentHelper;
import com.app.minyaneto_android.utilities.user.Alerts;
import com.app.minyaneto_android.zmanim.ZmanimFragment;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ravtech.co.il.httpclient.ErrorResponse;
import ravtech.co.il.httpclient.model.ErrorData;
import ravtech.co.il.httpclient.model.Result;


public class MainActivity extends AppCompatActivity implements
        MapFragment.OnFragmentInteractionListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        NavigationHelper.OnMenuItemSelectListener,
        AddSynagogueFragment.AddSynagogueListener,
        SynagogueDetailsFragment.WantCahngeFragmentListener,
        SynagoguesFragment.OnSynagoguesListener,
        AboutFragment.AboutListener, AddMinyanFragment.AddMinyanListener, SearchFragment.SearchListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public MapFragment mapFragment;
    public SynagoguesFragment synagoguesFragment;
    public ArrayList<Synagogue> synagogues;
    public AddSynagogueFragment addSynagogueFragment;
<<<<<<< HEAD

    public ArrayList<Synagogue> originSynagogues = new ArrayList<>();
=======
    public ArrayList<Synagogue> originSynagogues;
    private ArrayList<Fragment> liveFragments = new ArrayList<>();
    private NavigationHelper mNavigationHelper;
    private boolean doubleBackToExitPressedOnce = false;
    private FragmentHelper mFragmentHelper;
    private boolean isShowSynagoguesFragment = true;
>>>>>>> master

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

        invalidateOptionsMenu();

        returnToMain();

        initSynagoguesFragment();

        if (mapFragment != null) {

            mapFragment.onRefreshMap();
        }

    }

    @Override
    public void onMenuSelectZmanim() {
        returnToMain();
        mFragmentHelper.addFragment(R.id.MA_main_container, new ZmanimFragment(), ZmanimFragment.TAG, ZmanimFragment.TAG);
    }

    @Override
    public void onMenuSelectAddSynagogue() {

        returnToMain();

        if (addSynagogueFragment == null)

            addSynagogueFragment = AddSynagogueFragment.getInstance();

        mFragmentHelper.replaceFragment(R.id.MA_container, addSynagogueFragment, AddSynagogueFragment.TAG, AddSynagogueFragment.TAG);

    }

    private void returnToMain() {

        if (mFragmentHelper.isContains(AboutFragment.TAG))

            mFragmentHelper.removeFragment(AboutFragment.TAG, true);

        if (mFragmentHelper.isContains(ZmanimFragment.TAG))

            mFragmentHelper.removeFragment(ZmanimFragment.TAG, true);

    }

    @Override
    public void onMenuSelectSearchSynagogue() {

        mFragmentHelper.replaceFragment(R.id.MA_container, SearchFragment.getInstance(), SearchFragment.TAG, null);

    }

    @Override
    public void onMenuSelectAbout() {

        mFragmentHelper.addFragment(R.id.MA_main_container, AboutFragment.getInstance(), AboutFragment.TAG, AboutFragment.TAG);

    }


    @Override
    public void onShowSynagogueDetails(String id) {

        if (null == originSynagogues)
            return;

        Synagogue synagogue = null;

        for (Synagogue s : originSynagogues) {

            if (s.getId().equals(id)) {

                synagogue = s;

                break;
            }
        }

        if (null != synagogue)

            mFragmentHelper.replaceFragment(R.id.MA_main_container, SynagogueDetailsFragment.newInstance(synagogue), SynagogueDetailsFragment.TAG, SynagogueDetailsFragment.TAG);

        else {
            //TODO - show error message?
        }
    }

    @Override
    public void onSetActionBarTitle(String title) {

        if (title != null && getSupportActionBar() != null)

            getSupportActionBar().setTitle(title);

        else if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {

            onSetActionBarTitle(getResources().getString(R.string.main_screen_fragment));

        } else if (mFragmentHelper.isContains(AddSynagogueFragment.TAG)) {

            onSetActionBarTitle(getResources().getString(R.string.add_synagogue_fragment));
        }


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
    public void onUpdateMarker(Place place) {

        mapFragment.updateMarker(place);

    }

    @Override
    public void onUpdateSynagogues(final LatLng latLngCenter) {
        //TODO- choose the name of Tfilla - according to this time
        if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {
            if (null != mapFragment) {
                LatLng[] myBounds = mapFragment.onGetBounds();
                if (myBounds.length == 2) {
                    if (isShowSynagoguesFragment) {
                        updateSynagogues(latLngCenter, myBounds[0], myBounds[1], new Date(), null, null);
                    }
                }
            }
        }
    }

    private void updateSynagogues(final LatLng latLngCenter, LatLng northeast, LatLng southwest,
                                  final Date date, final PrayType name, final String nosach) {
        RequestHelper.getSynagogues(this, northeast, southwest, new Response.Listener<SynagogueArray>() {
            @Override
            public void onResponse(SynagogueArray response) {

                originSynagogues.clear();

                for (int i = 0;i<response.getSynagogues().size(); i++) {

                    ArrayList<Minyan> minyens = new ArrayList<Minyan>();

                    for (Minyan minyan : response.getSynagogues().get(i).getMinyans()) {
                        minyens.add(minyan);
                    }
                    originSynagogues.add(response.getSynagogues().get(i));
                    originSynagogues.get(i).setMinyans(minyens);
                }


                for (Synagogue s : originSynagogues) {
<<<<<<< HEAD
                    //    s.setMinyans(new ArrayList<Minyan>(s.getMinyans()));
=======
                    // The following line is the weirdest code line Iv'e ever seen
                    s.setMinyans(new ArrayList<>(s.getMinyans()));
>>>>>>> master
                    s.refreshData();
                }
                for (Synagogue s : new ArrayList<>(response.getSynagogues())) {
                    s.refreshData();
                    s.setDistanceFromLocation(calculateDistance(s.getGeo(), latLngCenter));
                    if ((s.getMinyans().size() == 0) ||
                            (nosach != null && !nosach.equals(s.getNosach()))) {
                        response.getSynagogues().remove(s);
                        continue;
                    }

                    for (Minyan m : new ArrayList<>(s.getMinyans())) {
                        if ((m.getPrayDayType().ordinal() != date.getDay()) ||
                                (name != null && m.getPrayType() != name)) {
                            s.getMinyans().remove(m);
                        }
                    }
                    String msg = getTimes(s.getMinyans(), date);
                    s.setMinyansAsString(msg);
                    if ("".equals(s.getMinyansAsString())) {
                        response.getSynagogues().remove(s);
                    }
                }

                synagogues = response.getSynagogues();
                synagoguesFragment.updateSynagogues(synagogues);

            }
        }, new ErrorResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Result<ErrorData> error) {
                error.getData().getMessage();
            }
        });
    }

    private String getTimes(ArrayList<Minyan> minyans, Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String result = "";
        ArrayList<String> myResult = new ArrayList<>();
        for (Minyan minyan : minyans) {
            Time time = minyan.getTime();
            //TODO calculate real time -like rosh hodesh..
            //Date f = minyan.getTime().toDate(WeekDay.values()[minyan.getPrayDayType().ordinal()]);
            Calendar cal = Calendar.getInstance();
            Log.d("-------------", cal.getTime().toString());
            cal.setTime(date);
            if (date.getDay() != minyan.getPrayDayType().ordinal())
                continue;
            cal.set(Calendar.DAY_OF_WEEK, minyan.getPrayDayType().ordinal() + 1);
            cal.set(Calendar.HOUR_OF_DAY, time.getHour());
            cal.set(Calendar.MINUTE, time.getMinutes());
            Log.d("-------------", cal.getTime().toString());
            Date f = cal.getTime();
            if (f.after(date)) {
                result = result + " ," + format.format(f);
                myResult.add(format.format(f));
            }
        }
        Log.d("------------", myResult.toString());
        Collections.sort(myResult, new Comparator<String>() {
            public int compare(String o1, String o2) {
                Date date1 = new Date();
                date1.setHours(Integer.parseInt(o1.split(":")[0]));
                date1.setMinutes(Integer.parseInt(o1.split(":")[1]));

                Date date2 = new Date();
                date2.setHours(Integer.parseInt(o2.split(":")[0]));
                date2.setMinutes(Integer.parseInt(o2.split(":")[1]));

                if (date1.equals(date2))
                    return 0;
                return date1.before(date2) ? -1 : 1;
            }
        });
        return myResult.toString().substring(1, myResult.toString().length() - 1);//result;
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

        if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {

            synagoguesFragment.scrollToSynagoguePosition(position);

        }
    }

    @Override
    public void onGetDistanse(double meters, String drivingTime) {

        if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {

// TODO: 20/12/2017
        }
    }

    @Override
    public void onWantToAddAMinyan(Synagogue synagogue) {

        mFragmentHelper.replaceFragment(R.id.MA_main_container, AddMinyanFragment.newInstance(synagogue), AddMinyanFragment.TAG, AddMinyanFragment.TAG);

    }

    @Override
    public void onBackPressed() {

        if (!mNavigationHelper.closeDrawer()) {

            if (mFragmentHelper.isContains(AddSynagogueFragment.TAG) || mFragmentHelper.getFragmentsSize() > 2) {

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


    @Override
    public void onSearch(LatLng latLngCenter, Date date, PrayType prayType, String nosach) {

        mFragmentHelper.replaceFragment(R.id.MA_container, synagoguesFragment, SynagoguesFragment.TAG, null);

        isShowSynagoguesFragment = true;

        if (null != mapFragment) {
            LatLng[] myBounds = mapFragment.onGetBounds();
            if (myBounds.length == 2) {
                if (isShowSynagoguesFragment) {
                    updateSynagogues(latLngCenter, myBounds[0], myBounds[1], date, prayType, nosach);
                }
            }
        }
    }
}
