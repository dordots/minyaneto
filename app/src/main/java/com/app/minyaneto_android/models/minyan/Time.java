package com.app.minyaneto_android.models.minyan;

import java.util.Date;

/**
 * Created by משה on 25/08/2017.
 */

public interface Time {
    int getHour();
    int getMinutes();
    Date toDate(WeekDay weekDay);
}
