package com.app.minyaneto_android.restApi;

import com.app.minyaneto_android.models.synagogue.SynagogueArrayData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SynagoguesRestAPI {

    //    SAMPLE GET: "synagogues/?max_hits=20&center=31.786,35.186&radius=3km"
    @GET("synagogues")
    Call<SynagogueArrayData> getSynagoguesArrayData(@Query("max_hits") String maxHits,
                                                    @Query("center") String latitude,
                                                    @Query("radius") String radius);
}
