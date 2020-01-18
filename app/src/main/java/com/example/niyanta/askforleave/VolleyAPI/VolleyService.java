package com.example.niyanta.askforleave.VolleyAPI;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyService {

    private static VolleyService instance;
    private RequestQueue requestQueue;
    private static Context mctx;

    private VolleyService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized  VolleyService getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyService(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
