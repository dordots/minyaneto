package com.app.minyaneto_android.ui.acivities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.app.minyaneto_android.R;


public class NavigationHelper implements NavigationView.OnNavigationItemSelectedListener {

  private AppCompatActivity mActivity;
  private DrawerLayout mDrawer;
  private OnMenuItemSelectListener mListener;

  public NavigationHelper(AppCompatActivity mActivity, OnMenuItemSelectListener listener) {
    this.mActivity = mActivity;
    mListener = listener;
    initVariables();
  }

  private void initVariables() {
    Toolbar toolbar = mActivity.findViewById(R.id.sidebar_action);
    mActivity.setSupportActionBar(toolbar);
    mDrawer = mActivity.findViewById(R.id.drawerLayout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        mActivity, mDrawer, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    mDrawer.addDrawerListener(toggle);
    toggle.syncState();
    NavigationView navigationView = mActivity.findViewById(R.id.sidebar_navigation_view);
    navigationView.setNavigationItemSelectedListener(this);
    ActionBar supportActionBar = mActivity.getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.setHomeButtonEnabled(true);
    }
  }

  boolean closeDrawer() {
    if (mDrawer.isDrawerOpen(GravityCompat.START)) {
      mDrawer.closeDrawer(GravityCompat.START);
      return true;
    }
    return false;
  }


  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sidebar_home:
        mListener.onMenuSelectHome();
        break;
      case R.id.sidebar_searchMinyan:
        mListener.onMenuSelectSearchMinyan();
        break;
      case R.id.sidebar_searchSynagogue:
        mListener.onMenuSelectSearchSynagogue();
        break;
      case R.id.sidebar_addSynagogue:
        mListener.onMenuSelectAddSynagogue();
        break;
      case R.id.sidebar_zmanim:
        mListener.onMenuSelectZmanim();
        break;
    }
    closeDrawer();
    return true;
  }


  public interface OnMenuItemSelectListener {

    void onMenuSelectAddSynagogue();

    void onMenuSelectSearchSynagogue();

    void onMenuSelectSearchMinyan();

    void onMenuSelectHome();

    void onMenuSelectZmanim();
  }
}
