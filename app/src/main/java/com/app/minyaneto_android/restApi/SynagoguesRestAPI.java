package com.app.minyaneto_android.restApi;

import com.app.minyaneto_android.data.SynagogueData;
import com.app.minyaneto_android.data.SynagogueIdData;
import com.app.minyaneto_android.data.SynagogueWrapperData;
import com.app.minyaneto_android.data.SynagoguesWrapperData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SynagoguesRestAPI {

    @Headers("Content-Type: application/json")
    @POST("synagogues/")
    Call<SynagogueIdData> addSynagogue(@Body SynagogueData synagogue);

    @PUT("synagogues/{id}")
    Call<Void> updateSynagogue(@Path("id") String id, SynagogueData synagogue);

    @GET("synagogues/{id}")
    Call<SynagogueWrapperData> getSynagogue(@Path("id") String id);

    @GET("synagogues")
    Call<SynagoguesWrapperData> getSynagoguesWrapperData(@Query("max_hits") int maxHits,
                                                         @Query("center") String center,
                                                         @Query("radius") String radius);
}
