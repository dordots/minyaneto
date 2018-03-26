package com.app.minyaneto_android.restApi;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import android.support.annotation.NonNull;
import com.app.minyaneto_android.data.IdFromServer;
import com.app.minyaneto_android.data.LatLngDoubleServer;
import com.app.minyaneto_android.data.MinyanScheduleFromServer;
import com.app.minyaneto_android.data.SynagogueFromServer;
import com.app.minyaneto_android.data.SynagogueToServer;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

public class SynagoguesRestAPITest {

  public static final String TEST_SYNAGOGUE_ID = "AWJjQfB-XhnPh-4nqCE2";
  public static final double LAT = 34.024;
  public static final double LON = 28.168;

  @Test
  public void getSynagogues() throws Exception {
    SynagoguesRestAPI api = getAPI();
    Call<List<SynagogueFromServer>> call = api
        .getSynagogues(4, String.format("%s,%s", LAT, LON), "3km");

    Response<List<SynagogueFromServer>> response = call.execute();

    List<SynagogueFromServer> synagogues = response.body();
    assertNotNull(synagogues);
    assertEquals(4, synagogues.size());
  }

  @Test
  public void getSynagogue() throws Exception {
    SynagoguesRestAPI api = getAPI();
    String synagogueName = "test_synagogue";
    Call<SynagogueFromServer> call = api.getSynagogue(TEST_SYNAGOGUE_ID);

    Response<SynagogueFromServer> response = call.execute();

    SynagogueFromServer result = response.body();
    assertNotNull(result);
    assertTrue(result.getName().contains(synagogueName));
  }

  @Test
  public void addSynagogue() throws Exception {
    SynagoguesRestAPI api = getAPI();
    String synagogueName = "test_synagogue";
    SynagogueToServer synagogue = generateSynagogueData(synagogueName);
    Call<IdFromServer> call = api.addSynagogue(synagogue);

    Response<IdFromServer> response = call.execute();

    IdFromServer idData = response.body();
    assertNotNull(idData);
    SynagogueFromServer result = api
        .getSynagogue(idData.getId())
        .execute()
        .body();
    assertNotNull(result);
    assertEquals(synagogueName, result.getName());
  }

  @Test
  public void updateSynagogue() throws Exception {
    SynagoguesRestAPI api = getAPI();
    String synagogueName = String.format("test_synagogue%d", new Random().nextInt(1000));
    SynagogueToServer synagogue = generateSynagogueData(synagogueName);
    Call<Void> call = api.updateSynagogue(TEST_SYNAGOGUE_ID, synagogue);

    Response<Void> response = call.execute();

    assertTrue(response.isSuccessful());
    SynagogueFromServer result = api
        .getSynagogue(TEST_SYNAGOGUE_ID)
        .execute()
        .body();
    assertNotNull(result);
    assertEquals(synagogueName, result.getName());
  }

  @NonNull
  private SynagogueToServer generateSynagogueData(String synagogueName) {
    SynagogueToServer synagogue = new SynagogueToServer(
        "no_where",
        false,
        "no_comments",
        new LatLngDoubleServer(LAT, LON),
        Collections.<MinyanScheduleFromServer>emptyList(),
        synagogueName,
        "test_nosach",
        false,
        false,
        false
    );
    return synagogue;
  }

  private SynagoguesRestAPI getAPI() {
    return RestAPIUtility.createSynagoguesRestAPI(true);
  }
}