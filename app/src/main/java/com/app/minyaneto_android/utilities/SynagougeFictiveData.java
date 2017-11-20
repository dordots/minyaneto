package com.app.minyaneto_android.utilities;

import com.app.minyaneto_android.models.minyan.ExactTime;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.RelativeTime;
import com.app.minyaneto_android.models.minyan.RelativeTimeType;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by משה on 25/08/2017.
 */

public class SynagougeFictiveData {
    public static ArrayList<Synagogue> getFictiveSynagouges(LatLng location) {
        ArrayList<Synagogue> synagogues = new ArrayList<>();
//        Synagogue s = new Synagogue("ירושלים קרית משה", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1",
//                "אהל משה", new LatLng(location.latitude + 0.001, location.longitude - 0.001), "ספרד",
//                false, true, true, true,10,8);
//
//        s.addMinyan(new Minyan(PrayType.MORNING, new RelativeTime(RelativeTimeType.DAWN, 72),
//                PrayDayType.MONDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים רמות", "לעלות בכניסה ג לקומה 2", "בית מנחם",
//                new LatLng(location.latitude + 0.001, location.longitude + 0.001), "עדות המזרח",
//                false, true, false, true,17,2);
//        s.addMinyan(new Minyan(PrayType.AFTER_NOON, new ExactTime(12,30),
//                PrayDayType.SUNDAY, PrayDayType.SATURDAY));
//        s.addMinyan(new Minyan(PrayType.AFTER_NOON, new ExactTime(12,45),
//                PrayDayType.SUNDAY, PrayDayType.SATURDAY));
//        s.addMinyan(new Minyan(PrayType.AFTER_NOON, new ExactTime(12,00),
//                PrayDayType.SUNDAY, PrayDayType.SATURDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים גבעת שאול", "כניסה ב", "אהל רבקה",
//                new LatLng(location.latitude - 0.001, location.longitude - 0.001), "חב'ד",
//                true, true, false, false,2,2);
//
//        s.addMinyan(new Minyan(PrayType.EVENING, new RelativeTime(RelativeTimeType.SUNSET,-12),
//                PrayDayType.TUESDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים גבעת שאול", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה",
//                new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז",
//                false, true, true, false, 12,4 );
//
//        s.addMinyan(new Minyan(PrayType.MORNING, new ExactTime(12,10),
//                PrayDayType.WEDNESDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים בית הכרם", "לעלות בכניסה ג לקומה 2", "אהל לאה ורחל",
//                new LatLng(location.latitude + 0.0037, location.longitude + 0.00281), "עדות המזרח",
//                true, false, true, true, 12,4 );
//
//        s.addMinyan(new Minyan(PrayType.MORNING, new RelativeTime(RelativeTimeType.SUNSET,0),
//                PrayDayType.FRIDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים רמות", "כניסה ב", "כרם התימנים",
//                new LatLng(location.latitude + 0.0041, location.longitude - 0.001), "ספרד",
//                true, false, false, true,7,4 );
//
//        s.addMinyan(new Minyan(PrayType.MORNING, new ExactTime(11,50),
//                PrayDayType.MONDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים קרית משה", "לרדת 2 קומות", "מיימון",
//                new LatLng(location.latitude - 0.0015, location.longitude + 0.005), "תימני",
//                false, true, false, true, 1,1 );
//
//        s.addMinyan(new Minyan(PrayType.MORNING, new ExactTime(12,30),
//                PrayDayType.MONDAY));
//        synagogues.add(s);
//
//        s = new Synagogue("ירושלים בית הדפוס", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה",
//                new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז",
//                false, true, true, false, 12,4 );
//
//        s.addMinyan(new Minyan(PrayType.MORNING, new ExactTime(13,0),
//                PrayDayType.MONDAY));
//        synagogues.add(s);
//
        return synagogues;
    }
}
