package com.app.minyaneto_android.models.minyan;

import java.util.Date;
/**
 * Created by משה on 25/08/2017.
 */

public class RelativeTime implements Time {

    private RelativeTimeType relativeTimeType;
    private int offset;

    @Override
    public int getHour() {
        // Implementation depends on Kosher Java
        return 0;
    }

    @Override
    public int getMinutes() {
        // Implementation depends on Kosher Java
        return 0;
    }

    @Override
    public Date toDate(WeekDay weekDay) {
        // Implementation depends on Kosher Java
        return null;
    }
}
