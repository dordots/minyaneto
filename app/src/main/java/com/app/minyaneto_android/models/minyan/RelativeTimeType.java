package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;

/**
 * Created by משה on 25/08/2017.
 */

public enum RelativeTimeType {
    DAWN(R.string.relative_time_dawn),
    SUNRISE(R.string.relative_time_sunrise),
    SUNSET(R.string.relative_time_sunset),
    STARS_OUT(R.string.relative_time_stars_out);

    private int stringId;

    RelativeTimeType(int stringId){
        this.stringId = stringId;
    }

    @Override
    public String toString() {
        return MainActivity.resources.getString(stringId);
    }
}
