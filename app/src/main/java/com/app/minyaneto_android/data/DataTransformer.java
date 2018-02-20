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
        ArrayList<SynagogueDomain> synagogues = new ArrayList<>();
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
                transform(data.getLatLngData()));
    }

    private MinyanScheduleDomain transform(MinyanScheduleData data) {
        return new MinyanScheduleDomain(
                DayOfWeek.valueOf(data.getWeekDay().toUpperCase()),
                PrayType.getType(data.getPrayType()),
                transformStringToTime(data.getStringTime()));
    }

    private LatLng transform(LatLngData data) {
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
}
