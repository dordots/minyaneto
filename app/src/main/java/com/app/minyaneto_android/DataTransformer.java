package com.app.minyaneto_android;

import com.app.minyaneto_android.models.data.MinyanScheduleData;
import com.app.minyaneto_android.models.data.SynagogueData;
import com.app.minyaneto_android.models.minyan.MinyanScheduleModel;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.synagogue.Geo;
import com.app.minyaneto_android.models.synagogue.SynagogueModel;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.google.android.gms.maps.model.LatLng;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DataTransformer {
    public List<SynagogueModel> transformSynagoguesDataList(List<SynagogueData> dataList) {
        ArrayList<SynagogueModel> synagogues = new ArrayList<>();
        for (SynagogueData data : dataList) {
            synagogues.add(transform(data));
        }
        return synagogues;
    }

    public List<MinyanScheduleModel> transformMinyanDataList(List<MinyanScheduleData> dataList) {
        ArrayList<MinyanScheduleModel> minyans = new ArrayList<>();
        for (MinyanScheduleData data : dataList) {
            try {
                MinyanScheduleModel model = transform(data);
                minyans.add(model);
            } catch (Exception ignored) {
            }
        }
        return minyans;
    }

    private MinyanScheduleModel transform(MinyanScheduleData data) {
        return new MinyanScheduleModel(
                DayOfWeek.valueOf(data.getWeekDay().toUpperCase()),
                PrayType.getType(data.getPrayType()),
                transformStringToTime(data.getStringTime()));
    }

    private SynagogueModel transform(SynagogueData data) {
        return new SynagogueModel(
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
                transform(data.getGeo()));
    }

    private LatLng transform(Geo geo) {
        return new LatLng(Double.valueOf(geo.getLat()), Double.valueOf(geo.getLon()));
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
