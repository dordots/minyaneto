package com.app.minyaneto_android.restApi;


import java.util.HashMap;
import java.util.TimeZone;

public class Headers {

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    private static final String TIMEZONE = "timezone";

    public static HashMap<String, String> getHeaders() {

        HashMap<String, String> mHeaders = new HashMap<>();

        mHeaders.put(CONTENT_TYPE, APPLICATION_JSON);

        mHeaders.put(TIMEZONE, TimeZone.getDefault().getID());

        return mHeaders;
    }
}
