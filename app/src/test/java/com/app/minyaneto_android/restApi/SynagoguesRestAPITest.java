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
import org.junit.Ignore;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

public class SynagoguesRestAPITest {

  @Test
  public void getSynagogues() throws Exception {
    SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
    Call<List<SynagogueFromServer>> call = api.getSynagogues(20, "31.786,35.186", "3km");

    Response<List<SynagogueFromServer>> response = call.execute();

    List<SynagogueFromServer> synagogues = response.body();
    assertNotNull(synagogues);
    assertEquals(9, synagogues.size());
  }

  @Test
  public void getSynagogue() throws Exception {
    SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
    String synagogueName = "test_synagogue";
    Call<SynagogueFromServer> call = api.getSynagogue("AWG4o-siXhnPh-4nqCDV");

    Response<SynagogueFromServer> response = call.execute();

    SynagogueFromServer result = response.body();
    assertNotNull(result);
    assertTrue(result.getName().contains(synagogueName));
  }

  @Test
  @Ignore
  public void addSynagogue() throws Exception {
    SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
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
    SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
    String synagogueName = String.format("test_synagogue%d", new Random().nextInt(1000));
    SynagogueToServer synagogue = generateSynagogueData(synagogueName);
    Call<Void> call = api.updateSynagogue("AWG4o-siXhnPh-4nqCDV", synagogue);

    Response<Void> response = call.execute();

    assertTrue(response.isSuccessful());
    SynagogueFromServer result = api
        .getSynagogue("AWG4o-siXhnPh-4nqCDV")
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
        new LatLngDoubleServer(34.024, 28.168),
        Collections.<MinyanScheduleFromServer>emptyList(),
        synagogueName,
        "test_nosach",
        false,
        false,
        false
    );
    return synagogue;
  }
}