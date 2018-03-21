package com.app.minyaneto_android.models.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynagogueCache {

  private static SynagogueCache instance = new SynagogueCache();
  private Map<String, Synagogue> map;

  private SynagogueCache() {
    map = new HashMap<>();
  }

  public static SynagogueCache getInstance() {
    return instance;
  }

  public Synagogue getSynagogue(String id) {
    return map.get(id);
  }

  public void putSynagogue(Synagogue synagogue) {
    map.put(synagogue.getId(), synagogue);
  }

  public void putSynagogues(List<Synagogue> synagogues) {
    for (Synagogue synagogue : synagogues) {
      putSynagogue(synagogue);
    }
  }
}
