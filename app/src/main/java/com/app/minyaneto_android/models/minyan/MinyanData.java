package com.app.minyaneto_android.models.minyan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinyanData {
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("time")
    @Expose
    private String stringTime;
}