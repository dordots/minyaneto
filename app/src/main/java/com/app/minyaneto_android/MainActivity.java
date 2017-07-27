package com.app.minyaneto_android;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggel;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mToolbar = (Toolbar) findViewById(R.id.sidebar_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggel = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.action_bar_open, R.string.action_bar_close);

        mDrawerLayout.addDrawerListener(mToggel);
        mToggel.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.sidebar_navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);


        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggel.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sidebar_home:
                return true;
            case R.id.sidebar_settings:
                return true;
            case R.id.sidebar_addMinyan:
                return true;
            case R.id.sidebar_serach:
                return true;
            case R.id.sidebar_about:
                return true;
            default:
                return false;
        }
    }
}
