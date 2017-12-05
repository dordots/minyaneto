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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import ravtech.co.il.httpclient.model.DataPart;

public class VolleyMultipartRequest<T> extends Request<T> {

    private static final String twoHyphens = "--";

    private static final String lineEnd = "\r\n";

    private static final String boundary = "apiclient-" + System.currentTimeMillis();

//    private static final String mimeType = "multipart/form-data;boundary=" + boundary;

    private final Response.Listener<T> mListener;

    private final Response.ErrorListener mErrorListener;

    private final Map<String, String> mHeaders;

    private final String mMimeType;


//    private final DataOutputStream dos;

    //    private final ByteArrayOutputStream bos;
    private final Type type;

    private Class<? extends T> clazz;


    public VolleyMultipartRequest(String url, Class<T> returnClass, Map<String, String> headers, String mimeType, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
        this.mMimeType = mimeType;
        clazz = returnClass;
        type = null;

        /*bos = new ByteArrayOutputStream();
        dos = new DataOutputStream(bos);

        try {
            buildPart(dos, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public VolleyMultipartRequest(String url, Type returnClass, Map<String, String> headers, String mimeType, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
        this.mMimeType = mimeType;
        type = returnClass;
        clazz = null;

//        bos = new ByteArrayOutputStream();
//        dos = new DataOutputStream(bos);

      /*  try {
            buildPart(dos, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
/*
    public VolleyMultipartRequest(Request.Method.POST,String url, Response.Listener<T>() {
        super(Method.POST,url ,errorListener );

    }*/

   /* public void addPart(String fileName) {
        try {
            buildPart(dos, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
    }*/

    public VolleyMultipartRequest(String url, Class<T> returnClass, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        this(url, returnClass, null, "image/*", fileName, listener, errorListener);
    }

    public VolleyMultipartRequest(String url, Type returnClass, String fileName, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        this(url, returnClass, null, "image/*", fileName, listener, errorListener);
    }

    protected VolleyMultipartRequest(String url, Type returnClass, Response.Listener<T> listener, ErrorResponse errorListener) {
//        super(url, type, listener, errorResponse);
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = null;
        this.mMimeType = null;
        type = returnClass;
        clazz = null;


    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {

        return "multipart/form-data;boundary=" + boundary;
    }

    /**
     * Custom method handle data payload.
     *
     * @return Map data part label with data byte
     * @throws AuthFailureError
     */
    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // populate text payload
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }

            // populate data byte payload
            Map<String, DataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }

            // close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    /**
     * Write string data into header and data output stream.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param parameterName    name of input
     * @param parameterValue   value of input
     * @throws IOException
     */
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    /**
     * Parse data into data output stream.
     *
     * @param dataOutputStream data output stream handle file attachment
     * @param data             loop through data
     * @throws IOException
     */
    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {


        try {
            // If it's not muted; we just need to create our POJO from the returned JSON and handle correctly the errors
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            T parsedObject;

            if (clazz == null) {

                parsedObject = new Gson().fromJson(json, type);

            } else {

                parsedObject = new Gson().fromJson(json, clazz);
            }

            return Response.success(parsedObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }

    }

    /**
     * Write data file into header and data output stream.
     *
     * @param dataOutputStream data output stream handle data parsing
     * @param dataFile         data byte as DataPart from collection
     * @param inputName        name of data input
     * @throws IOException
     */
    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private void buildPart(DataOutputStream dataOutputStream, String fileName) throws IOException {

        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes("Content-Type: " + mMimeType + lineEnd);
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