package com.app.minyaneto_android.data;

import android.util.Log;

import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataTransformer {
    public List<SynagogueDomain> transformSynagoguesDataList(List<SynagogueData> dataList) {
        List<SynagogueDomain> synagogues = new ArrayList<>();
        for (SynagogueData data : dataList) {
            try {
                synagogues.add(transform(data));
            } catch (Exception e) {
                Log.w(DataTransformer.class.getSimpleName(),
                        "Couldn't parse synagogues data from server: " + data.toString());
            }
        }
        return synagogues;
    }

    public List<MinyanScheduleDomain> transformMinyanDataList(List<MinyanScheduleData> dataList) {
        ArrayList<MinyanScheduleDomain> minyans = new ArrayList<>();
        for (MinyanScheduleData data : dataList) {
            try {
                MinyanScheduleDomain model = transform(data);
                minyans.add(model);
            } catch (Exception e) {
                Log.w(DataTransformer.class.getSimpleName(),
                        "Couldn't parse minyan data from server: " + data.toString());
            }
        }
        return minyans;
    }

    private List<MinyanScheduleData> transformMinyanToDataList(List<MinyanScheduleDomain> minyans) throws Exception {
        List<MinyanScheduleData> list = new ArrayList<>();
        for (MinyanScheduleDomain minyan : minyans) {
            list.add(transform(minyan));
        }
        return list;
    }

    public SynagogueDomain transform(SynagogueData data) throws Exception {
        return new SynagogueDomain(
                data.getAddress(),
                data.getClasses(),
                data.getComments(),
                data.getId(),
                transformMinyanDataList(data.getMinyans()),
                data.getName(),
                data.getNosach(),
                data.getParking(),
                data.getSeferTora(),
                data.getWheelchairAccessible(),
                transform(data.getLatLngStringData()));
    }

    public SynagogueToServerData transform(SynagogueDomain synagogue) throws Exception {
        LatLng latLng = synagogue.getLocation();
        return new SynagogueToServerData(
                synagogue.getAddress(),
                synagogue.getClasses(),
                synagogue.getComments(),
                new LatLngDoubleData(latLng.latitude, latLng.longitude),
                transformMinyanToDataList(synagogue.getMinyans()),
                synagogue.getName(),
                synagogue.getNosach(),
                synagogue.getParking(),
                synagogue.getSeferTora(),
                synagogue.getWheelchairAccessible());
    }

    private MinyanScheduleDomain transform(MinyanScheduleData data) throws Exception {
        return new MinyanScheduleDomain(
                transformStringToWeekDay(data.getWeekDay().toUpperCase()),
                transformStringToPrayType(data.getPrayType()),
                transformStringToTime(data.getStringTime()));
    }

    private MinyanScheduleData transform(MinyanScheduleDomain minyan) throws Exception {
        return new MinyanScheduleData(
                transformWeekDayToString(minyan.getWeekDay()),
                transformPrayTypeToString(minyan.getPrayType()),
                transformTimeToString(minyan.getPrayTime()));
    }

    public WeekDay transformStringToWeekDay(String data) throws Exception {
        switch (data) {
            case "SUNDAY":
            case "sunday":
            case "ראשון":
                return WeekDay.SUNDAY;
            case "MONDAY":
            case "monday":
            case "שני":
                return WeekDay.MONDAY;
            case "TUESDAY":
            case "tuesday":
            case "שלישי":
                return WeekDay.TUESDAY;
            case "WEDNESDAY":
            case "wednesday":
            case "רביעי":
                return WeekDay.WEDNESDAY;
            case "THURSDAY":
            case "thursday":
            case "חמישי":
                return WeekDay.THURSDAY;
            case "FRIDAY":
            case "friday":
            case "שישי":
                return WeekDay.FRIDAY;
            case "SATURDAY":
            case "saturday":
            case "שבת":
                return WeekDay.SATURDAY;

        }
        throw new Exception(data + " not found in WeekDay");
    }

    private String transformWeekDayToString(WeekDay data) throws Exception {
        switch (data) {
            case SUNDAY:
                return "SUNDAY".toLowerCase();
            case MONDAY:
                return "MONDAY".toLowerCase();
            case TUESDAY:
                return "TUESDAY".toLowerCase();
            case WEDNESDAY:
                return "WEDNESDAY".toLowerCase();
            case THURSDAY:
                return "THURSDAY".toLowerCase();
            case FRIDAY:
                return "FRIDAY".toLowerCase();
            case SATURDAY:
                return "SATURDAY".toLowerCase();
        }
        throw new Exception(data.toString() + " not found.");
    }

    private PrayType transformStringToPrayType(String data) throws Exception {
        switch (data) {
            case "shachrit":
            case "shacharit":
            case "shaharit":
            case "שחרית":
            case "MORNING":
                return PrayType.MORNING;
            case "mincha":
            case "minha":
            case "מנחה":
            case "AFTER_NOON":
                return PrayType.AFTER_NOON;
            case "maariv":
            case "arvit":
            case "ערבית":
            case "EVENING":
                return PrayType.EVENING;
        }
        throw new Exception(data.toString() + " not found in PrayType.");
    }

    private String transformPrayTypeToString(PrayType data) throws Exception {
        switch (data) {
            case MORNING:
                return "shachrit";
            case AFTER_NOON:
                return "mincha";
            case EVENING:
                return "maariv";
        }
        throw new Exception(data.toString() + " not found.");
    }

    private LatLng transform(LatLngStringData data) throws Exception {
        return new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLon()));
    }

    private PrayTime transformStringToTime(String stringTime) throws Exception {
        String[] parts;
        if (stringTime.contains(":")) {
            parts = stringTime.split(":");
            return new PrayTime(new ExactTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        } else if (stringTime.contains("#")) {
            parts = stringTime.split("#");
            return new PrayTime(new RelativeTime(RelativeTimeType.valueOf(parts[0]), Integer.parseInt(parts[1])));
        }
        throw new IllegalArgumentException("Couldn't transform time from data: " + stringTime);
    }

    private String transformTimeToString(PrayTime prayTime) throws Exception {
        if (prayTime.isRelative()) {
            RelativeTime relativeTime = prayTime.getRelativeTime();
            return relativeTime.getRelativeTimeType() + "#" + relativeTime.getOffset();
        }
        ExactTime exactTime = prayTime.getExactTime();
        return exactTime.getHour() + ":" + exactTime.getMinutes();
    }
}
