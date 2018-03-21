package com.app.minyaneto_android.models.domain;

import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.data.SynagogueData;
import com.app.minyaneto_android.data.SynagogueIdData;
import com.app.minyaneto_android.data.SynagogueToServerData;
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
    Callback<List<SynagogueData>> callback = new Callback<List<SynagogueData>>() {
      @Override
      public void onResponse(Call<List<SynagogueData>> call,
          Response<List<SynagogueData>> response) {
        List<SynagogueData> data = response.body();
        if (data != null) {
          List<Synagogue> synagogueList = transformer.transformSynagoguesFromServer(data);
          cache.putSynagogues(synagogueList);
          listener.onResponse(synagogueList);
        }
      }

      @Override
      public void onFailure(Call<List<SynagogueData>> call, Throwable t) {
        Timber.w(t, "Couldn't get synagogues data, an exception occurred:");
        listener.onResponse(null);
      }
    };
    api.getSynagogues(maxHits, center, radius).enqueue(callback);
  }

  public void addSynagogue(Synagogue synagogue, final ResponseListener<String> listener) {
    Callback<SynagogueIdData> callback = new Callback<SynagogueIdData>() {
      @Override
      public void onResponse(Call<SynagogueIdData> call, Response<SynagogueIdData> response) {
        SynagogueIdData idData = response.body();
        if (idData != null) {
          String id = idData.getId();
          api.getSynagogue(id).enqueue(new Callback<SynagogueData>() {
            @Override
            public void onResponse(Call<SynagogueData> call, Response<SynagogueData> response) {
              SynagogueData data = response.body();
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
            public void onFailure(Call<SynagogueData> call, Throwable t) {

            }
          });
          listener.onResponse(id);
        }
      }

      @Override
      public void onFailure(Call<SynagogueIdData> call, Throwable t) {
        Timber.w(t, "Couldn't add synagogue, an exception occurred:");
      }
    };
    try {
      SynagogueToServerData toServer = transformer.transformToServer(synagogue);
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
      SynagogueToServerData toServer = transformer.transformToServer(synagogue);
      api.updateSynagogue(synagogue.getId(), toServer).enqueue(callback);
    } catch (Exception e) {
      Timber.w(e, "Couldn't parse synagogue data for update server: " + synagogue.toString());
    }
  }
}
