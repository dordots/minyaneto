package com.app.minyaneto_android.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SynagoguesWrapperData {

    @SerializedName("synagogues")
    @Expose
    private List<SynagogueData> synagogues;

    public List<SynagogueData> getSynagogues() {
        return synagogues;
    }
}