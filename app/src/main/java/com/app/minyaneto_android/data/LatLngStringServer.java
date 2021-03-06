package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLngStringServer {

  @SerializedName("lat")
  @Expose
  private String lat;

  @SerializedName("lon")
  @Expose
  private String lon;

  public LatLngStringServer(String lat, String lon) {
    this.lat = lat;
    this.lon = lon;
  }

  public String getLat() {
    return lat;
  }

  public String getLon() {
    return lon;
  }
}
