package com.app.minyaneto_android.models.domain;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.app.minyaneto_android.Config.DEFAULT_RADIUS_IN_KM;
import static com.app.minyaneto_android.Config.MAX_HITS_PER_REQUEST;

public class MinyanManager {

    private SynagoguesSource source;
    private SynagogueCacheRepository cache;

    public MinyanManager(SynagoguesSource source, SynagogueCacheRepository cache) {
        this.source = source;
        this.cache = cache;
    }

    public List<ConcreteMinyanDomain> getConcreteMinyanList(LatLng location) throws IOException {
        List<SynagogueDomain> synagogues = source.getSynagogues(MAX_HITS_PER_REQUEST, location, DEFAULT_RADIUS_IN_KM);
        cache.putSynagogues(synagogues);
        return Collections.emptyList();
    }

    public SynagogueDomain getSynagogue(String id) throws IOException {
        SynagogueDomain synagogue = cache.getSynagogue(id);
        if (synagogue != null)
            return synagogue;
        return source.getSynagogue(id);
    }
}
