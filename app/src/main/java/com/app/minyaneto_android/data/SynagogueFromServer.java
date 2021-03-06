package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SynagogueFromServer {

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
  private LatLngStringServer latLngStringServer;

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("minyans")
  @Expose
  private List<MinyanScheduleServer> minyans;

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

  public SynagogueFromServer(
      String address,
      Boolean classes,
      String comments,
      LatLngStringServer latLngStringServer,
      String id,
      List<MinyanScheduleServer> minyans,
      String name,
      String nosach,
      Boolean parking,
      Boolean seferTora,
      Boolean wheelchairAccessible) {

    this.address = address;
    this.classes = classes;
    this.comments = comments;
    this.latLngStringServer = latLngStringServer;
    this.id = id;
    this.minyans = minyans;
    this.name = name;
    this.nosach = nosach;
    this.parking = parking;
    this.seferTora = seferTora;
    this.wheelchairAccessible = wheelchairAccessible;
  }

  public String getName() {
    return name;
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

  public LatLngStringServer getLatLngStringServer() {
    return latLngStringServer;
  }

  public String getId() {
    return id;
  }

  public List<MinyanScheduleServer> getMinyans() {
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
