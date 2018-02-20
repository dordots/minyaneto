package com.app.minyaneto_android.models.domain;

import java.util.List;
import java.util.Map;

public class SynagogueCacheRepository {
    private static SynagogueCacheRepository instance = new SynagogueCacheRepository();
    private Map<String, SynagogueDomain> map;

    private SynagogueCacheRepository() {
    }

    public static SynagogueCacheRepository getInstance() {
        return instance;
    }

    public SynagogueDomain getSynagogue(String id) {
        return map.get(id);
    }

    public void putSynagogue(SynagogueDomain synagogue) {
        map.put(synagogue.getId(), synagogue);
    }

    public void putSynagogues(List<SynagogueDomain> synagogues) {
        for (SynagogueDomain synagogue : synagogues) {
            putSynagogue(synagogue);
        }
    }
}
