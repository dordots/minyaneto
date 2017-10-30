package com.app.minyaneto_android.models.client;

import android.util.Log;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by david on 09/09/2017.
 */

public class CustomJSONObjectRequest<T> implements JSONObjectRequestHandlerInterface<T>, Response.Listener<JSONObject>, Response.ErrorListener {

    protected JsonObjectRequest mJsonObjReq = null;
    protected VolleyRequestQueueSingleton mVolleyInstance = null;

    protected JSONObjectRequestHandlerInterface<T> mJsonRequestHandler = null;

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        this.mJsonObjReq = new JsonObjectRequest(method, url, jsonRequest, listener, errorListener);

    }

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest) {
        this.mJsonObjReq = new JsonObjectRequest(method, url, jsonRequest, this, this);
    }

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest, JSONObjectRequestHandlerInterface<T> mJsonRequestHandler) {
        this.mJsonObjReq = new JsonObjectRequest(method, url, jsonRequest, this, this);
        this.mJsonRequestHandler = mJsonRequestHandler;
    }


    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest, VolleyRequestQueueSingleton volleyInstance) {
        this.mJsonObjReq = new JsonObjectRequest(method, url, jsonRequest, this, this);
        this.mVolleyInstance = volleyInstance;
    }

    /* JSONObjectRequestHandlerInterface methods */

    @Override
    public boolean isProcessReceivedDataImplemented() {
        return false;
    }

    @Override
    public boolean isExecuteCommandsImplemented() {
        return false;
    }

    @Override
    public T processReceivedData(JSONObject jsObj) {
        Log.d("custom_josn", "processReceivedData");
        return null;
    }

    @Override
    public boolean executeCommands(Object processedData) {
        Log.d("custom_josn", "executeCommands");
        return false;
    }

    /* Response.Listener<JSONObject> methods */

    @Override
    final public void onResponse(JSONObject response) {
        T processedData;
        boolean success;

        Log.d("custom_josn", "onResponse");

        if (null != this.mJsonRequestHandler && this.mJsonRequestHandler.isProcessReceivedDataImplemented()) {
            processedData = this.mJsonRequestHandler.processReceivedData(response);
        }
        else {
            processedData = this.processReceivedData(response);
        }

        if (null != this.mJsonRequestHandler && this.mJsonRequestHandler.isExecuteCommandsImplemented()) {
            success = this.mJsonRequestHandler.executeCommands(processedData);
        }
        else {
            success = this.executeCommands(processedData);
        }
    }

    /* Response.ErrorListener */

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("custom_josn", "onErrorResponse");
    }

    /* Add to requests queue functions */

    public boolean addToRequestQueue()
    {
        if (null == this.mVolleyInstance)
        {
            return false;
        }

        this.mVolleyInstance.addToRequestQueue(this.mJsonObjReq);
        return true;
    }

    public boolean addToRequestQueue(VolleyRequestQueueSingleton volleyInstance)
    {
        if (null == volleyInstance)
        {
            return false;
        }

        volleyInstance.addToRequestQueue(this.mJsonObjReq);
        return true;
    }


    /* Getters and Setters */

    public JsonObjectRequest getmJsonObjReq() {
        return mJsonObjReq;
    }

    public void setmJsonObjReq(JsonObjectRequest mJsonObjReq) {
        this.mJsonObjReq = mJsonObjReq;
    }

    public JSONObjectRequestHandlerInterface<T> getmJsonRequestHandler() {
        return mJsonRequestHandler;
    }

    public void setmJsonRequestHandler(JSONObjectRequestHandlerInterface<T> mJsonRequestHandler) {
        this.mJsonRequestHandler = mJsonRequestHandler;
    }
}

