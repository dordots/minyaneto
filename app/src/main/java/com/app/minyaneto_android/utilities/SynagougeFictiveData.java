package com.app.minyaneto_android.utilities;

import com.app.minyaneto_android.models.minyan.ExactTime;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by משה on 25/08/2017.
 */

public class SynagougeFictiveData {
    public static ArrayList<Synagogue> getFictiveSynagouges(LatLng location) {
        ArrayList<Synagogue> synagogues = new ArrayList<>();

        Synagogue s = new Synagogue("ירושלים קרית משה", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל משה", new LatLng(location.latitude + 0.001, location.longitude - 0.001), "ספרד", false, true, true, true);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים רמות", "לעלות בכניסה ג לקומה 2", "בית מנחם", new LatLng(location.latitude + 0.001, location.longitude + 0.001), "עדות המזרח", false, true, false, true);
        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים גבעת שאול", "כניסה ב", "אהל רבקה", new LatLng(location.latitude - 0.001, location.longitude - 0.001), "חב'ד", true, true, false, false);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים גבעת שאול", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה", new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז", false, true, true, false);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים בית הכרם", "לעלות בכניסה ג לקומה 2", "אהל לאה ורחל", new LatLng(location.latitude + 0.0037, location.longitude + 0.00281), "עדות המזרח", true, false, true, true);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים רמות", "כניסה ב", "כרם התימנים", new LatLng(location.latitude + 0.0041, location.longitude - 0.001), "ספרד", true, false, false, true);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים קרית משה", "לרדת 2 קומות", "מיימון", new LatLng(location.latitude - 0.0015, location.longitude + 0.005), "תימני", false, true, false, true);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים בית הדפוס", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה", new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז", false, true, true, false);

        s.addMinyan(new Minyan("1", PrayType.MORNING, new ExactTime(12,0),
                PrayDayType.MONDAY));
        synagogues.add(s);

        return synagogues;
    }
}
