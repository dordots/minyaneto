package com.app.minyaneto_android.entities;

import com.google.android.gms.maps.model.LatLng;

public class Synagogue {

    private String name;
    private LatLng location;
    private int nosachResId;

    public Synagogue(String name, LatLng location, int nosachResId) {
        this.name = name;
        this.location = location;
        this.nosachResId = nosachResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getNosachResId() {
        return nosachResId;
    }

    public void setNosachResId(int nosachResId) {
        this.nosachResId = nosachResId;
    }
}
