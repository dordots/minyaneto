package com.app.minyaneto_android.models.client;

/**
 * Created by david on 06/09/2017.
 */

import com.google.gson.Gson;

public class GenericJsonParser<T> {
    private Class<T> mClassObject = null;
    private Gson mGson = null;

    private static Gson static_gson;

    static {
        static_gson = new Gson();
    }

    public GenericJsonParser(Class<T> tClassObject) {
        this.mClassObject = tClassObject;
        this.mGson = new Gson();
    }

    public String toJson(T toJsonObject)
    {
        if (null == this.mGson)
        {
            this.mGson = new Gson();
        }

        return mGson.toJson(toJsonObject);
    }

    public T fromJson(String jsonString)
    {
        if (null == this.mGson)
        {
            this.mGson = new Gson();
        }

        if (this.mClassObject == null)
        {
            return null;
        }

        return (T)this.mGson.fromJson(jsonString, mClassObject);
    }

    public static Object fromJson(String jsonString, Class objectClass)
    {
        Object jsonObject;

        synchronized (GenericJsonParser.static_gson) {
            if (null == GenericJsonParser.static_gson) {
                GenericJsonParser.static_gson = new Gson();
            }

            jsonObject = GenericJsonParser.static_gson.fromJson(jsonString, objectClass);
        };
        return jsonObject;
    }

    public static String toJson(Object object, Class objectClass)
    {
        String jsonString = null;

        synchronized (GenericJsonParser.static_gson) {
            if (null == GenericJsonParser.static_gson) {
                GenericJsonParser.static_gson = new Gson();
            }

            jsonString = GenericJsonParser.static_gson.toJson(object);
        }

        return jsonString;
    }

}
