package com.app.minyaneto_android.restApi;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.minyaneto_android.models.minyan.Minyan;

import ravtech.co.il.httpclient.ErrorResponse;
import ravtech.co.il.httpclient.model.ErrorData;
import ravtech.co.il.httpclient.model.Result;

/**
 * Created by david vardi.
 */

public class Demo {

    void test(Context context) {

        RequestHelper.getUser(context, 0, new Response.Listener<Result<Minyan>>() {
            @Override
            public void onResponse(Result<Minyan> response) {

                ;

            }
        }, new ErrorResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Result<ErrorData> error) {

                error.getData().getMessage();

            }
        });
    }

    void test2(Context context) {

        RequestHelper.setUser(context, new Object(), new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
