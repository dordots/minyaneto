package com.app.minyaneto_android.models.domain;

import android.util.Log;

import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.data.SynagogueData;
import com.app.minyaneto_android.data.SynagoguesWrapperData;
import com.app.minyaneto_android.restApi.SynagoguesRestAPI;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SynagoguesSource {
    private SynagoguesRestAPI api;
    private DataTransformer transformer;

    public SynagoguesSource(SynagoguesRestAPI api, DataTransformer transformer) {
        this.api = api;
        this.transformer = transformer;
    }

    public List<SynagogueDomain> getSynagogues(int maxHits, LatLng location, int radiusInKm) throws IOException {
        String center = location.latitude + "," + location.longitude;
        String radius = radiusInKm + "km";
        List<SynagogueDomain> synagogueList;
        try {
            SynagoguesWrapperData body = api.getSynagoguesWrapperData(maxHits, center, radius)
                    .execute()
                    .body();
            //noinspection ConstantConditions
            synagogueList = transformer.transformSynagoguesDataList(body.getSynagogues());
        } catch (Exception e) {
            synagogueList = transformer.transformSynagoguesDataList(Collections.<SynagogueData>emptyList());
            Log.w(SynagoguesSource.class.getSimpleName(),
                    "Couldn't get synagogues data, an exception occurred:\n" + e.getMessage());
        }
        return synagogueList;
    }

    public SynagogueDomain getSynagogue(String id) throws IOException {
        return transformer.transform(api.getSynagogue(id).execute().body());
    }
}
