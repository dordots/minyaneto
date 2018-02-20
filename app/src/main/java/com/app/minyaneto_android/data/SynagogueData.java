package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SynagogueData {
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
    private LatLngData latLngData;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("minyans")
    @Expose
    private List<MinyanScheduleData> minyans;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LatLngData getLatLngData() {
        return latLngData;
    }

    public String getId() {
        return id;
    }

    public List<MinyanScheduleData> getMinyans() {
        return minyans;
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

}