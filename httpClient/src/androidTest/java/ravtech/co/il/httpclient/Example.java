package ravtech.co.il.httpclient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by shahar on 27/02/16.
 *
 * volley test
 */
public class Example implements Response.Listener<Example.Response>,Response.ErrorListener {

    int i = 2;

    String s = "s";

    private GenericRequest<Response> genericRequest;


    public  static class Response{
        String response;
        int number;
    }

    public  static class ExampleSend{
        String i = "i";
        int number = 0;
    }

    public void onResume(){

        final ExampleSend toBeSent = new ExampleSend();
        new GenericRequest<>("",Response.class,this,this);
        genericRequest =  new GenericRequest<>(Request.Method.POST, "http://google.com",
                Response.class, toBeSent,
                this, this
        );

        AppQueue.getInstance(null/*context*/).addToRequestQueue(genericRequest);


    }

    public void onPause(){
        genericRequest.cancel();
    }

    public void onDestroy(){
        AppQueue.getInstance(null).cancelAll();
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Response response) {


    }


}
