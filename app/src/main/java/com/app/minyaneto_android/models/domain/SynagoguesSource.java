package com.app.minyaneto_android.models.domain;

import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.data.IdFromServer;
import com.app.minyaneto_android.data.SynagogueFromServer;
import com.app.minyaneto_android.data.SynagogueToServer;
import com.app.minyaneto_android.restApi.ResponseListener;
import com.app.minyaneto_android.restApi.SynagoguesRestAPI;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SynagoguesSource {

  private SynagoguesRestAPI api;
  private DataTransformer transformer;
  private SynagogueCache cache;

  public SynagoguesSource(SynagoguesRestAPI api, DataTransformer transformer,
      SynagogueCache cache) {
    this.api = api;
    this.transformer = transformer;
    this.cache = cache;
  }

  public void fetchSynagogues(int maxHits, LatLng location, double radiusInKm,
      final ResponseListener<List<Synagogue>> listener) throws IOException {
    String center = location.latitude + "," + location.longitude;
    String radius = radiusInKm + "km";
    Callback<List<SynagogueFromServer>> callback = new Callback<List<SynagogueFromServer>>() {
      @Override
      public void onResponse(Call<List<SynagogueFromServer>> call,
          Response<List<SynagogueFromServer>> response) {
        List<SynagogueFromServer> data = response.body();
        if (data != null) {
          List<Synagogue> synagogueList = transformer.transformSynagoguesFromServer(data);
          cache.putSynagogues(synagogueList);
          listener.onResponse(synagogueList);
        }
      }

      @Override
      public void onFailure(Call<List<SynagogueFromServer>> call, Throwable t) {
        Timber.w(t, "Couldn't get synagogues data, an exception occurred:");
        listener.onResponse(null);
      }
    };
    api.getSynagogues(maxHits, center, radius).enqueue(callback);
  }

  public void addSynagogue(Synagogue synagogue, final ResponseListener<String> listener) {
    Callback<IdFromServer> callback = new Callback<IdFromServer>() {
      @Override
      public void onResponse(Call<IdFromServer> call, Response<IdFromServer> response) {
        IdFromServer idData = response.body();
        if (idData != null) {
          String id = idData.getId();
          api.getSynagogue(id).enqueue(new Callback<SynagogueFromServer>() {
            @Override
            public void onResponse(Call<SynagogueFromServer> call,
                Response<SynagogueFromServer> response) {
              SynagogueFromServer data = response.body();
              if (data != null) {
                try {
                  Synagogue synagogue = transformer.transformFromServer(data);
                  cache.putSynagogue(synagogue);
                } catch (Exception e) {
                  Timber.w(e, "Couldn't parse synagogue data from server: " + data.toString());
                }

              }
            }

            @Override
            public void onFailure(Call<SynagogueFromServer> call, Throwable t) {

            }
          });
          listener.onResponse(id);
        }
      }

      @Override
      public void onFailure(Call<IdFromServer> call, Throwable t) {
        Timber.w(t, "Couldn't add synagogue, an exception occurred:");
      }
    };
    try {
      SynagogueToServer toServer = transformer.transformToServer(synagogue);
      api.addSynagogue(toServer).enqueue(callback);
    } catch (Exception e) {
      Timber.w(e, "Couldn't parse synagogue data for send to server: %s", synagogue.toString());
    }
  }

  public void updateSynagogue(Synagogue synagogue, final ResponseListener<Void> listener) {
    Callback<Void> callback = new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        listener.onResponse(response.body());
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        Timber.w(t, "Couldn't add synagogue, an exception occurred:");
      }
    };
    try {
      SynagogueToServer toServer = transformer.transformToServer(synagogue);
      api.updateSynagogue(synagogue.getId(), toServer).enqueue(callback);
    } catch (Exception e) {
      Timber.w(e, "Couldn't parse synagogue data for update server: " + synagogue.toString());
    }
  }
}
