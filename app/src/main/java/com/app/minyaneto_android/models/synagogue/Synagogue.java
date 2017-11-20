package com.app.minyaneto_android.models.synagogue;

import com.app.minyaneto_android.models.client.HelpJsonParser;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Synagogue {
    private String id;

    private String address;
    private String comments;
    private String name;
    private String nosach;
    private double distanceFromLocation;
    private int driving_time;
    private int walking_time;
    private LatLng geo;
    private boolean classes;
    private boolean parking;
    private boolean sefer_tora;
    private boolean wheelchair_accessible;
    private ArrayList<Minyan> minyans;

    public Synagogue(String address, String comments, String name, LatLng geo, String nosach, boolean classes,
                     boolean parking, boolean sefer_tora, boolean wheelchair_accessible,
                     int driving_time, int walking_time) {
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
        this.driving_time = driving_time;
        this.walking_time = walking_time;
    }

    public Synagogue(JSONObject object) throws JSONException {
        this.address = object.getString("address");
        this.name = object.getString("name");
        this.classes = object.getString("classes") == "true" ? true : false;
        this.comments = object.getString("comments");
        this.wheelchair_accessible = object.getString("wheelchair-accessible") == "true" ? true : false;
        this.nosach = object.getString("nosach");
        this.sefer_tora = object.getString("sefer-tora") == "true" ? true : false;
        this.parking = object.getString("parking") == "true" ? true : false;
        this.geo = new LatLng(object.getJSONObject("geo").getDouble("lat"), object.getJSONObject("geo").getDouble("lon"));

        this.minyans = new ArrayList<>();
        List<JSONObject> listObjs = HelpJsonParser.parseJsonData(object, "minyans");
        for (JSONObject ob : listObjs) {
            try {
                this.minyans.add(new Minyan(ob));
            } catch (ParseException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Synagogue() {
        this.minyans = new ArrayList<>();

    }

    public int getDriving_time() {
        return driving_time;
    }

    public void setDriving_time(int driving_time) {
        this.driving_time = driving_time;
    }

    public int getWalking_time() {
        return walking_time;
    }

    public void setWalking_time(int walking_time) {
        this.walking_time = walking_time;
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
