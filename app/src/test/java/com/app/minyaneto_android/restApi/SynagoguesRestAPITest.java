package com.app.minyaneto_android.restApi;

import com.app.minyaneto_android.models.data.SynagogueData;
import com.app.minyaneto_android.models.data.SynagoguesWrapperData;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class SynagoguesRestAPITest {

    @Test
    public void getSynagogues() throws Exception {
        SynagoguesWrapperData body = RestAPIUtils
                .createSynagoguesRestAPI()
                .getSynagoguesWrapperData(20, "31.786,35.186", "3km")
                .execute()
                .body();
        assertNotNull(body);
        List<SynagogueData> synagogues = body.getSynagogues();
        assertNotNull(synagogues);
        assertEquals(8, synagogues.size());
    }

    @Test
    @Ignore
    public void addSynagogue() throws Exception {
        SynagoguesRestAPI api = RestAPIUtils
                .createSynagoguesRestAPI();
        SynagogueData synagogue = new SynagogueData();
        String synagogueName = "test_synagogue";
        synagogue.setName(synagogueName);

        api.addSynagogue(synagogue);

        SynagoguesWrapperData body = api
                .getSynagoguesWrapperData(1, "34.024,28.168", "1km")
                .execute()
                .body();
        assertNotNull(body);
        List<SynagogueData> synagogues = body.getSynagogues();
        assertNotNull(synagogues);
        assertFalse(synagogues.isEmpty());
        assertEquals(synagogueName, synagogues.get(0).getName());
    }
}