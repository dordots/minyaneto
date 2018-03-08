package com.app.minyaneto_android.restApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPIUtility {

  public static final String BASE_URL = "http://minyaneto.startach.com/v1/";

  public static SynagoguesRestAPI createSynagoguesRestAPI() {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SynagoguesRestAPI.class);
  }
}
