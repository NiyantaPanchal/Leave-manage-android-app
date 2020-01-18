package com.example.niyanta.askforleave.VolleyAPI;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private String check_url;
    private Map<String, String> headers = new HashMap<String, String>();

    public CustomRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        check_url = url;
    }


    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    };

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    //In your extended request class
    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            NetworkResponse response = volleyError.networkResponse;
            Log.e("tag", "parseNetworkError is ! null ");
            Log.e("tag", "parseNetworkError status code : " + response.statusCode);
            Log.e("tag", "parseNetworkError message : " + error.getLocalizedMessage());
            //volleyError = error;
        } else {
            Log.e("", "parseNetworkError is null ");
            if (volleyError instanceof TimeoutError) {
                Log.e("tag", "TimeoutError:parseNetworkError:" + volleyError.getLocalizedMessage());
            } else if (volleyError instanceof NoConnectionError) {
                Log.e("tag", "NoConnectionError:parseNetworkError:" + volleyError.getLocalizedMessage());
            } else if (volleyError instanceof AuthFailureError) {
                Log.e("tag", "AuthFailureError:parseNetworkError:" + volleyError.getLocalizedMessage());
            } else if (volleyError instanceof ServerError) {
                Log.e("tag", "ServerError:parseNetworkError:" + volleyError.getLocalizedMessage());
            } else if (volleyError instanceof NetworkError) {
                Log.e("tag", "NetworkError:parseNetworkError:" + volleyError.getLocalizedMessage());
            } else if (volleyError instanceof ParseError) {
                Log.e("tag", "ParseError:parseNetworkError:" + volleyError.getLocalizedMessage());
            }
        }
        return volleyError;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        //TODO Auto-generated method stub
        listener.onResponse(response);
    }
}