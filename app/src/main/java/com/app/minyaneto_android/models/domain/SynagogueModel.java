package com.app.minyaneto_android.models.domain;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SynagogueModel {
    private String address;
    private Boolean classes;
    private String comments;
    private String id;
    private List<MinyanScheduleModel> minyans;
    private String name;
    private String nosach;
    private Boolean parking;
    private Boolean seferTora;
    private Boolean wheelchairAccessible;
    private LatLng location;

    public SynagogueModel(String address,
                          Boolean classes,
                          String comments,
                          String id,
                          List<MinyanScheduleModel> minyans,
                          String name,
                          String nosach,
                          Boolean parking,
                          Boolean seferTora,
                          Boolean wheelchairAccessible,
                          LatLng location) {
        this.address = address;
        this.classes = classes;
        this.comments = comments;
        this.id = id;
        this.minyans = minyans;
        this.name = name;
        this.nosach = nosach;
        this.parking = parking;
        this.seferTora = seferTora;
        this.wheelchairAccessible = wheelchairAccessible;
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getClasses() {
        return classes;
    }

    public String getComments() {
        return comments;
    }

    public String getId() {
        return id;
    }

    public List<MinyanScheduleModel> getMinyans() {
        return minyans;
    }

    public String getName() {
        return name;
    }

    public String getNosach() {
        return nosach;
    }

    public Boolean getParking() {
        return parking;
    }

    public Boolean getSeferTora() {
        return seferTora;
    }

    public Boolean getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public LatLng getLocation() {
        return location;
    }
}
