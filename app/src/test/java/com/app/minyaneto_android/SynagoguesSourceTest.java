package com.app.minyaneto_android;

import com.app.minyaneto_android.models.data.SynagogueData;
import com.app.minyaneto_android.restApi.SynagoguesRestAPI;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SynagoguesSourceTest {
    @Test
    public void retrievesSynagoguesFromRestAPI() throws Exception {
        SynagoguesRestAPI api = mock(SynagoguesRestAPI.class);
        DataTransformer transformer = mock(DataTransformer.class);
        try {
            new SynagoguesSource(api, transformer).getSynagogues(20, 32, 34, 3);
        } catch (Exception ignored) {
        }
        verify(api).getSynagoguesWrapperData("20", "32,34", "3km");
    }

    @Test
    public void transformSynagoguesDataToSynagogues() throws Exception {
        SynagoguesRestAPI api = mock(SynagoguesRestAPI.class);
        DataTransformer transformer = mock(DataTransformer.class);
        try {
            new SynagoguesSource(api, transformer).getSynagogues(20, 32, 34, 3);
        } catch (Exception ignored) {
        }
        verify(transformer).transformSynagoguesDataList(ArgumentMatchers.<SynagogueData>anyList());
    }
}