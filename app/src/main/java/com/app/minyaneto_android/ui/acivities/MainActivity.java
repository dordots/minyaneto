package com.app.minyaneto_android.ui.acivities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.fragments.MapFragment;
import com.app.minyaneto_android.ui.fragments.about_fragments.AboutFragment;
import com.app.minyaneto_android.ui.fragments.add_synagogue_fragments.AddSynagogueFragment;
import com.app.minyaneto_android.ui.fragments.main_screen_fragments.MainScreenFragment;
import com.app.minyaneto_android.ui.fragments.synagogue_details_fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.utilities.fragment.ActivityRunning;
import com.app.minyaneto_android.utilities.fragment.FragmentHelper;
import com.app.minyaneto_android.utilities.user.Alerts;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,
        ActivityCompat.OnRequestPermissionsResultCallback, NavigationHelper.OnMenuItemSelectListener {

    private static final String TAG  = MainActivity.class.getSimpleName();


    // Statics members
    public static Resources resources;

    private ArrayList<Fragment> liveFragments = new ArrayList<>();

    private boolean showRefreshButton = true;

    private String fragmentTitle = "";

    private NavigationHelper mNavigationHelper;

    private boolean doubleBackToExitPressedOnce = false;

    private RefreshMapDataClickListener myRefreshMapDataClickListener;

    private FragmentHelper mFragmentHelper;

    @Override
    public void onMenuSelectAddSynagogue() {

        changeFragment(AddSynagogueFragment.getInstance(new AddSynagogueFragment.OnSeccessAdd() {
            @Override
            public void OnSeccess(Synagogue synagogue) {
                changeFragment(SynagogueDetailsFragment.newInstance(synagogue,
                        new SynagogueDetailsFragment.WantCahngeFragmentListener() {
                            @Override
                            public void onWantToAddAMinyan(Fragment fragment) {
                                changeFragment(fragment);
                            }
                        }));
            }
        }));
    }

    @Override
    public void onMenuSelectAbout() {

        changeFragment(AboutFragment.getInstance());

    }

    @Override
    public void onMenuSelectHome() {

        MainScreenFragment.getInstance(this).checkPermissions(true);

        changeFragment(MainScreenFragment.getInstance(this));

    }

    @Override
    public void onSetActionBarTitle(String title) {

    }

    @Override
    public void onUpdateSynagogues(LatLng latLng) {

    }

    @Override
    public void onMarkerClick(int position) {

    }

    @Override
    public void onGetDistanse(double meters, String drivingTime) {

    }

    public interface RefreshMapDataClickListener {
        void onClickRefreshIcon();
    }

    public interface UpdateTitle {
        String onFragmentChange();
    }

    public void setMyUpdateTitle(UpdateTitle myUpdateTitle) {
        this.myUpdateTitle = myUpdateTitle;
    }

    private UpdateTitle myUpdateTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        initVars();

        MainActivity.resources = getResources();

        initMapFragment();

    }

    private void initMapFragment() {

        mFragmentHelper.addFragment(R.id.MA_container_map, MapFragment.newInstance(),MapFragment.TAG, null);
    }


    private void initVars() {

        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());

        mNavigationHelper = new NavigationHelper(this, this);

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {

        if (!mNavigationHelper.closeDrawer()) {


                closeWithDoubleClick();

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
        int id = item.getItemId();
        if (id == R.id.actionbar_refresh) {
            if (myRefreshMapDataClickListener != null) {
                myRefreshMapDataClickListener.onClickRefreshIcon();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            hideOtherFragments(ft);
            if (liveFragments.contains(fragment)) {
                ft.show(fragment);
                ft.commit();
            } else {
                if (fragment instanceof MainScreenFragment) {
                   // ft.add(R.id.views, fragment);
                } else {
                  //  ft.add(R.id.views, fragment).addToBackStack("tag");
                }
                ft.commit();
                liveFragments.add(fragment);
            }
            updateTitle(fragment);

        }
    }

    private void updateTitle(Fragment fragment) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        if (myUpdateTitle != null) {
            fragmentTitle = myUpdateTitle.onFragmentChange();
            invalidateOptionsMenu();
        }
        if (fragment instanceof MainScreenFragment) {
            if (!showRefreshButton) {
                showRefreshButton = true;
                invalidateOptionsMenu();
            }
        } else {
            if (showRefreshButton) {
                showRefreshButton = false;
                invalidateOptionsMenu();
            }
        }
    }

    private void hideOtherFragments(FragmentTransaction ft) {
        for (Fragment fragment : liveFragments) {
            ft.hide(fragment);
        }
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
        menu.findItem(R.id.actionbar_refresh).setVisible(showRefreshButton);
        //menu.findItem(R.id.sidebar_action).setTitle(fragmentTitle);
        return true;
    }

    public void setRefreshClickListener(RefreshMapDataClickListener listener) {
        this.myRefreshMapDataClickListener = listener;
    }
}
