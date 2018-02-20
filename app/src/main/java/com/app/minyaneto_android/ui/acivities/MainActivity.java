package com.app.minyaneto_android.ui.acivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.synagogue.SynagogueArray;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.TimeUtility;
import com.app.minyaneto_android.restApi.RequestHelper;
import com.app.minyaneto_android.ui.fragments.AboutFragment;
import com.app.minyaneto_android.ui.fragments.AddMinyanFragment;
import com.app.minyaneto_android.ui.fragments.AddSynagogueFragment;
import com.app.minyaneto_android.ui.fragments.MapFragment;
import com.app.minyaneto_android.ui.fragments.SearchMinyanFragment;
import com.app.minyaneto_android.ui.fragments.SearchSynagogueFragment;
import com.app.minyaneto_android.ui.fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.ui.fragments.SynagoguesFragment;
import com.app.minyaneto_android.utilities.LocationHelper;
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
import java.util.Locale;

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
        AboutFragment.AboutListener,
        AddMinyanFragment.AddMinyanListener,
        SearchMinyanFragment.SearchListener,
        ZmanimFragment.ZmanimListener,
        SearchSynagogueFragment.SearchListener, ErrorResponse.ErrorListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double DEFUALT_RADUIS = 3;
    private static final double RADUIS_FOR_ADD_SYNAGOGUE = 0.3;
    public MapFragment mapFragment;
    public SynagoguesFragment synagoguesFragment;
    public ArrayList<Synagogue> synagogues;
    public AddSynagogueFragment addSynagogueFragment;

    public ArrayList<Synagogue> originSynagogues = new ArrayList<>();
    private NavigationHelper mNavigationHelper;
    private boolean doubleBackToExitPressedOnce = false;
    private FragmentHelper mFragmentHelper;
    private boolean isShowSynagoguesFragment = true;
    private MenuItem refresh_btn = null;

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


    private void returnToMain() {

        if (mFragmentHelper.isContains(AboutFragment.TAG))

            mFragmentHelper.removeFragment(AboutFragment.TAG, true);

        if (mFragmentHelper.isContains(ZmanimFragment.TAG))

            mFragmentHelper.removeFragment(ZmanimFragment.TAG, true);

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

    @Override
    public void onMenuSelectSearchSynagogue() {
        returnToMain();
        mFragmentHelper.replaceFragment(R.id.MA_container, SearchSynagogueFragment.getInstance(), SearchSynagogueFragment.TAG, SearchSynagogueFragment.TAG);
    }


    @Override
    public void onMenuSelectSearchMinyan() {
        returnToMain();
        mFragmentHelper.replaceFragment(R.id.MA_container, SearchMinyanFragment.getInstance(), SearchMinyanFragment.TAG, SearchMinyanFragment.TAG);
    }

    @Override
    public void onMenuSelectAbout() {
        returnToMain();

        mFragmentHelper.addFragment(R.id.MA_main_container, AboutFragment.getInstance(), AboutFragment.TAG, AboutFragment.TAG);

    }

    @Override
    public void onAddSynagogue(Synagogue synagogue) {
        mFragmentHelper.replaceFragment(R.id.MA_main_container, SynagogueDetailsFragment.newInstance(synagogue), SynagogueDetailsFragment.TAG, SynagogueDetailsFragment.TAG);
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

    }

    @Override
    public void onSetActionBarTitle(String title) {

        if (title != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            if (null != refresh_btn) {
                if (title.equals(getResources().getString(R.string.zmanim_fragment)) ||
                        title.equals(getResources().getString(R.string.sidebar_addMinyan)) ||
                        title.equals(getResources().getString(R.string.synagogue_details_fragment)) ||
                        title.equals(getResources().getString(R.string.about_fragment)))
                    refresh_btn.setVisible(false);
                else
                    refresh_btn.setVisible(true);
            }
        } else if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.main_screen_fragment));
        } else if (mFragmentHelper.isContains(AddSynagogueFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.add_synagogue_fragment));
        } else if (mFragmentHelper.isContains(SearchMinyanFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.search_minyan_fragment));
        } else if (mFragmentHelper.isContains(SearchSynagogueFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.search_synagogue_fragment));
        } else if (mFragmentHelper.isContains(AddMinyanFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.sidebar_addMinyan));
        } else if (mFragmentHelper.isContains(ZmanimFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.zmanim_fragment));
        } else if (mFragmentHelper.isContains(AboutFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.about_fragment));
        } else if (mFragmentHelper.isContains(SynagogueDetailsFragment.TAG)) {
            onSetActionBarTitle(getResources().getString(R.string.synagogue_details_fragment));
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
    public void onGetTheSynagoguesAround(LatLng lng) {

        RequestHelper.getSynagogues(this, lng, RADUIS_FOR_ADD_SYNAGOGUE, new Response.Listener<SynagogueArray>() {
            @Override
            public void onResponse(SynagogueArray response) {

                if (response != null && response.getSynagogues() != null && response.getSynagogues().size() > 0)

                    Toast.makeText(MainActivity.this, R.string.attention_alert, Toast.LENGTH_SHORT).show();

                mapFragment.updateMarkers(response.getSynagogues());

            }
        }, this);
    }

    @Override
    public void onUpdateSynagogues(final LatLng latLngCenter) {
        //TODO- choose the name of Tfilla - according to this time
        if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {
            if (null != mapFragment) {
                if (isShowSynagoguesFragment) {
                    updateSynagogues(latLngCenter, new Date(), null, null);

                }
            }
        }
    }

    private void updateSynagogues(final LatLng latLngCenter,
                                  final Date date, final PrayType name, final String nosach) {
        RequestHelper.getSynagogues(this, latLngCenter, DEFUALT_RADUIS, new Response.Listener<SynagogueArray>() {
            @Override
            public void onResponse(SynagogueArray response) {

                originSynagogues.clear();

                for (int i = 0; i < response.getSynagogues().size(); i++) {

                    ArrayList<Minyan> minyens = new ArrayList<>();

                    minyens.addAll(response.getSynagogues().get(i).getMinyans());
                    originSynagogues.add(response.getSynagogues().get(i));
                    originSynagogues.get(i).setMinyans(minyens);
                }


                for (Synagogue s : originSynagogues) {
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
                        if (name != null && m.getPrayType() != name) {
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
        }, this);
    }

    private String getTimes(ArrayList<Minyan> minyans, Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        StringBuilder result = new StringBuilder();
        ArrayList<String> myResult = new ArrayList<>();
        for (Minyan minyan : minyans) {
            //TODO calculate real time -like rosh hodesh..
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (date.getDay() != minyan.getPrayDayType().ordinal())
                continue;
            cal.set(Calendar.DAY_OF_WEEK, minyan.getPrayDayType().ordinal() + 1);
            ExactTime exactTime = TimeUtility.extractSpecificTime(minyan.getPrayTime(), LocationRepository.getInstance().getLastKnownLocation());
            cal.set(Calendar.HOUR_OF_DAY, exactTime.getHour());
            cal.set(Calendar.MINUTE, exactTime.getMinutes());
            Date f = cal.getTime();
            if (minyan.getPrayDayType().ordinal() == date.getDay() && f.after(date)) {
                result.append(" ,").append(format.format(f));
                myResult.add(format.format(f));
            }
        }
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

    @Override
    public void onMarkerClick(int position) {

        if (mFragmentHelper.isContains(SynagoguesFragment.TAG)) {

            synagoguesFragment.scrollToSynagoguePosition(position);

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (!mFragmentHelper.isContains(AddSynagogueFragment.TAG)) {
            return;
        }

        String addressLine = LocationHelper.getAddressLineFromLatLng(this, latLng);
        mapFragment.updateMarker(latLng, addressLine);

        if (addSynagogueFragment != null) {
            addSynagogueFragment.updateSynagogueAddress(addressLine);
        }
    }

    @Override
    public void onWantToAddAMinyan(Synagogue synagogue) {

        mFragmentHelper.replaceFragment(R.id.MA_main_container, AddMinyanFragment.newInstance(synagogue), AddMinyanFragment.TAG, AddMinyanFragment.TAG);

    }

    @Override
    public void onBackPressed() {

        if (!mNavigationHelper.closeDrawer()) {

            if (mFragmentHelper.isContains(AddSynagogueFragment.TAG) ||
                    mFragmentHelper.isContains(SearchSynagogueFragment.TAG) ||
                    mFragmentHelper.isContains(SearchMinyanFragment.TAG) ||
                    mFragmentHelper.getFragmentsSize() > 2) {

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Alerts.ACTION_CODE_OPEN_GPS_SETTINGS) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.action_bar_menu, menu);

        refresh_btn = menu.findItem(R.id.actionbar_refresh);
        refresh_btn.setVisible(true);

        return true;
    }

    @Override
    public void onSearchSynagogue(String address, final LatLng latLng) {

        mFragmentHelper.replaceFragment(R.id.MA_container, synagoguesFragment, SynagoguesFragment.TAG, null);

        mapFragment.startSearchMode(address);
        isShowSynagoguesFragment = true;

        if (null != mapFragment) {

            RequestHelper.getSynagogues(this, latLng, DEFUALT_RADUIS, new Response.Listener<SynagogueArray>() {

                @Override
                public void onResponse(SynagogueArray response) {

                    originSynagogues.clear();

                    for (Synagogue s : response.getSynagogues()) {

                        s.refreshData();
                        s.setDistanceFromLocation(calculateDistance(s.getGeo(), latLng));

                    }

                    originSynagogues = response.getSynagogues();

                    synagogues = originSynagogues;

                    synagoguesFragment.updateSynagogues(synagogues);
                }

            }, this);
        }

    }


    @Override
    public void onSearch(String address, LatLng latLngCenter, Date date, PrayType prayType, String nosach) {

        mFragmentHelper.replaceFragment(R.id.MA_container, synagoguesFragment, SynagoguesFragment.TAG, null);

        isShowSynagoguesFragment = true;


        mapFragment.startSearchMode(address);
        if (null != mapFragment) {
            updateSynagogues(latLngCenter, date, prayType, nosach);

        }
    }

    @Override
    public void onErrorResponse(Result<ErrorData> error) {
        // TODO: 2/12/2018  add error dialog David

        Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
    }
}
