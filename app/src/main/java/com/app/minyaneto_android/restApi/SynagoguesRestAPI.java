package com.app.minyaneto_android.restApi;

import com.app.minyaneto_android.data.IdFromServer;
import com.app.minyaneto_android.data.SynagogueFromServer;
import com.app.minyaneto_android.data.SynagogueToServer;
import java.util.List;
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
  Call<IdFromServer> addSynagogue(@Body SynagogueToServer synagogue);

  @PUT("synagogues/{id}")
  Call<Void> updateSynagogue(@Path("id") String id, @Body SynagogueToServer synagogue);

  @GET("synagogues/{id}")
  Call<SynagogueFromServer> getSynagogue(@Path("id") String id);

  @GET("synagogues")
  Call<List<SynagogueFromServer>> getSynagogues(@Query("max_hits") int maxHits,
      @Query("center") String center,
      @Query("radius") String radius);
}
