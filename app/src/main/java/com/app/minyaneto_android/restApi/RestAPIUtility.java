package com.app.minyaneto_android.restApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPIUtility {

  public static final String BASE_URL = "http://minyaneto.startach.com/";
  public static final String VERSION_SUFFIX = "v1/";
  public static final String DEBUG_VERSION_SUFFIX = "test-v1/";

  public static SynagoguesRestAPI createSynagoguesRestAPI(boolean isInDebugMode) {
    String baseUrl = BASE_URL;
    if (isInDebugMode) {
      baseUrl += DEBUG_VERSION_SUFFIX;
    } else {
      baseUrl += VERSION_SUFFIX;
    }
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SynagoguesRestAPI.class);
  }
}
