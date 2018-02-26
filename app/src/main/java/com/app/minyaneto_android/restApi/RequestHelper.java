package com.app.minyaneto_android.restApi;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.synagogue.SynagogueArray;
import com.google.android.gms.maps.model.LatLng;

import ravtech.co.il.httpclient.AppQueue;
import ravtech.co.il.httpclient.ErrorResponse;
import ravtech.co.il.httpclient.GenericRequest;

public class RequestHelper {

    public static final String TAG = RequestHelper.class.getSimpleName();

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    public static void addSynagogue(Context context, Synagogue synagogue, Response.Listener<Synagogue> mResponseListener,
                                    Response.ErrorListener errorResponse) {

        GenericRequest<Synagogue> request = new GenericRequest<>(
                Request.Method.POST,
                URL.getAddSynagogue(),
                Synagogue.class,
                synagogue,
                mResponseListener,
                errorResponse,
                Headers.getHeaders()
        );

        setRetryPolicy(request);

        AppQueue.getInstance(context).addToRequestQueue(request);

    }

    public static void updateSynagogue(Context context, String id, Synagogue synagogue, Response.Listener<String> mResponseListener,
                                       Response.ErrorListener errorResponse) {

        GenericRequest<String> request = new GenericRequest<>(
                Request.Method.PUT,
                URL.getUpdateSynagogue(id),
                Synagogue.class,
                synagogue,
                mResponseListener,
                errorResponse,
                Headers.getHeaders()
        );

        setRetryPolicy(request);

        AppQueue.getInstance(context).addToRequestQueue(request);

    }

    private static void setRetryPolicy(GenericRequest<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static void getSynagogues(Context context, LatLng center, double radius,
                                     Response.Listener<SynagogueArray> responseListener,
                                     ErrorResponse.ErrorListener errorListener) {

        GenericRequest<SynagogueArray> request = new GenericRequest<>(
                URL.getUrlSynagogues(center, radius),
                SynagogueArray.class,
                responseListener,
                new ErrorResponse(errorListener),
                Headers.getHeaders()
        );

        setRetryPolicy(request);

        AppQueue.getInstance(context).addToRequestQueue(request);
    }
}
