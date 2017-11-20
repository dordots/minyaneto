package com.app.minyaneto_android.models.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 17/11/2017.
 */

public class HelpJsonParser {
    public static List<JSONObject> parseJsonData(JSONObject obj, String pattern) throws JSONException {

        List<JSONObject> listObjs = new ArrayList<JSONObject>();
        JSONArray geodata = obj.getJSONArray(pattern);
        for (int i = 0; i < geodata.length(); ++i) {
            final JSONObject site = geodata.getJSONObject(i);
            listObjs.add(site);
        }
        return listObjs;
    }
}
