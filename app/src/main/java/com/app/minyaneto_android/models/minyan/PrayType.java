package com.app.minyaneto_android.models.minyan;

/**
 * Created by משה on 25/08/2017.
 */

public enum PrayType {
    MORNING,
    AFTER_NOON,
    EVENING;

    String uiText;
    PrayType(){}
    PrayType(String uiText){
        this.uiText = uiText;
    }
    String getUiText(){
        return uiText;
    }
    void setUiText(String uiText){
        this.uiText = uiText;
    }
}
