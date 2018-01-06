package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;

/**
 * Created by משה on 25/08/2017.
 */

public enum PrayType {

    // TODO: CR david
    MORNING(R.string.pray_type_morning),
    AFTER_NOON(R.string.pray_type_after_noon),
    EVENING(R.string.pray_type_evening);

    PrayType(int stringId) {
    }

    public static PrayType getType(String string) throws Exception {
        switch (string) {
            case "shachrit":
            case "shacharit":
            case "shaharit":
            case "שחרית":
                return PrayType.MORNING;
            case "mincha":
            case "minha":
            case "מנחה":
                return PrayType.AFTER_NOON;
            case "maariv":
            case "arvit":
            case "ערבית":
                return PrayType.EVENING;
        }
        throw new Exception(string);
    }


}
