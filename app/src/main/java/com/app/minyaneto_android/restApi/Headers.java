package com.app.minyaneto_android.restApi;


import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by David vardi
 */
public class Headers {


    private static final String LANGUAGE = "language";

    private static final String DEVICE_ID = "deviceID";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    private static final String TIMEZONE = "timezone";


    public static HashMap<String, String> getHeaders() {

        HashMap<String, String> mHeaders = new HashMap<>();

      //  mHeaders.put(DEVICE_ID,UserManager.getUniqueId());

        mHeaders.put(CONTENT_TYPE, APPLICATION_JSON);

        mHeaders.put(TIMEZONE, TimeZone.getDefault().getID());

        return mHeaders;

    }







}
