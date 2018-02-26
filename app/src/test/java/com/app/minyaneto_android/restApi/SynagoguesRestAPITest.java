package com.app.minyaneto_android.restApi;

import com.app.minyaneto_android.models.synagogue.SynagogueArrayData;
import com.app.minyaneto_android.models.synagogue.SynagogueData;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SynagoguesRestAPITest {

    @Test
    public void sampleSynagoguesData() throws Exception {
        SynagogueArrayData body = RestAPIUtils
                .createSynagoguesRestAPI()
                .getSynagoguesArrayData("20", "31.786,35.186", "3km")
                .execute()
                .body();
        assertNotNull(body);
        ArrayList<SynagogueData> synagogues = body.getSynagogues();
        assertNotNull(synagogues);
        assertEquals(8, synagogues.size());
    }

}