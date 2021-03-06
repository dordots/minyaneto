package com.app.minyaneto_android.utilities;

import static com.app.minyaneto_android.models.time.RelativeTimeType.DAWN;
import static com.app.minyaneto_android.models.time.RelativeTimeType.STARS_OUT;
import static com.app.minyaneto_android.models.time.RelativeTimeType.SUNRISE;
import static com.app.minyaneto_android.models.time.RelativeTimeType.SUNSET;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

import android.content.Context;
import com.app.minyaneto_android.BuildConfig;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.domain.WeekDay;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.RelativeTimeType;

public class SynagogueUtils {

  public static String getTextFromEnum(Context context, PrayType prayType) {
    switch (prayType) {
      case MORNING:
        return context.getResources().getString(R.string.pray_type_morning);
      case AFTER_NOON:
        return context.getResources().getString(R.string.pray_type_after_noon);
      case EVENING:
        return context.getResources().getString(R.string.pray_type_evening);
    }
    return "";
  }


  public static String getTextFromEnum(Context context, WeekDay prayDayType) {
    switch (prayDayType.ordinal() + 1) {
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

  public static String getTextFromEnum(Context context, RelativeTimeType relativeTimeType) {
    switch (relativeTimeType) {
      case DAWN:
        return context.getResources().getString(R.string.relative_time_dawn);
      case SUNSET:
        return context.getResources().getString(R.string.relative_time_sunset);
      case SUNRISE:
        return context.getResources().getString(R.string.relative_time_sunrise);
      case STARS_OUT:
        return context.getResources().getString(R.string.relative_time_stars_out);
    }
    return "";
  }

  public static RelativeTimeType getRelativeTimeTypeFromText(Context context, String type) {
    if (type.equals(context.getResources().getString(R.string.relative_time_dawn))) {
      return DAWN;
    } else if (type.equals(context.getResources().getString(R.string.relative_time_sunset))) {
      return SUNSET;
    } else if (type.equals(context.getResources().getString(R.string.relative_time_sunrise))) {
      return SUNRISE;
    } else if (type.equals(context.getResources().getString(R.string.relative_time_stars_out))) {
      return STARS_OUT;
    } else {
      if (BuildConfig.DEBUG) {
        throw new RuntimeException("Couldn't recognise RelativeTimeType: " + type);
      } else {
        return DAWN;
      }
    }
  }
}
