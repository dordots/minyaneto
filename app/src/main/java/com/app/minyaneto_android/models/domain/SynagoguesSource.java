package com.app.minyaneto_android.models.domain;

import android.util.Log;

import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.data.SynagoguesWrapperData;
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

    public void fetchSynagogues(int maxHits, LatLng location, int radiusInKm) throws IOException {
        String center = location.latitude + "," + location.longitude;
        String radius = radiusInKm + "km";
        api.getSynagoguesWrapperData(maxHits, center, radius).enqueue(new Callback<SynagoguesWrapperData>() {
            @Override
            public void onResponse(Call<SynagoguesWrapperData> call, Response<SynagoguesWrapperData> response) {
                SynagoguesWrapperData data = response.body();
                if (data != null) {
                    List<SynagogueDomain> synagogueList = transformer.transformSynagoguesDataList(data.getSynagogues());
                    cache.putSynagogues(synagogueList);
                }
            }

            @Override
            public void onFailure(Call<SynagoguesWrapperData> call, Throwable t) {
                Log.w(SynagoguesSource.class.getSimpleName(),
                        "Couldn't get synagogues data, an exception occurred:\n" + t.getMessage());
            }
        });
    }

    public SynagogueDomain getSynagogue(String id) throws IOException {
        return cache.getSynagogue(id);
    }
}
