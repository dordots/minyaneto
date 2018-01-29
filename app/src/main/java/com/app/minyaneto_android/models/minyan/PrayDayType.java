package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;

public enum PrayDayType {

    // TODO: CR david

    SUNDAY(R.string.pray_day_type_s),
    MONDAY(R.string.pray_day_type_m),
    TUESDAY(R.string.pray_day_type_tu),
    WEDNESDAY(R.string.pray_day_type_w),
    THURSDAY(R.string.pray_day_type_th),
    FRIDAY(R.string.pray_day_type_fr),
    SATURDAY(R.string.pray_day_type_sa),
    FAST(R.string.pray_day_type_fa),
    ROSH_CHODESH(R.string.pray_day_type_rh);

    private int stringId;

    PrayDayType(int stringId) {
        this.stringId = stringId;
    }

    public static PrayDayType getType(String str) throws Exception {
        switch (str) {
            case "sunday":
            case "ראשון":
                return SUNDAY;
            case "monday":
            case "שני":
                return MONDAY;
            case "tuesday":
            case "שלישי":
                return TUESDAY;
            case "wednesday":
            case "רביעי":
                return WEDNESDAY;
            case "thursday":
            case "חמישי":
                return THURSDAY;
            case "friday":
            case "שישי":
                return FRIDAY;
            case "saturday":
            case "שבת":
                return SATURDAY;
            default:
                return THURSDAY; //TODO return the right value for spacial cases like -rosh-hodesh..
        }
    }


}
