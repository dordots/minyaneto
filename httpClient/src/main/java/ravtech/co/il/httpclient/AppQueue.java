package ravtech.co.il.httpclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class AppQueue {

    protected static final String TAG = "all_queue" ;

    private static final int DEFAULT_CACHE_SIZE = 20 * 1024 * 1024;

    private static AppQueue mInstance;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private Context mCtx;

    private AppQueue(Context context) {

        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {

                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {

                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized AppQueue getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new AppQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(@NonNull  Request<T> req) {

        req.setTag(TAG);

        setTimeOut(req);

        getRequestQueue().add(req);
    }


    private <T> void setTimeOut(Request<T> req) {

        req.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public ImageLoader getImageLoader() {

        return mImageLoader;
    }


    public void cancelAll(){

        if(getRequestQueue() != null)
            getRequestQueue().cancelAll(TAG);

    }

}