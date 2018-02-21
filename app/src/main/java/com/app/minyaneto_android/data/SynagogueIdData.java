package com.app.minyaneto_android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SynagogueIdData {
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }
}
