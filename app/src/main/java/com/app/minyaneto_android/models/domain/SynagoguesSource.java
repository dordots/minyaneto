package com.app.minyaneto_android.models.domain;

import android.util.Log;

import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.data.SynagogueIdData;
import com.app.minyaneto_android.data.SynagogueToServerData;
import com.app.minyaneto_android.data.SynagoguesWrapperData;
import com.app.minyaneto_android.restApi.ResponseListener;
import com.app.minyaneto_android.restApi.SynagoguesRestAPI;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SynagoguesSource {
    private SynagoguesRestAPI api;
    private DataTransformer transformer;
    private SynagogueCache cache;

    public SynagoguesSource(SynagoguesRestAPI api, DataTransformer transformer, SynagogueCache cache) {
        this.api = api;
        this.transformer = transformer;
        this.cache = cache;
    }

    public void fetchSynagogues(int maxHits, LatLng location, int radiusInKm, final ResponseListener<List<SynagogueDomain>> listener) throws IOException {
        String center = location.latitude + "," + location.longitude;
        String radius = radiusInKm + "km";
        Callback<SynagoguesWrapperData> callback = new Callback<SynagoguesWrapperData>() {
            @Override
            public void onResponse(Call<SynagoguesWrapperData> call, Response<SynagoguesWrapperData> response) {
                SynagoguesWrapperData data = response.body();
                if (data != null) {
                    List<SynagogueDomain> synagogueList = transformer.transformSynagoguesDataList(data.getSynagogues());
                    cache.putSynagogues(synagogueList);
                    listener.onResponse(synagogueList);
                }
            }

            @Override
            public void onFailure(Call<SynagoguesWrapperData> call, Throwable t) {
                Log.w(SynagoguesSource.class.getSimpleName(),
                        "Couldn't get synagogues data, an exception occurred:\n" + t.getMessage());
            }
        };
        api.getSynagoguesWrapperData(maxHits, center, radius).enqueue(callback);
    }

    public void addSynagogue(SynagogueDomain synagogue, final ResponseListener<String> listener) {
        Callback<SynagogueIdData> callback = new Callback<SynagogueIdData>() {
            @Override
            public void onResponse(Call<SynagogueIdData> call, Response<SynagogueIdData> response) {
                SynagogueIdData idData = response.body();
                if (idData != null) {
                    listener.onResponse(idData.getId());
                }
            }

            @Override
            public void onFailure(Call<SynagogueIdData> call, Throwable t) {
                Log.w(SynagoguesSource.class.getSimpleName(),
                        "Couldn't add synagogue, an exception occurred:\n" + t.getMessage());
            }
        };
        SynagogueToServerData toServer = transformer.transform(synagogue);
        api.addSynagogue(toServer).enqueue(callback);
    }

    public void updateSynagogue(SynagogueDomain synagogue) {
        Callback<Void> callback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(SynagoguesSource.class.getSimpleName(),
                        "Couldn't add synagogue, an exception occurred:\n" + t.getMessage());
            }
        };
        SynagogueToServerData toServer = transformer.transform(synagogue);
        api.updateSynagogue(synagogue.getId(), toServer).enqueue(callback);
    }
}
