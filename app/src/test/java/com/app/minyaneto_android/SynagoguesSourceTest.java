package com.app.minyaneto_android;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.data.SynagogueFromServer;
import com.app.minyaneto_android.models.domain.SynagogueCache;
import com.app.minyaneto_android.models.domain.SynagoguesSource;
import com.app.minyaneto_android.restApi.SynagoguesRestAPI;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class SynagoguesSourceTest {

  @Test
  public void retrievesSynagoguesFromRestAPI() throws Exception {
    SynagoguesRestAPI api = mock(SynagoguesRestAPI.class);
    DataTransformer transformer = mock(DataTransformer.class);
    SynagogueCache cache = mock(SynagogueCache.class);
    try {
      LatLng location = new LatLng(32, 34);
      new SynagoguesSource(api, transformer, cache).fetchSynagogues(20, location, 3, null);
    } catch (Exception ignored) {
    }
    verify(api).getSynagogues(20, "32,34", "3km");
  }

  @Test
  public void transformSynagoguesDataToSynagogues() throws Exception {
    SynagoguesRestAPI api = mock(SynagoguesRestAPI.class);
    DataTransformer transformer = mock(DataTransformer.class);
    SynagogueCache cache = mock(SynagogueCache.class);
    try {
      LatLng location = new LatLng(32, 34);
      new SynagoguesSource(api, transformer, cache).fetchSynagogues(20, location, 3, null);
    } catch (Exception ignored) {
    }
    verify(transformer)
        .transformSynagoguesFromServer(ArgumentMatchers.<SynagogueFromServer>anyList());
  }
}