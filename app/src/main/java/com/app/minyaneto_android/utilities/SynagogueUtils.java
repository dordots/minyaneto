package com.app.minyaneto_android.utilities;

import android.content.Context;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.synagogue.Geo;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.google.android.gms.maps.model.LatLng;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

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


    public static String getTextFromEnum(Context context, DayOfWeek prayDayType) {
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
                return context.getResources().getString(R.string.relative_time_sunrise);
            case SUNRISE:
                return context.getResources().getString(R.string.relative_time_sunrise);
            case STARS_OUT:
                return context.getResources().getString(R.string.relative_time_stars_out);
        }
        return "";
    }

    public static Synagogue toOldModel(SynagogueDomain synagogue) {
        Synagogue result = new Synagogue();
        result.setAddress(synagogue.getAddress());
        result.setClasses(synagogue.getClasses());
        result.setComments(synagogue.getComments());
        LatLng latLng = synagogue.getLocation();
        result.setGeo(new Geo(latLng.latitude, latLng.longitude));
        result.setId(synagogue.getId());
        result.setMinyans(toOldModel(synagogue.getMinyans()));
        result.setName(synagogue.getName());
        result.setNosach(synagogue.getNosach());
        result.setParking(synagogue.getParking());
        result.setSeferTora(synagogue.getSeferTora());
        result.setWheelchairAccessible(synagogue.getWheelchairAccessible());
        return result;
    }

    private static ArrayList<Minyan> toOldModel(List<MinyanScheduleDomain> minyans) {
        ArrayList<Minyan> list = new ArrayList<>();
        for (MinyanScheduleDomain minyan :
                minyans) {
            Minyan item = new Minyan();
            item.setDay(minyan.getDayOfWeek().name());
            item.setName(minyan.getPrayType().name());
            PrayTime time = minyan.getPrayTime();
            String strTime;
            if (time.isRelative()) {
                RelativeTime relativeTime = time.getRelativeTime();
                strTime = relativeTime.getRelativeTimeType().name() + ":" + relativeTime.getOffset();
            } else {
                ExactTime exactTime = time.getExactTime();
                strTime = exactTime.getHour() + ":" + exactTime.getMinutes();
            }
            item.setTime(strTime);
            list.add(item);
        }
        return list;
    }
}
