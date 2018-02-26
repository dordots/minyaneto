package com.app.minyaneto_android.utilities;

import android.content.Context;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.RelativeTimeType;

/**
 * Created by admin on 22/02/2018.
 */

public class SynagogeUtils {

    public static String getTextFromEnum(Context context, PrayType prayType){
        switch (prayType){
            case MORNING:
                return context.getResources().getString(R.string.pray_type_morning);
            case AFTER_NOON:
                return context.getResources().getString(R.string.pray_type_after_noon);
            case EVENING:
                return context.getResources().getString(R.string.pray_type_evening);
        }
        return "";
    }


    public static String getTextFromEnum(Context context, PrayDayType prayDayType){
        switch (prayDayType){
            case SUNDAY:
                return context.getResources().getString(R.string.pray_day_type_s);
            case MONDAY:
                return context.getResources().getString(R.string.pray_day_type_m);
            case TUESDAY:
                return context.getResources().getString(R.string.pray_day_type_tu);
            case WEDNESDAY:
                return context.getResources().getString(R.string.pray_day_type_w);
            case THURSDAY:
                return context.getResources().getString(R.string.pray_day_type_th);
            case FRIDAY:
                return context.getResources().getString(R.string.pray_day_type_fr);
            case SATURDAY:
                return context.getResources().getString(R.string.pray_day_type_sa);
        }
        return "";
    }

    public static String getTextFromEnum(Context context, RelativeTimeType relativeTimeType){
        switch (relativeTimeType){
            case DAWN:
                return context.getResources().getString(R.string.relative_time_dawn);
            case SUNSET:
                return context.getResources().getString(R.string.relative_time_sunrise);
            case SUNRISE:
                return context.getResources().getString(R.string.relative_time_sunrise);
            case STARS_OUT:
                return context.getResources().getString(R.string.relative_time_stars_out);
        }
        return "";
    }
}
