package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLngDoubleServer {

  @SerializedName("lat")
  @Expose
  private double lat;

  @SerializedName("lon")
  @Expose
  private double lon;

  public LatLngDoubleServer(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }

  public double getLat() {
    return lat;
  }

  public double getLon() {
    return lon;
  }
}
