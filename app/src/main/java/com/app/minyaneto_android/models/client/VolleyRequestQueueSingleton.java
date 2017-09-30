package com.app.minyaneto_android.models.client;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by david on 09/09/2017.
 */

public class VolleyRequestQueueSingleton {
    private static VolleyRequestQueueSingleton mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private VolleyRequestQueueSingleton(Context context) {
        mCtx = context;
        mRequestQueue = this.getRequestQueue();
    }

    public static synchronized VolleyRequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueueSingleton(context);
        }
        return VolleyRequestQueueSingleton.mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            Cache cache = new DiskBasedCache(this.mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            this.mRequestQueue = new RequestQueue(cache, network);

            this.mRequestQueue.start();
        }

        return this.mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
