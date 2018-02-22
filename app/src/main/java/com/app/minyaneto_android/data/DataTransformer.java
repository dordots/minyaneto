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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DataTransformer {
    public List<SynagogueDomain> transformSynagoguesDataList(List<SynagogueData> dataList) {
        List<SynagogueDomain> synagogues = new ArrayList<>();
        for (SynagogueData data : dataList) {
            synagogues.add(transform(data));
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

    private List<MinyanScheduleData> transformMinyanToDataList(List<MinyanScheduleDomain> minyans) {
        List<MinyanScheduleData> list = new ArrayList<>();
        for (MinyanScheduleDomain minyan : minyans) {
            list.add(transform(minyan));
        }
        return list;
    }

    public SynagogueDomain transform(SynagogueData data) {
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

    public SynagogueToServerData transform(SynagogueDomain synagogue) {
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

    private MinyanScheduleDomain transform(MinyanScheduleData data) {
        return new MinyanScheduleDomain(
                DayOfWeek.valueOf(data.getWeekDay().toUpperCase()),
                PrayType.getType(data.getPrayType()),
                transformStringToTime(data.getStringTime()));
    }

    private MinyanScheduleData transform(MinyanScheduleDomain minyan) {
        return new MinyanScheduleData(
                minyan.getDayOfWeek().toString(),
                minyan.getPrayType().toString(),
                transformTimeToString(minyan.getPrayTime()));
    }

    private LatLng transform(LatLngStringData data) {
        return new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLon()));
    }

    private PrayTime transformStringToTime(String stringTime) {
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

    private String transformTimeToString(PrayTime prayTime) {
        if (prayTime.isRelative()) {
            RelativeTime relativeTime = prayTime.getRelativeTime();
            return relativeTime.getRelativeTimeType() + "#" + relativeTime.getOffset();
        }
        ExactTime exactTime = prayTime.getExactTime();
        return exactTime.getHour() + ":" + exactTime.getMinutes();
    }
}
