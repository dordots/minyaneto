package com.app.minyaneto_android.models.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynagogueCache {

  private static SynagogueCache instance = new SynagogueCache();
  private Map<String, SynagogueDomain> map;

  private SynagogueCache() {
    map = new HashMap<>();
  }

  public static SynagogueCache getInstance() {
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
