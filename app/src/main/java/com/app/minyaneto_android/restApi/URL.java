package com.app.minyaneto_android.restApi;

import com.google.android.gms.maps.model.LatLng;

public class URL {

    public static String BASE_URL = "http://minyaneto.startach.com/v1/";


    public static String getUrlSynagogues(LatLng center) {

//        return BASE_URL + "synagogues/?max_hits=20&top_left=" + 33.2326675 + "," + 34.0780113 +
//                "&bottom_right=" + 29.3842887 + "," + 35.8924053;

//        http://minyaneto.startach.com/v1/synagogues/?max_hits=1000&center=32.80462387123734,35.061328982421855&radius=30km

        return BASE_URL + "synagogues/?max_hits=20&center=" + center.latitude + "," + center.longitude +
                "&radius=0.3km";
//        return "http://minyaneto.startach.com/v1/synagogues/?max_hits=1000&center=32.80462387123734,35.061328982421855&radius=10km";
    }


    public static String getUrlOrders() {

        return BASE_URL + "/user";
    }

    public static String getAddSynagogue() {

        return BASE_URL + "synagogues/";
    }

    public static String getUpdateSynagogue(String id) {
        return BASE_URL + "synagogues/" + id;
    }

    public static String getUser(int id) {

        return getUrlOrders() + "/" + id;
    }
}
