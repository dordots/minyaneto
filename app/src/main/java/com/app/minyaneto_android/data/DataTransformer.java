package com.app.minyaneto_android.data;

import com.app.minyaneto_android.models.domain.MinyanSchedule;
import com.app.minyaneto_android.models.domain.Synagogue;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class DataTransformer {

  public List<Synagogue> transformSynagoguesFromServer(List<SynagogueFromServer> dataList) {
    List<Synagogue> synagogues = new ArrayList<>();
    for (SynagogueFromServer data : dataList) {
      try {
        synagogues.add(transformFromServer(data));
      } catch (Exception e) {
        Timber.w("Couldn't parse synagogue data from server: %s", data);
      }
    }
    return synagogues;
  }

  public List<MinyanSchedule> transformMinyansFromServer(List<MinyanScheduleFromServer> dataList) {
    ArrayList<MinyanSchedule> minyans = new ArrayList<>();
    for (MinyanScheduleFromServer data : dataList) {
      try {
        MinyanSchedule model = transformFromServer(data);
        minyans.add(model);
      } catch (Exception e) {
        Timber.w("Couldn't parse minyan data from server: %s", data);
      }
    }
    return minyans;
  }

  public Synagogue transformFromServer(SynagogueFromServer data) throws Exception {
    return new Synagogue(
        data.getAddress(),
        data.getClasses(),
        data.getComments(),
        data.getId(),
        transformMinyansFromServer(data.getMinyans()),
        data.getName(),
        data.getNosach(),
        data.getParking(),
        data.getSeferTora(),
        data.getWheelchairAccessible(),
        transformFromServer(data.getLatLngStringServer()));
  }

  public SynagogueToServer transformToServer(Synagogue synagogue) throws Exception {
    LatLng latLng = synagogue.getLocation();
    return new SynagogueToServer(
        synagogue.getAddress(),
        synagogue.getClasses(),
        synagogue.getComments(),
        new LatLngDoubleServer(latLng.latitude, latLng.longitude),
        transformMinyansToServer(synagogue.getMinyans()),
        synagogue.getName(),
        synagogue.getNosach(),
        synagogue.getParking(),
        synagogue.getSeferTora(),
        synagogue.getWheelchairAccessible());
  }

  private List<MinyanScheduleFromServer> transformMinyansToServer(List<MinyanSchedule> minyans)
      throws Exception {
    List<MinyanScheduleFromServer> list = new ArrayList<>();
    for (MinyanSchedule minyan : minyans) {
      list.add(transformToServer(minyan));
    }
    return list;
  }

  private MinyanSchedule transformFromServer(MinyanScheduleFromServer data) throws Exception {
    return new MinyanSchedule(
        transformStringToWeekDay(data.getWeekDay().toUpperCase()),
        transformStringToPrayType(data.getPrayType()),
        transformStringToTime(data.getStringTime()));
  }

  private MinyanScheduleFromServer transformToServer(MinyanSchedule minyan) throws Exception {
    return new MinyanScheduleFromServer(
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
    throw new Exception(data + " not found in PrayType.");
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

  private LatLng transformFromServer(LatLngStringServer data) throws Exception {
    return new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLon()));
  }

  private PrayTime transformStringToTime(String stringTime) throws Exception {
    String[] parts;
    if (stringTime.contains(":")) {
      parts = stringTime.split(":");
      return new PrayTime(new ExactTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
    } else if (stringTime.contains("#")) {
      parts = stringTime.split("#");
      return new PrayTime(
          new RelativeTime(RelativeTimeType.valueOf(parts[0]), Integer.parseInt(parts[1])));
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
