package com.app.minyaneto_android.ui.acivities;

import android.app.AlertDialog;
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

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.domain.SynagogueCache;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.domain.SynagoguesSource;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.TimeUtility;
import com.app.minyaneto_android.restApi.ResponseListener;
import com.app.minyaneto_android.restApi.RestAPIUtility;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.app.minyaneto_android.Config.ADD_SYNAGOGUE_RADIUS_IN_KM;
import static com.app.minyaneto_android.Config.DEFAULT_RADIUS_IN_KM;
import static com.app.minyaneto_android.Config.MAX_HITS_PER_REQUEST;


public class MainActivity extends AppCompatActivity implements
        Alerts.OnCancelDialogListener,
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
        SearchSynagogueFragment.SearchListener {

    public MapFragment mapFragment;
    public SynagoguesFragment synagoguesFragment;
    public AddSynagogueFragment addSynagogueFragment;
    public SearchMinyanFragment searchMinyanFragment;
    private SynagoguesSource synagoguesSource;
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
        synagoguesSource = new SynagoguesSource(RestAPIUtility.createSynagoguesRestAPI(), new DataTransformer(), SynagogueCache.getInstance());
        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());
        mNavigationHelper = new NavigationHelper(this, this);
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


    private void returnToMain() {
        if (mFragmentHelper.isContains(AboutFragment.TAG))
            mFragmentHelper.removeFragment(AboutFragment.TAG, true);
        if (mFragmentHelper.isContains(ZmanimFragment.TAG))
            mFragmentHelper.removeFragment(ZmanimFragment.TAG, true);
        if (mFragmentHelper.isContains(SynagogueDetailsFragment.TAG))
            mFragmentHelper.removeFragment(SynagogueDetailsFragment.TAG, true);
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
        mapFragment.stopSearchMode();
        mFragmentHelper.replaceFragment(R.id.MA_container, addSynagogueFragment, AddSynagogueFragment.TAG, AddSynagogueFragment.TAG);
    }

    @Override
    public void onMenuSelectSearchSynagogue() {
        returnToMain();
        mapFragment.stopSearchMode();
        mFragmentHelper.replaceFragment(R.id.MA_container, SearchSynagogueFragment.getInstance(), SearchSynagogueFragment.TAG, SearchSynagogueFragment.TAG);
    }

    @Override
    public void onMenuSelectSearchMinyan() {
        returnToMain();
        if (null == searchMinyanFragment)
            searchMinyanFragment = SearchMinyanFragment.getInstance();
        mapFragment.stopSearchMode();
        mFragmentHelper.replaceFragment(R.id.MA_container, searchMinyanFragment, SearchMinyanFragment.TAG, SearchMinyanFragment.TAG);
    }

    @Override
    public void onMenuSelectAbout() {
        returnToMain();
        mFragmentHelper.addFragment(R.id.MA_main_container, AboutFragment.getInstance(), AboutFragment.TAG, AboutFragment.TAG);
    }

    @Override
    public void onAddSynagogue(String id) {
        showSynagogueDetails(id);
    }

    @Override
    public void onShowSynagogueDetails(String id) {
        showSynagogueDetails(id);
    }

    @Override
    public void onSearchMinyan() {
        returnToMain();
        mapFragment.stopSearchMode();
        mFragmentHelper.replaceFragment(R.id.MA_container, SearchMinyanFragment.getInstance(), SearchMinyanFragment.TAG, SearchMinyanFragment.TAG);
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
    public void onUpdateMarkers(List<SynagogueDomain> mSynagogues) {
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
    public void onUpdateMarker(LatLng latLng, String address) {
        mapFragment.startSearchMode(address);
        mapFragment.updateMarker(latLng, address);
    }

    @Override
    public void onGetTheSynagoguesAround(LatLng center) {
        try {
            synagoguesSource.fetchSynagogues(MAX_HITS_PER_REQUEST, center, ADD_SYNAGOGUE_RADIUS_IN_KM, new ResponseListener<List<SynagogueDomain>>() {
                @Override
                public void onResponse(List<SynagogueDomain> response) {
                    if (response != null && response.size() > 0) {
                        // Toast.makeText(MainActivity.this, R.string.attention_alert, Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage(R.string.attention_alert)
                                .show();
                    }
                    mapFragment.updateMarkers(response);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateSynagogues(final LatLng latLngCenter) {
        //TODO- choose the name of Tfila - according to this time
        if (mFragmentHelper.isContains(SynagoguesFragment.TAG) && null != mapFragment && isShowSynagoguesFragment) {
            updateSynagogues(latLngCenter, new Date(), null, null);
        }
    }

    private void updateSynagogues(final LatLng center,
                                  final Date date, final PrayType name, final String nosach) {
        try {
            synagoguesSource.fetchSynagogues(MAX_HITS_PER_REQUEST, center, DEFAULT_RADIUS_IN_KM, new ResponseListener<List<SynagogueDomain>>() {
                @Override
                public void onResponse(List<SynagogueDomain> response) {

                    if (null == response) {
                        synagoguesFragment.updateSynagogues(new ArrayList<SynagogueDomain>(0), "החיפוש לא הצליח");
                    }
                    List<SynagogueDomain> synagogues = response;

                    for (SynagogueDomain s : new ArrayList<>(synagogues)) {
                        if ((s.getMinyans().size() == 0) ||
                                (nosach != null && !nosach.equals(s.getNosach()))) {
                            synagogues.remove(s);
                            continue;
                        }

                        for (MinyanScheduleDomain m : new ArrayList<>(s.getMinyans())) {
                            if (name != null && m.getPrayType() != name) {
                                s.getMinyans().remove(m);
                            }
                        }
                        if ("".equals(TimeUtility.getTimes(s.getMinyans(), date))) {
                            synagogues.remove(s);
                        }
                    }
                    if (null == name)
                        synagoguesFragment.updateSynagogues(synagogues, getResources().getString(R.string.no_minyans_found));
                    else
                        synagoguesFragment.updateSynagogues(synagogues, getResources().getString(R.string.no_minyans_found_for_time));

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            addSynagogueFragment.updateLatLng(latLng);
        }
    }

    @Override
    public void onWantToAddAMinyan(String id) {
        mFragmentHelper.replaceFragment(R.id.MA_main_container, AddMinyanFragment.newInstance(id), AddMinyanFragment.TAG, AddMinyanFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        if (!mNavigationHelper.closeDrawer()) {
            if (mFragmentHelper.getFragmentsSize() > 2) {
                super.onBackPressed();
            } else if (mFragmentHelper.isContains(AddSynagogueFragment.TAG) ||
                    mFragmentHelper.isContains(SearchSynagogueFragment.TAG) ||
                    mFragmentHelper.isContains(SearchMinyanFragment.TAG)) {
                mapFragment.stopSearchMode();
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
            try {
                super.onBackPressed();
            } catch (Exception ignored) {
            }
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
        if (null != mapFragment)
            mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mapFragment)
            mapFragment.onActivityResult(requestCode, resultCode, data);
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
    public void onSearchSynagogue(String address, final LatLng center) {
        mFragmentHelper.replaceFragment(R.id.MA_container, synagoguesFragment, SynagoguesFragment.TAG, null);
        mapFragment.startSearchMode(address);
        isShowSynagoguesFragment = true;
        if (null != mapFragment) {
            try {
                synagoguesSource.fetchSynagogues(MAX_HITS_PER_REQUEST, center, DEFAULT_RADIUS_IN_KM, new ResponseListener<List<SynagogueDomain>>() {
                    @Override
                    public void onResponse(List<SynagogueDomain> response) {
                        synagoguesFragment.updateSynagogues(response, getResources().getString(R.string.no_synagogues_found));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void showSynagogueDetails(String id) {
        mFragmentHelper.replaceFragment(R.id.MA_main_container, SynagogueDetailsFragment.newInstance(id), SynagogueDetailsFragment.TAG, SynagogueDetailsFragment.TAG);
    }

    @Override
    public void onCancelAlertDialog() {

    }

    @Override
    public void onClickOkAlertDialog() {

    }
}
