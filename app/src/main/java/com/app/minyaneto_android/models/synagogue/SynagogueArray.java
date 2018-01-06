package com.app.minyaneto_android.models.synagogue;


import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SynagogueArray {

    @SerializedName("synagogues")
    @Expose
    private ArrayList<Synagogue> synagogues = null;

    public ArrayList<Synagogue> getSynagogues() {
        return synagogues;
    }

    public void setSynagogues(ArrayList<Synagogue> synagogues) {
        this.synagogues = synagogues;
    }
}