package com.example.niyanta.askforleave.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtils {


    public static String email = "email";
    public static String password = "password";
    public static String id = "id";
    public static String user_id = "user_id";
    public static String name = "name";
    public static String isLogin = "isLogin";
    public static String rep_mang_id ="rep_mang_id";
    public static String leave_count="leave_count";
    public static String leavestatus="leavestatus";
    public static String from_date = "from_data";
    public static String to_date = "to_date";
    public static String reason = "reason";
    public static String role ="role";
    public static String request_id="request_id";
    public static String status ="status";



    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static SharedPreferences.Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }


    public static String getPreferenceValue(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }


    public static int getPreferenceValue(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static boolean getPreferenceValue(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }


    public static void setPreferenceValue(Context context, String key, String prefsValue) {
        getPreferencesEditor(context).putString(key, prefsValue).apply();
    }

    public static void setPreferenceValue(Context context, String key, int prefsValue) {
        getPreferencesEditor(context).putInt(key, prefsValue).apply();
    }

    public static void setPreferenceValue(Context context, String key, boolean prefsValue) {
        getPreferencesEditor(context).putBoolean(key, prefsValue).apply();
    }


    public static boolean containsPreferenceKey(Context context, String key) {
        return getPreferences(context).contains(key);
    }

    public static void removePreferenceValue(Context context, String key) {
        getPreferencesEditor(context).remove(key).apply();
    }

    public static String getPreferencesValue(Context applicationContext, String leave_count, String s) {
        return null;
    }





}
