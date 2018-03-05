package com.app.minyaneto_android.restApi;

public interface ResponseListener<T> {

  void onResponse(T response);
}
