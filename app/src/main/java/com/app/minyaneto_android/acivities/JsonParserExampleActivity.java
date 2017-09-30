package com.app.minyaneto_android.acivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.client.CustomJSONObjectRequest;
import com.app.minyaneto_android.models.client.JSONObjectRequestHandlerInterface;
import com.app.minyaneto_android.models.client.VolleyRequestQueueSingleton;

import org.json.JSONObject;

public class JsonParserExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String url = "http://minyaneto.startach.com/v1/synagogues/?top_left=42.0000000,-72.0000000&bottom_right=40.0000000,-74.0000000";
        CustomJSONObjectRequest<String> cjsobj = new CustomJSONObjectRequest<String>(Request.Method.GET, url, null);

        JSONObjectRequestHandlerInterface<String> analyzer = new JSONObjectRequestHandlerInterface<String>() {
            @Override
            public boolean isProcessReceivedDataImplemented() {
                return true;
            }

            @Override
            public boolean isExecuteCommandsImplemented() {
                return true;
            }

            @Override
            public String processReceivedData(JSONObject jsObj) {
                Log.d("json_subtree", "processReceivedData");
                return null;
            }

            @Override
            public boolean executeCommands(String processedData) {
                Log.d("json_subtree", "executeCommands");
                return false;
            }
        };
        cjsobj.setmJsonRequestHandler(analyzer);
        cjsobj.addToRequestQueue(VolleyRequestQueueSingleton.getInstance(this));

                /*Log.d("json_debug", response.getJSONArray("synagogues").toString());*/
        /*JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++) {
                            JSONObject synagogue = null;
                            try {
                                synagogue = response.getJSONObject(i);
                                Log.d("json_debug", "Response: " + synagogue.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }*/
        /*ModelObject modelObject = new ModelObject("myname", 12, true, 2.3);
        GenericJsonParser<ModelObject> gsp = new GenericJsonParser<ModelObject>(ModelObject.class);
        String jsonString = GenericJsonParser.toJson(modelObject, ModelObject.class);
        Log.d("json", jsonString);
        Log.d("class", ((ModelObject)GenericJsonParser.fromJson(jsonString, ModelObject.class)).toString());*/
    }

    public class ModelObject {
        String name;
        int val;
        boolean status;
        double f;

        public ModelObject(String name, int val,
                           boolean status, double f) {
            super();
            this.name = name;
            this.val = val;
            this.status = status;
            this.f = f;
        }

        @Override
        public String toString() {
            return "ModelObject [name=" + name + ", " +
                    "val=" + val + ", status="
                    + status + ", f=" + f + "]";
        }

    }

}

