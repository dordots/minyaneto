package com.app.minyaneto_android.acivities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.fragments.about_fragments.AboutFragment;
import com.app.minyaneto_android.fragments.add_synagogue_fragments.AddSynagogueFragment;
import com.app.minyaneto_android.fragments.main_screen_fragments.MainScreenFragment;
import com.app.minyaneto_android.fragments.synagogue_details_fragments.SynagogueDetailsFragment;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.zmanim.ZmanimFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {


    // Statics members
    public static Resources resources;

    private ArrayList<Fragment> liveFragments = new ArrayList<>();
    private boolean showRefreshButton = true;

    private RefreshMapDataClickListener myRefreshMapDataClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MainActivity.resources = getResources();

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
        MainScreenFragment.setChangeFragment(new MainScreenFragment.ChangeFragment() {
            @Override
            public void OnChangeFragment(Fragment fragment) {
                changeFragment(fragment);
            }
        });
        changeFragment(MainScreenFragment.getInstance(this));

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
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
                if (fragment instanceof MainScreenFragment) {
                    ft.add(R.id.views, fragment);
                } else {
                    ft.add(R.id.views, fragment).addToBackStack("tag");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sidebar_home) {
            MainScreenFragment.getInstance(this).checkPermissions(true);
            changeFragment(MainScreenFragment.getInstance(this));
        } else if (id == R.id.sidebar_addSynagogue) {
            changeFragment(AddSynagogueFragment.getInstance(new AddSynagogueFragment.OnSuccessAdd() {
                @Override
                public void OnSuccess(Synagogue synagogue) {
                    changeFragment(SynagogueDetailsFragment.newInstance(synagogue,
                            new SynagogueDetailsFragment.WantCahngeFragmentListener() {
                                @Override
                                public void onWantToAddAMinyan(Fragment fragment) {
                                    changeFragment(fragment);
                                }
                            }));
                }
            }));
        } else if (id == R.id.sidebar_about) {
            changeFragment(AboutFragment.getInstance());
        } else if (id == R.id.sidebar_zmanim) {
            changeFragment(new ZmanimFragment());
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
        //menu.findItem(R.id.sidebar_action).setTitle(fragmentTitle);
        return true;
    }

    public void setRefreshClickListener(RefreshMapDataClickListener listener) {
        this.myRefreshMapDataClickListener = listener;
    }

    public interface RefreshMapDataClickListener {
        void onClickRefreshIcon();
    }
}
