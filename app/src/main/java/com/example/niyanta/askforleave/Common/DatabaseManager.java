package com.example.niyanta.askforleave.Common;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.niyanta.askforleave.Interface.getNotificationFromFirebase;
import com.example.niyanta.askforleave.Interface.getResponseFromDBNotif;
import com.example.niyanta.askforleave.Models.NotificationData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by sonnguyen on 12/16/15.
 */
public class DatabaseManager {

    public SQLiteDatabase database;
    private Context mContext;

    @SuppressLint("WrongConstant")
    public DatabaseManager(Context context) {
        mContext = context;
        database = mContext.openOrCreateDatabase("AskForLeave.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        openDatabase();
    }

    @SuppressLint("WrongConstant")
    private void openDatabase() {
        database.execSQL("CREATE TABLE IF NOT EXISTS `notification`(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,message TEXT,time TEXT) ");
    }

    public void insertNotification(String title, String message, String time, getResponseFromDBNotif getResponseFromDBNotif) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("message", message);
        values.put("time", time);
        long result = database.insert("notification", null, values);

        if (result > 0) {
            getResponseFromDBNotif.OnSuccess(result);
        } else {
            getResponseFromDBNotif.OnFailure(result);
        }
    }

    public void clearNotifications(String id, getResponseFromDBNotif getResponseFromDBNotif) {

        if (id == "") {
            long result = database.delete("notification", "", null);
            if (result > 0) {
                getResponseFromDBNotif.OnSuccess(result);
            } else {
                getResponseFromDBNotif.OnFailure(result);
            }
        } else {
            long result = database.delete("notification", "id = ?", new String[]{id});
            if (result > 0) {
                getResponseFromDBNotif.OnSuccess(result);
            } else {
                getResponseFromDBNotif.OnFailure(result);
            }
        }
    }

    public void getAllNotifications(getNotificationFromFirebase getNotificationFormDatabase) {

        ArrayList<NotificationData> notificationData = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM `notification`", null);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                NotificationData data = new NotificationData();
                data.setId(cursor.getString(cursor.getColumnIndex("id")));
                data.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                data.setMessage(cursor.getString(cursor.getColumnIndex("message")));

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat df_show = new SimpleDateFormat("hh:mm aa");
                SimpleDateFormat df_older_show = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                String formateOfDate = "";

                Date date1 = null;
                try {
                    date1 = dateFormat.parse(cursor.getString(cursor.getColumnIndex("time")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String currentDate = df.format(c.getTime());
                String last_seen = df.format(date1);
                String dateToView = df_show.format(date1);
                String olderDate = df_older_show.format(date1);

                if (currentDate.equalsIgnoreCase(last_seen)) {
                    formateOfDate = "Today, " + dateToView;
                } else {
                    formateOfDate = olderDate;
                }
                data.setTime(formateOfDate);
                notificationData.add(data);
            } while (cursor.moveToNext());

            Collections.reverse(notificationData);

            getNotificationFormDatabase.OnSuccess(notificationData);
        } else {
            getNotificationFormDatabase.OnFailure("No Notifications Found..");
        }
    }
}