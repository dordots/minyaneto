package com.app.minyaneto_android.restApi;

import com.app.minyaneto_android.models.data.SynagogueData;
import com.app.minyaneto_android.models.data.SynagoguesWrapperData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SynagoguesRestAPI {

    //    SAMPLE GET: "synagogues/?max_hits=20&center=31.786,35.186&radius=3km"
    @GET("synagogues")
    Call<SynagoguesWrapperData> getSynagoguesWrapperData(@Query("max_hits") int maxHits,
                                                         @Query("center") String center,
                                                         @Query("radius") String radius);

    @POST("synagogues")
    Call<String> addSynagogue(@Body SynagogueData synagogue);

    @PUT("synagogues/{id}")
    Call<Void> updateSynagogue(@Path("id") String id, SynagogueData synagogue);
}
