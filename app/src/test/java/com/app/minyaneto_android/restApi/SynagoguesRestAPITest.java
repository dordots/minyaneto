package com.app.minyaneto_android.restApi;

import android.support.annotation.NonNull;

import com.app.minyaneto_android.data.LatLngDoubleData;
import com.app.minyaneto_android.data.MinyanScheduleData;
import com.app.minyaneto_android.data.SynagogueData;
import com.app.minyaneto_android.data.SynagogueIdData;
import com.app.minyaneto_android.data.SynagogueToServerData;
import com.app.minyaneto_android.data.SynagogueWrapperData;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class SynagoguesRestAPITest {

    @Test
    public void getSynagogues() throws Exception {
        SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
        Call<List<SynagogueData>> call = api.getSynagogues(20, "31.786,35.186", "3km");

        Response<List<SynagogueData>> response = call.execute();

        List<SynagogueData> synagogues = response.body();
        assertNotNull(synagogues);
        assertEquals(9, synagogues.size());
    }

    @Test
    public void getSynagogue() throws Exception {
        SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
        String synagogueName = "test_synagogue";
        Call<SynagogueWrapperData> call = api.getSynagogue("AWG4o-siXhnPh-4nqCDV");

        Response<SynagogueWrapperData> response = call.execute();

        SynagogueWrapperData body = response.body();
        assertNotNull(body);
        SynagogueData result = body.getSynagogue();
        assertNotNull(result);
        assertEquals(synagogueName, result.getName());
    }

    @Test
    @Ignore
    public void addSynagogue() throws Exception {
        SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
        String synagogueName = "test_synagogue";
        SynagogueToServerData synagogue = generateSynagogueData(synagogueName);
        Call<SynagogueIdData> call = api.addSynagogue(synagogue);

        Response<SynagogueIdData> response = call.execute();

        SynagogueIdData idData = response.body();
        assertNotNull(idData);
        SynagogueWrapperData body = api
                .getSynagogue(idData.getId())
                .execute()
                .body();
        assertNotNull(body);
        SynagogueData result = body.getSynagogue();
        assertNotNull(result);
        assertEquals(synagogueName, result.getName());
    }

    @Test
    public void updateSynagogue() throws Exception {
        SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
        String synagogueName = String.format("test_synagogue%d", new Random().nextInt(1000));
        SynagogueToServerData synagogue = generateSynagogueData(synagogueName);
        Call<Void> call = api.updateSynagogue("AWG4o-siXhnPh-4nqCDV", synagogue);

        Response<Void> response = call.execute();

        assertTrue(response.isSuccessful());
        SynagogueWrapperData body = api
                .getSynagogue("AWG4o-siXhnPh-4nqCDV")
                .execute()
                .body();
        assertNotNull(body);
        SynagogueData result = body.getSynagogue();
        assertNotNull(result);
        assertEquals(synagogueName, result.getName());
    }

    @NonNull
    private SynagogueToServerData generateSynagogueData(String synagogueName) {
        SynagogueToServerData synagogue = new SynagogueToServerData(
                "no_where",
                false,
                "no_comments",
                new LatLngDoubleData(34.024, 28.168),
                Collections.<MinyanScheduleData>emptyList(),
                synagogueName,
                "test_nosach",
                false,
                false,
                false
        );
        return synagogue;
    }
}