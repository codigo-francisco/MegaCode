package com.udacity.gamedev.gigagal.android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by Francisco on 26/02/2018.
 */

public class CustomRequest extends StringRequest {

    private Map<String, String> params;

    CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> params){
        super(method, url, listener, errorListener);
        this.params = params;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
