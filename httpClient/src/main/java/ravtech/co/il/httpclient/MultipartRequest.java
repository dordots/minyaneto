package ravtech.co.il.httpclient;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

public class MultipartRequest<T> extends Request<T> {

    private static final String twoHyphens = "--";

    private static final String lineEnd = "\r\n";

    private static final String boundary = "apiclient-" + System.currentTimeMillis();

    private static final String mimeType = "multipart/form-data;boundary=" + boundary;

    private final Response.Listener<T> mListener;

    private final Response.ErrorListener mErrorListener;

    private final Map<String, String> mHeaders;

    private final String mMimeType;


    private final DataOutputStream dos;

    private final ByteArrayOutputStream bos;
    private final Type type;

    private Class<? extends T> clazz;


    public MultipartRequest(String url, Class<T> returnClass, Map<String, String> headers, String mimeType, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
        this.mMimeType = mimeType;
        clazz = returnClass;
        type = null ;

         bos = new ByteArrayOutputStream();
         dos = new DataOutputStream(bos);

        try {
             buildPart(dos,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MultipartRequest(String url, Type returnClass, Map<String, String> headers, String mimeType, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
        this.mMimeType = mimeType;
        type = returnClass;
        clazz = null ;

         bos = new ByteArrayOutputStream();
         dos = new DataOutputStream(bos);

        try {
             buildPart(dos,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(String fileName){
        try {
            buildPart(dos,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public MultipartRequest(String url, Class<T> returnClass, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        this(url,returnClass,null,"image/*",fileName,listener,errorListener);
    }

    public MultipartRequest(String url, Type returnClass, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        this(url,returnClass,null,"image/*",fileName,listener,errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {

        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        return bos.toByteArray();
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {


            try {
                // If it's not muted; we just need to create our POJO from the returned JSON and handle correctly the errors
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                T parsedObject ;

                if (clazz == null){

                    parsedObject =  new Gson().fromJson(json, type);

                }else {

                    parsedObject = new Gson().fromJson(json, clazz);
                }

                return Response.success(parsedObject, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }

    }


    private void buildPart(DataOutputStream dataOutputStream, String fileName) throws IOException {

        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes("Content-Type: "+mMimeType + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

//        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileName);
        FileInputStream fileInputStream = new FileInputStream(fileName);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }


    @Override
    protected void deliverResponse(T response) {

        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {

        mErrorListener.onErrorResponse(error);
    }
}