package com.app.minyaneto_android.models.synagogue;

import com.app.minyaneto_android.models.minyan.Minyan;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Synagogue {
    private String id;
    private String address;
    private String comments;
    private String name;
    private String nosach;
    private double distanceFromLocation;
    private LatLng geo;
    private boolean classes;
    private boolean parking;
    private boolean sefer_tora;
    private boolean wheelchair_accessible;
    private ArrayList<Minyan> minyans;

    public Synagogue(String address, String comments, String name, LatLng geo, String nosach, boolean classes, boolean parking, boolean sefer_tora, boolean wheelchair_accessible) {
        this.address = address;
        this.comments = comments;
        this.name = name;
        this.geo = geo;
        this.nosach = nosach;
        this.classes = classes;
        this.parking = parking;
        this.sefer_tora = sefer_tora;
        this.wheelchair_accessible = wheelchair_accessible;
        this.minyans = new ArrayList<>();
    }

    public Synagogue() {
        this.minyans = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getGeo() {
        return geo;
    }

    public void setGeo(LatLng geo) {
        this.geo = geo;
    }

    public String getNosach() {
        return nosach;
    }

    public void setNosach(String nosach) {
        this.nosach = nosach;
    }

    public boolean isClasses() {
        return classes;
    }

    public void setClasses(boolean classes) {
        this.classes = classes;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isSefer_tora() {
        return sefer_tora;
    }

    public void setSefer_tora(boolean sefer_tora) {
        this.sefer_tora = sefer_tora;
    }

    public boolean isWheelchair_accessible() {
        return wheelchair_accessible;
    }

    public void setWheelchair_accessible(boolean wheelchair_accessible) {
        this.wheelchair_accessible = wheelchair_accessible;
    }

    public ArrayList<Minyan> getMinyans() {
        return minyans;
    }

    public void setMinyans(ArrayList<Minyan> minyans) {
        this.minyans = minyans;
    }

    public double getDistanceFromLocation() {
        return distanceFromLocation;
    }

    public void setDistanceFromLocation(double distanceFromLocation) {
        this.distanceFromLocation = distanceFromLocation;
    }

    public boolean addMinyan(Minyan minyan) {
        if (minyan == null)
            return false;

        return minyans.add(minyan);
    }
}
