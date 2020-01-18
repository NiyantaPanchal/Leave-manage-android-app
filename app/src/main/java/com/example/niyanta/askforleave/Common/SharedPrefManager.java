package com.example.niyanta.askforleave.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final String SHARED_PREF = "ah_firebase";

    public static final String KEY_ACCESS_TOKEN = "device_id";
    public static final String title ="title";
    public static final String message ="message";

    private static Context mctx;
    private static SharedPrefManager mInstance;


    private SharedPrefManager(Context context){
        mctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context)
    {
        if(mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
        }

        //this method will save the device token to shared preferences
        public boolean saveDeviceToken(String token){
            SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_ACCESS_TOKEN, token);
            editor.apply();
            return true;
        }
    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
}
