package com.app.minyaneto_android.acivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.fragments.add_minyan_fragments.AddMinyanFragment;
import com.app.minyaneto_android.fragments.add_synagogue_fragments.AddSynagogueFragment;
import com.app.minyaneto_android.fragments.main_screen_fragments.MainScreenFragment;
import com.app.minyaneto_android.fragments.settings_fragments.SettingsFragment;
import com.app.minyaneto_android.fragments.synagogue_details_fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.models.Synagogue;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ArrayList<Fragment> liveFragments = new ArrayList<>();
    private boolean showRefreshButton = true;

    private RefreshMapDataClickListener myRefreshMapDataClickListener;

    public interface RefreshMapDataClickListener {
        void onClickRefreshIcon();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sidebar_action);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.sidebar_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
        }

        AddSynagogueFragment.theInstance().setOnSeccessAdd(new AddSynagogueFragment.OnSeccessAdd() {
            @Override
            public void OnSeccess(Synagogue synagogue) {
                changeFragment(SynagogueDetailsFragment.newInstance(synagogue, new SynagogueDetailsFragment.WantCahngeFragmentListener() {
                    @Override
                    public void onWantCahngeFragment(Fragment fragment) {
                        changeFragment(fragment);
                    }
                }));
            }
        });

        changeFragment(MainScreenFragment.getInstance());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                ft.add(R.id.views, fragment);
                ft.commit();
                liveFragments.add(fragment);
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void hideOtherFragments(FragmentTransaction ft) {
        for (Fragment fragment : liveFragments) {
            ft.hide(fragment);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sidebar_home) {
            if (!showRefreshButton) {
                showRefreshButton = true;
                invalidateOptionsMenu();
            }
            MainScreenFragment.getInstance().checkPermissions(true);
            changeFragment(MainScreenFragment.getInstance());
        } else {
            if (showRefreshButton) {
                showRefreshButton = false;
                invalidateOptionsMenu();
            }
            if (id == R.id.sidebar_settings) {
               // changeFragment(SettingsFragment.theInstance("Hello", "World"));
            } else if (id == R.id.sidebar_addSynagogue) {
                changeFragment(AddSynagogueFragment.theInstance());

            } else if (id == R.id.sidebar_serach) {

            } else if (id == R.id.sidebar_about) {

            }
        }

        return true;
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
        return true;
    }

    public void setRefreshClickListener(RefreshMapDataClickListener listener) {
        this.myRefreshMapDataClickListener = listener;
    }
}
