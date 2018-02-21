package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SynagogueWrapperData {
    @SerializedName("synagogue")
    @Expose
    private SynagogueData synagogue;

    public SynagogueData getSynagogue() {
        return synagogue;
    }
}
