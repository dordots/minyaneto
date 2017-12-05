package com.app.minyaneto_android.restApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.app.minyaneto_android.models.minyan.Minyan;

import ravtech.co.il.httpclient.AppQueue;
import ravtech.co.il.httpclient.ErrorResponse;
import ravtech.co.il.httpclient.GenericRequest;
import ravtech.co.il.httpclient.model.Result;


/**
 * class to handel the request
 * <p/>
 * Created by droiter on 5/15/2016.
 */
public class RequestHelper {

    public static final String TAG = RequestHelper.class.getSimpleName();

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    public static void getUser(Context context, int id, Response.Listener<Result<Minyan>> responseListener, ErrorResponse.ErrorListener errorListener) {

        GenericRequest<Result<Minyan>> request = new GenericRequest<>(
                URL.getUser(id),
                Minyan.class,
                responseListener,
                new ErrorResponse(errorListener),
                Headers.getHeaders()
        );

        setRetryPolicy(request);

        AppQueue.getInstance(context).addToRequestQueue(request);
    }

    public static void setUser(Context context, Object user, Response.Listener<Object> mResponseListener, Response.ErrorListener errorResponse) {

        GenericRequest<Object> request = new GenericRequest<Object>(
                Request.Method.POST,
                URL.getUrlOrders(),
                Object.class,
                user,
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


}
