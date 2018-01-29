
package com.app.minyaneto_android.models.synagogue;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.minyaneto_android.models.minyan.Minyan;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Synagogue implements Parcelable ,Cloneable{

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
    @Expose
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

    private double distanceFromLocation;

    private LatLng latLng;

    private String minyansAsString;

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
            latLng =new LatLng(Double.parseDouble(geo.getLat()), Double.parseDouble(geo.getLon()));
        }
        catch (Exception ex){

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
        this.minyans =minyans;
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

    public void addMinyan(Minyan minyan){
        this.minyans.add(minyan);
    }

    public double getDistanceFromLocation() {
        return distanceFromLocation;
    }

    public void setDistanceFromLocation(double distanceFromLocation) {
        this.distanceFromLocation = distanceFromLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.address);
        dest.writeString(this.comments);
        dest.writeString(this.name);
        dest.writeString(this.nosach);
        dest.writeDouble(this.distanceFromLocation);
        dest.writeParcelable(this.geo, flags);
        dest.writeByte(this.classes ? (byte) 1 : (byte) 0);
        dest.writeByte(this.parking ? (byte) 1 : (byte) 0);
        dest.writeByte(this.seferTora ? (byte) 1 : (byte) 0);
        dest.writeByte(this.wheelchairAccessible ? (byte) 1 : (byte) 0);
        dest.writeList(this.minyans);
    }

    public Synagogue(){
        this.minyans = new ArrayList<Minyan>();
    }

    protected Synagogue(Parcel in) {
        this.id = in.readString();
        this.address = in.readString();
        this.comments = in.readString();
        this.name = in.readString();
        this.nosach = in.readString();
        this.distanceFromLocation = in.readDouble();
        this.geo = in.readParcelable(Geo.class.getClassLoader());
        this.classes = in.readByte() != 0;
        this.parking = in.readByte() != 0;
        this.seferTora = in.readByte() != 0;
        this.wheelchairAccessible = in.readByte() != 0;
        this.minyans = new ArrayList<Minyan>();
        in.readList(this.minyans, Minyan.class.getClassLoader());
    }

    public static final Creator<Synagogue> CREATOR = new Creator<Synagogue>() {
        @Override
        public Synagogue createFromParcel(Parcel source) {
            return new Synagogue(source);
        }

        @Override
        public Synagogue[] newArray(int size) {
            return new Synagogue[size];
        }
    };

    public void refreshData() {
        try {
            latLng =new LatLng(Double.parseDouble(geo.getLat()), Double.parseDouble(geo.getLon()));
        }
        catch (Exception ignored){
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
