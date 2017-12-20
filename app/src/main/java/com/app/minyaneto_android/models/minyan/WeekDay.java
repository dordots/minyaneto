package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;

/**
 * Created by משה on 25/08/2017.
 */

public enum WeekDay {
    SUNDAY(R.string.pray_day_type_s),
    MONDAY(R.string.pray_day_type_m),
    TUESDAY(R.string.pray_day_type_tu),
    WEDNESDAY(R.string.pray_day_type_w),
    THURSDAY(R.string.pray_day_type_th),
    FRIDAY(R.string.pray_day_type_fr),
    SATURDAY(R.string.pray_day_type_sa);

    private int stringId;

    WeekDay(int stringId){
        this.stringId = stringId;
    }

    // TODO: CR david
   /*@Override
    public String toString() {
        return MainActivity.resources.getString(stringId);
    }*/
}
