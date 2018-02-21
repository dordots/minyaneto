package com.app.minyaneto_android.restApi;

import android.support.annotation.NonNull;

import com.app.minyaneto_android.data.LatLngStringData;
import com.app.minyaneto_android.data.MinyanScheduleData;
import com.app.minyaneto_android.data.SynagogueData;
import com.app.minyaneto_android.data.SynagogueIdData;
import com.app.minyaneto_android.data.SynagogueWrapperData;
import com.app.minyaneto_android.data.SynagoguesWrapperData;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SynagoguesRestAPITest {

    @Test
    public void getSynagogues() throws Exception {
        SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
        Call<SynagoguesWrapperData> call = api.getSynagoguesWrapperData(20, "31.786,35.186", "3km");

        Response<SynagoguesWrapperData> response = call.execute();

        SynagoguesWrapperData body = response.body();
        assertNotNull(body);
        List<SynagogueData> synagogues = body.getSynagogues();
        assertNotNull(synagogues);
        assertEquals(8, synagogues.size());
    }

    @Test
    public void getSynagogue() throws Exception {
        SynagoguesRestAPI api = RestAPIUtility.createSynagoguesRestAPI();
        String synagogueName = "חזון איש - רמת שלמה";
        Call<SynagogueWrapperData> call = api.getSynagogue("AV-W6p-eUpGeNoeyp2vV");

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
        SynagogueData synagogue = generateSynagogueData(synagogueName);
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

    @NonNull
    private SynagogueData generateSynagogueData(String synagogueName) {
        SynagogueData synagogue = new SynagogueData(
                "no_where",
                false,
                "no_comments",
                new LatLngStringData("34.024", "28.168"),
                "id_should_be_ignored",
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