package com.app.minyaneto_android.utilities.fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david vardi.
 */

@SuppressLint("RestrictedApi")
public class FragmentHelper {

    private static final String TAG = FragmentHelper.class.getSimpleName();

    private final FragmentActivity mFragmentActivity;

    private List<AddFragment> commitFragments = new ArrayList<>();

    private ActivityRunning mActivityRunning;


    public FragmentHelper(FragmentActivity fragmentActivity, ActivityRunning activityRunning) {

        this.mFragmentActivity = fragmentActivity;

        this.mActivityRunning = activityRunning;

    }


    public void addFragment(int containerId, Fragment fragment, String tag, String tagToBackStack) {

        if (mActivityRunning.isRunning()) {

            FragmentTransaction fragmentTransaction =
                    this.mFragmentActivity.getSupportFragmentManager().beginTransaction();

            if (tagToBackStack != null) {

                fragmentTransaction.addToBackStack(tagToBackStack);

            }

            fragmentTransaction.add(containerId, fragment, tag);

            fragmentTransaction.commit();

        } else {

            addToListCommit(containerId, fragment, tag, tagToBackStack, AddFragment.Action.ADD);

        }
    }


    public void addFragment(int containerId, Fragment fragment, String tag) {

        if (mActivityRunning.isRunning()) {

            FragmentTransaction fragmentTransaction =
                    this.mFragmentActivity.getSupportFragmentManager().beginTransaction();

            fragmentTransaction.add(containerId, fragment, tag);

            fragmentTransaction.commit();

        } else {

            addToListCommit(containerId, fragment, tag, null, AddFragment.Action.ADD);

        }

    }


    public void replaceFragment(int containerId, Fragment fragment, String tag,
                                String tagToBackStack) {

        if (mActivityRunning.isRunning()) {

            FragmentTransaction fragmentTransaction =
                    this.mFragmentActivity.getSupportFragmentManager().beginTransaction();

            if (tagToBackStack != null) {

                fragmentTransaction.addToBackStack(tagToBackStack);

            }

            fragmentTransaction.replace(containerId, fragment, tag);

            fragmentTransaction.commit();

        } else {

            addToListCommit(containerId, fragment, tag, tagToBackStack, AddFragment.Action.REPLACE);

        }
    }


    public void popBackStack(String name) {

        if (mActivityRunning.isRunning())

            mFragmentActivity.getSupportFragmentManager()
                    .popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        else {

            addToListCommit(0, null, name, null, AddFragment.Action.POP);

        }

    }


    public void removeAllFragments() {

        List<Fragment> fragments = mFragmentActivity.getSupportFragmentManager().getFragments();

        mFragmentActivity.getSupportFragmentManager().popBackStack();

        mFragmentActivity.getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (fragments != null) {

            for (Fragment fragment : fragments) {

                removeFragment(fragment);

            }

        }

    }


    public void removeFragment(String tag, boolean popBackStack) {

        if (mActivityRunning.isRunning()) {

            Log.d(TAG ," removeFragment  mActivityRunning");

            Fragment fragment =
                    mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);

            removeFragment(fragment);


            if (popBackStack) {

                mFragmentActivity.getSupportFragmentManager().popBackStack();

            }

        } else {

            Log.d(TAG ," ! removeFragment  ! mActivityRunning");

            addToListCommit(0, null, tag, popBackStack, null, AddFragment.Action.REMOVE);

        }

    }


    public void removeFragment(Fragment fragment) {

        if (fragment != null) {

            Log.d(TAG , "removeFragment");

            FragmentTransaction fragmentTransaction =
                    this.mFragmentActivity.getSupportFragmentManager().beginTransaction();

            fragmentTransaction.remove(fragment)

                    .commit();
        }else {

            Log.d(TAG , "! removeFragment");

        }

    }


    public void commitAll() {

        for (AddFragment fragment : commitFragments) {

            if (fragment.getAction() == AddFragment.Action.ADD) {

                addFragment(fragment.getContainerId(), fragment.getFragment(), fragment.getTag());

            } else if (fragment.getAction() == AddFragment.Action.REPLACE) {

                replaceFragment(fragment.getContainerId(), fragment.getFragment(),
                        fragment.getTag(), fragment.getTagToBackStack());

            } else if (fragment.getAction() == AddFragment.Action.POP) {

                popBackStack(fragment.getTag());

            } else if (fragment.getAction() == AddFragment.Action.REMOVE) {

                removeFragment(fragment.getTag(), fragment.popBackStack());

            }

        }

        commitFragments.clear();

    }


    private void addToListCommit(int containerId, Fragment fragment, String tag,
                                 boolean popBackStack, String tagToBackStack,
                                 AddFragment.Action action) {

        AddFragment addFragment = new AddFragment(
                fragment,
                containerId,
                tag,
                popBackStack,
                tagToBackStack,
                action);

        commitFragments.add(addFragment);

    }

    private void addToListCommit(int containerId, Fragment fragment, String tag,
                                 String tagToBackStack, AddFragment.Action action) {

        AddFragment addFragment = new AddFragment(
                fragment,
                containerId,
                tag,
                tagToBackStack,
                action);

        commitFragments.add(addFragment);

    }


    public android.support.v4.app.FragmentManager getSupportFragmentManager() {

        return mFragmentActivity.getSupportFragmentManager();

    }


    public void removeFromBackStack(String... tag) {

        for (String t : tag) {

            mFragmentActivity.getSupportFragmentManager().popBackStack(t, 0);

        }

    }


    public boolean isCurrent(String tag) {

        if (mFragmentActivity != null && mFragmentActivity.getSupportFragmentManager() != null &&
                mFragmentActivity.getSupportFragmentManager().getFragments() != null) {

            int size = mFragmentActivity.getSupportFragmentManager().getFragments().size();

            if (size > 0) {

                Fragment fragment =
                        mFragmentActivity.getSupportFragmentManager().getFragments().get(size - 1);

                return fragment != null && fragment.getTag() != null &&
                        fragment.getTag().equals(tag);

            }
        }

        return false;

    }


    public boolean isContains(String tag) {

        if (mFragmentActivity.getSupportFragmentManager().getFragments() != null) {

            for (Fragment fragment : mFragmentActivity.getSupportFragmentManager().getFragments()) {

                if (fragment != null && fragment.getTag() != null && fragment.getTag().equals(tag)) {

                    return true ;

                }

            }

        }

        return false ;

    }

    public void addToBackStack(String tag) {

        mFragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(tag)
                .commit();

    }
}
