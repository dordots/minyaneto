package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.acivities.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 15/08/2017.
 */

public class Minyan {
    private String id;
    private PrayType prayType;
    private Time time;
    private ArrayList<PrayDayType> prayDayTypeArray;

    public Minyan() {}

    public Minyan(PrayType prayType, Time time, PrayDayType ... prayDayType) {
        this.prayType = prayType;
        this.time = time;
        this.prayDayTypeArray = new ArrayList<>();
        setPrayDayTypeArray(prayDayType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PrayType getPrayType() {
        return prayType;
    }

    public void setPrayType(PrayType prayType) {
        this.prayType = prayType;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public ArrayList<PrayDayType> getPrayDayTypeArray() {
        return prayDayTypeArray;
    }

    public void setPrayDayTypeArray(ArrayList<PrayDayType> prayDayTypeArray) {
        this.prayDayTypeArray = prayDayTypeArray;
    }

    public void setPrayDayTypeArray(PrayDayType ... prayDayTypeArray) {
        if (prayDayTypeArray.length > 0)
            this.prayDayTypeArray = new ArrayList<>(Arrays.asList(prayDayTypeArray));
    }

    @Override
    public String toString() {
        if (!isValid())
            return "Minyan is not valid!";

        String nounPray = MainActivity.resources.getString(R.string.nouns_pray);
        String nounInDays =
                MainActivity.resources.getQuantityString(R.plurals.nouns_in_days, prayDayTypeArray.size());
        StringBuilder days = new StringBuilder();
        for(PrayDayType pdt : prayDayTypeArray){
            days.append(pdt + ", ");
        }
        days.delete(days.length()-2, days.length());

        return String.format("%s %s %s %s %s", nounPray, prayType.toString(),
                nounInDays, days.toString(), time.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Minyan)) return false;

        Minyan minyan = (Minyan) o;

        if (id != null ? !id.equals(minyan.id) : minyan.id != null) return false;
        if (prayType != minyan.prayType) return false;
        if (!time.equals(minyan.time)) return false;
        return prayDayTypeArray.equals(minyan.prayDayTypeArray);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + prayType.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + prayDayTypeArray.hashCode();
        return result;
    }

    public boolean isValid() {
        return true;
    }
}
