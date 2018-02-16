package com.app.minyaneto_android.models.synagogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SynagogueArrayData {
    @SerializedName("synagogues")
    @Expose
    private ArrayList<SynagogueData> synagogues;

    public ArrayList<SynagogueData> getSynagogues() {
        return synagogues;
    }
}