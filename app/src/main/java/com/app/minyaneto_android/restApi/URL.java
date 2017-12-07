package com.app.minyaneto_android.restApi;


/**
 * class to contain all the url's for the app
 * <p/>
 * Created by David vardi .
 */
public class URL {

    public static String BASE_URL = "http://api.somthing.co.il/v1";


    public static String getUrlOrders() {

        return BASE_URL + "/user";
    }

    public static String getUser(int id) {

        return getUrlOrders() + "/" + id;
    }

    public static String getDistanceUrl(double lat1, double lon1, double lat2, double lon2) {

        return "http://maps.googleapis.com/maps/api/directions/" +
                "json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2
                + "&sensor=false&units=metric&mode=driving";
    }
}
