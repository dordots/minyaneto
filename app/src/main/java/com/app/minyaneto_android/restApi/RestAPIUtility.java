package com.app.minyaneto_android.restApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPIUtility {

  public static final String BASE_URL = "http://minyaneto.startach.com/";
  public static final String VERSION = "v1/";
  public static final String STAGING_PREFIX = "test-";

  public static SynagoguesRestAPI createSynagoguesRestAPI(String flavor) {
    String baseUrl = BASE_URL;
    if (flavor.equals("staging")) {
      baseUrl += STAGING_PREFIX;
    }
    baseUrl += VERSION;
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SynagoguesRestAPI.class);
  }
}
