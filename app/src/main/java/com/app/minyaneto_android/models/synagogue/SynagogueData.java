package com.app.minyaneto_android.models.synagogue;

import com.app.minyaneto_android.models.minyan.MinyanData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    private Geo geo;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("minyans")
    @Expose
    private ArrayList<MinyanData> minyans;
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
}
