package ravtech.co.il.httpclient;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;

import ravtech.co.il.httpclient.model.ErrorData;
import ravtech.co.il.httpclient.model.Result;

/**
 * Created by maytav on 9/5/2016.
 */
public class ErrorResponse implements Response.ErrorListener {

    private static final String TAG = ErrorResponse.class.getSimpleName();
    private ErrorListener mListener;


    public ErrorResponse(ErrorListener mListener) {

        this.mListener = mListener;

    }


    @Override
    public void onErrorResponse(final VolleyError error) {

        Result<ErrorData> errorDataResult = new Result<>();;

        if (error.networkResponse != null) {
            try {

                Log.d(TAG , new String(error.networkResponse.data, StandardCharsets.UTF_8));

                errorDataResult = new Gson().fromJson(new String(error.networkResponse.data, StandardCharsets.UTF_8),
                        new TypeToken<Result<ErrorData>>() {
                        }.getType());
            } catch (Exception e) {

                try {

                    errorDataResult.setErrorCode(error.networkResponse.statusCode);
                    errorDataResult.setSuccess(false);
                    errorDataResult.setErrors(new String(error.networkResponse.data, StandardCharsets.UTF_8));

                    ErrorData errorData = new ErrorData();

                    errorData.setCode(error.networkResponse.statusCode);
                    errorData.setMessage(new String(error.networkResponse.data, StandardCharsets.UTF_8));

                    errorDataResult.setData(errorData);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        }else {

            errorDataResult.setErrors(error.getMessage());

            ErrorData errorData = new ErrorData();

            errorData.setMessage(error.getMessage());

            errorDataResult.setData(errorData);

        }
        mListener.onErrorResponse(errorDataResult);
    }


    public interface ErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        void onErrorResponse(Result<ErrorData> error);
    }


}
