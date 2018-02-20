package com.app.minyaneto_android;

import android.util.Log;

import com.app.minyaneto_android.models.data.SynagogueData;
import com.app.minyaneto_android.models.data.SynagoguesWrapperData;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.restApi.SynagoguesRestAPI;

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

    public List<SynagogueDomain> getSynagogues(int maxHits, double latitude, double longitude, int radiusInKm) throws IOException {
        String center = latitude + "," + longitude;
        String radius = radiusInKm + "km";
        List<SynagogueDomain> synagogueList;
        try {
            SynagoguesWrapperData body = api.getSynagoguesWrapperData(maxHits, center, radius)
                    .execute()
                    .body();
            synagogueList = transformer.transformSynagoguesDataList(body.getSynagogues());
        } catch (Exception e) {
            synagogueList = transformer.transformSynagoguesDataList(Collections.<SynagogueData>emptyList());
            Log.w(SynagoguesSource.class.getSimpleName(),
                    "Couldn't get synagogues data, an exception occurred:\n" + e.getMessage());
        }
        return synagogueList;
    }
}
