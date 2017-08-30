package com.app.minyaneto_android.utilities;

import com.app.minyaneto_android.models.Minyan;
import com.app.minyaneto_android.models.Synagogue;
import com.app.minyaneto_android.models.minyan.ExactTime;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by משה on 25/08/2017.
 */

public class SynagougeFictiveData {
    public static ArrayList<Synagogue> getFictiveSynagouges(LatLng location) {
        ArrayList<Synagogue> synagogues = new ArrayList<>();

        Synagogue s = new Synagogue("ירושלים קרית משה", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל משה", new LatLng(location.latitude + 0.001, location.longitude - 0.001), "ספרד", false, true, true, true);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(16, 30), PrayDayType.SUNDAY));
        s.addMinyan(new Minyan( PrayType.EVENING, new ExactTime(17, 30), PrayDayType.SUNDAY));
        s.addMinyan(new Minyan(PrayType.MORNING, new ExactTime(8, 00), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים רמות", "לעלות בכניסה ג לקומה 2", "בית מנחם", new LatLng(location.latitude + 0.001, location.longitude + 0.001), "עדות המזרח", false, true, false, true);
        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(16, 30), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים גבעת שאול", "כניסה ב", "אהל רבקה", new LatLng(location.latitude - 0.001, location.longitude - 0.001), "חב'ד", true, true, false, false);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(13, 30), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים גבעת שאול", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה", new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז", false, true, true, false);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(16, 00), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים בית הכרם", "לעלות בכניסה ג לקומה 2", "אהל לאה ורחל", new LatLng(location.latitude + 0.0037, location.longitude + 0.00281), "עדות המזרח", true, false, true, true);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(16, 30), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים רמות", "כניסה ב", "כרם התימנים", new LatLng(location.latitude + 0.0041, location.longitude - 0.001), "ספרד", true, false, false, true);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(16, 30), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים קרית משה", "לרדת 2 קומות", "מיימון", new LatLng(location.latitude - 0.0015, location.longitude + 0.005), "תימני", false, true, false, true);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(12, 30), PrayDayType.SUNDAY));
        synagogues.add(s);

        s = new Synagogue("ירושלים בית הדפוס", "לפנות במסדרון שמאלה ולעלות במדרגות לקומה 1", "אהל שרה", new LatLng(location.latitude - 0.00124, location.longitude - 0.0021), "אשכנז", false, true, true, false);

        s.addMinyan(new Minyan( PrayType.AFTER_NOON, new ExactTime(16, 30), PrayDayType.SUNDAY));
        synagogues.add(s);

        return synagogues;
    }




}
