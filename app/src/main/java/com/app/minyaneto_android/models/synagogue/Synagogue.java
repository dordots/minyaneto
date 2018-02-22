package com.app.minyaneto_android.models.synagogue;

import android.os.Parcelable;

import com.app.minyaneto_android.models.minyan.Minyan;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Synagogue implements Cloneable , Serializable{

    public static final String TAG = Synagogue.class.getName();

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("classes")
    @Expose
    private Boolean classes;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("geo")
    @Expose
    private Geo geo;
    @SerializedName("id")
    @Expose(serialize = false)
    private String id;
    @SerializedName("minyans")
    @Expose
    private ArrayList<Minyan> minyans;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nosach")
    @Expose
    private String nosach;
    @SerializedName("parking")
    @Expose
    private Boolean parking;
    @SerializedName("sefer-tora")
    @Expose
    private Boolean seferTora;
    @SerializedName("wheelchair-accessible")
    @Expose
    private Boolean wheelchairAccessible;
    @Expose(serialize = false)
    private double distanceFromLocation;
    @Expose(serialize = false)
    private LatLng latLng;
    @Expose(serialize = false)
    private String minyansAsString;

    public Synagogue() {
        this.minyans = new ArrayList<>();
    }

    public String getMinyansAsString() {
        return minyansAsString;
    }

    public void setMinyansAsString(String minyansAsString) {
        this.minyansAsString = minyansAsString;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getClasses() {
        return classes;
    }

    public void setClasses(Boolean classes) {
        this.classes = classes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LatLng getGeo() {

        return latLng;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
        try {
            latLng = new LatLng(geo.getLat(), geo.getLon());
        } catch (Exception ignored) {
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Minyan> getMinyans() {
        return minyans;
    }

    public void setMinyans(ArrayList<Minyan> minyans) {
        this.minyans = minyans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNosach() {
        return nosach;
    }

    public void setNosach(String nosach) {
        this.nosach = nosach;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getSeferTora() {
        return seferTora;
    }

    public void setSeferTora(Boolean seferTora) {
        this.seferTora = seferTora;
    }

    public Boolean getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(Boolean wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public void addMinyan(Minyan minyan) {
        this.minyans.add(minyan);
    }

    public double getDistanceFromLocation() {
        return distanceFromLocation;
    }

    public void setDistanceFromLocation(double distanceFromLocation) {
        this.distanceFromLocation = distanceFromLocation;
    }

    public void refreshData() {
        try {
            latLng = new LatLng(geo.getLat(), geo.getLon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Minyan m : new ArrayList<>(minyans)) {
            try {
                m.refreshData();

            } catch (Exception ex) {
                minyans.remove(m);
            }
        }
    }
}
