package com.app.minyaneto_android.utilities;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import timber.log.Timber;

@SuppressLint("RestrictedApi")
public class FragmentHelper {

  private final FragmentActivity mFragmentActivity;

  public FragmentHelper(FragmentActivity fragmentActivity) {
    this.mFragmentActivity = fragmentActivity;
  }

  public void addFragment(int containerId, Fragment fragment, String tag) {
    if (fragment.isAdded()) {
      return;
    }
    FragmentTransaction fragmentTransaction =
        this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(containerId, fragment, tag);
    fragmentTransaction.commit();
  }

  public void replaceFragment(int containerId, Fragment fragment, String tag,
      String tagToBackStack) {

    if (fragment.isAdded()) {
      return;
    }
    FragmentTransaction fragmentTransaction =
        this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
    if (tagToBackStack != null) {
      fragmentTransaction.addToBackStack(tagToBackStack);
    }
    fragmentTransaction.replace(containerId, fragment, tag);
    fragmentTransaction.commit();
  }

  public void removeFragment(String tag) {
    Timber.d(" removeFragment  mActivityRunning");
    Fragment fragment =
        mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
    if (fragment != null) {
      Timber.d("removeFragment");
      FragmentTransaction fragmentTransaction =
          this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
      fragmentTransaction.remove(fragment)
          .commit();
    } else {
      Timber.d("! removeFragment");
    }
    mFragmentActivity.getSupportFragmentManager().popBackStack();
  }


  public boolean contains(String tag) {
    if (mFragmentActivity.getSupportFragmentManager().getFragments() != null) {
      for (Fragment fragment : mFragmentActivity.getSupportFragmentManager().getFragments()) {
        if (fragment != null && fragment.getTag() != null && fragment.getTag().equals(tag)) {
          return true;
        }
      }
    }
    return false;
  }

  public int getFragmentsSize() {
    int size = 0;
    if (mFragmentActivity.getSupportFragmentManager().getFragments() != null) {
      for (Fragment fragment : mFragmentActivity.getSupportFragmentManager().getFragments()) {
        if (fragment != null) {
          size++;
        }
      }
    }
    return size;
  }
}
