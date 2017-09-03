package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.acivities.MainActivity;

/**
 * Created by משה on 25/08/2017.
 */

public enum PrayType {
    MORNING(R.string.pray_type_morning),
    AFTER_NOON(R.string.pray_type_after_noon),
    EVENING(R.string.pray_type_evening);

    private int stringId;

    PrayType(int stringId){
        this.stringId = stringId;
    }

    @Override
    public String toString() {
        return MainActivity.resources.getString(stringId);
    }
}
