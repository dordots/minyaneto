package com.app.minyaneto_android.utilities.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by david vardi.
 */
public class AddFragment {

    public enum Action {
        ADD,
        REPLACE,
        POP,
        REMOVE
    }

    private Fragment fragment;
    private int containerId ;
    private String tag ;
    private boolean popBackStack ;
    private String tagToBackStack ;
    private Action action ;

    public AddFragment() {
    }

    public AddFragment(Fragment fragment, int containerId, String tag, boolean popBackStack , String tagToBackStack, Action action) {
        this.fragment = fragment;
        this.containerId = containerId;
        this.popBackStack = popBackStack ;
        this.tag = tag;
        this.tagToBackStack = tagToBackStack;
        this.action = action;

    }

    public AddFragment(Fragment fragment, int containerId, String tag, String tagToBackStack, Action action) {
        this.fragment = fragment;
        this.containerId = containerId;
        this.tag = tag;
        this.tagToBackStack = tagToBackStack;
        this.action = action;

    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean popBackStack() {
        return popBackStack;
    }

    public void setPopBackStack(boolean popBackStack) {
        this.popBackStack = popBackStack;
    }

    public String getTagToBackStack() {
        return tagToBackStack;
    }

    public void setTagToBackStack(String tagToBackStack) {
        this.tagToBackStack = tagToBackStack;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
