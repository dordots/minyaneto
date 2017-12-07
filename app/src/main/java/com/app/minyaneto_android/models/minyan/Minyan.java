package com.app.minyaneto_android.models.minyan;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 15/08/2017.
 */

public class Minyan {
    private String id;
    private PrayType prayType;
    private Time time;
    private PrayDayType prayDayType;
    private Date lastUpdate;

    public Minyan() {
    }

    public Minyan(PrayType prayType, Time time, PrayDayType prayDayType) {
        this.prayType = prayType;
        this.time = time;
        this.prayDayType = prayDayType;
    }

    public Minyan(JSONObject object) throws JSONException, ParseException ,Exception{

            this.prayType=PrayType.getType(object.getString("name"));
            this.prayDayType=PrayDayType.getType(object.getString("day"));
            String m_time=object.getString("time");
            if(m_time.split(":").length>1) {
                SimpleDateFormat format =  new SimpleDateFormat("HH:mm");

                this.time = new ExactTime(format.parse(m_time).getHours(),format.parse(m_time).getMinutes());
            }
            else
            {
                this.time=new RelativeTime(RelativeTimeType.valueOf(m_time.split(" ")[0]), Integer.parseInt(m_time.split(" ")[1], 0));
            }
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

    public PrayDayType getPrayDayType() {
        return prayDayType;
    }

    public void setPrayDayType(PrayDayType prayDayType) {
        this.prayDayType = prayDayType;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        if (!isValid())
            return "Minyan is not valid!";

        String nounPray = MainActivity.resources.getString(R.string.nouns_pray);
        String nounInDays =
                MainActivity.resources.getQuantityString(R.plurals.nouns_in_days, 1);
//        StringBuilder days = new StringBuilder();
//        for (PrayDayType pdt : prayDayTypeArray) {
//            days.append(pdt + ", ");
//        }
//        days.delete(days.length() - 2, days.length());

        return String.format("%s %s %s %s %s", nounPray, prayType.toString(),
                nounInDays, prayDayType.toString(), time.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Minyan)) return false;

        Minyan minyan = (Minyan) o;

        if (id != null ? !id.equals(minyan.id) : minyan.id != null) return false;
        if (prayType != minyan.prayType) return false;
        if (!time.equals(minyan.time)) return false;
        return prayDayType.equals(minyan.prayDayType);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + prayType.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + prayDayType.hashCode();
        return result;
    }

    public boolean isValid() {
        return true;
    }
}
