package com.example.niyanta.askforleave.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.niyanta.askforleave.Common.NotificationUtils;
import com.example.niyanta.askforleave.Common.Constants;
import com.example.niyanta.askforleave.Common.DatabaseManager;
import com.example.niyanta.askforleave.Activity.NotificationView;
import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.Common.SharedPrefManager;
import com.example.niyanta.askforleave.Interface.getResponseFromDBNotif;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    DatabaseManager manager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        manager = new DatabaseManager(getApplicationContext());

        if (remoteMessage == null)
            return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            // Check if message contains a notification payload.
        } else if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void handleNotification(final String message) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        String date = format.format(calendar.getTime());
        final String time = timeFormat.format(calendar.getTime());
        manager.insertNotification("AskForLeave", message, date, new getResponseFromDBNotif() {
            @Override
            public void OnSuccess(long result) {
                Intent intent = new Intent(getApplicationContext(), NotificationView.class);
                showNoti("AskForLeave", message, time, intent);
                Log.e("Insert", "Success");
            }

            @Override
            public void OnFailure(long result) {
                Log.e("Insert", "Failure");
            }
        });

        Intent pushNotification = new Intent(SharedPrefManager.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        //pushNotification.putExtra("title",title);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            final String title = data.getString("title");
            final String message = data.getString("message");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aa");
            String date = format.format(calendar.getTime());
            final String time = formatTime.format(calendar.getTime());

            manager.insertNotification(title, message, date, new getResponseFromDBNotif() {
                @Override
                public void OnSuccess(long result) {
                    Intent intent = new Intent(getApplicationContext(), NotificationView.class);
                    showNoti(title, message, time, intent);
                    Log.e("Insert", "Success");
                }

                @Override
                public void OnFailure(long result) {
                    Log.e("Insert", "Failure");
                }
            });

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(SharedPrefManager.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            }else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), NotificationView.class);
                resultIntent.putExtra("message", message);

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
//
    public void showNoti(String title, String message, String time, Intent intent) {

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/notification");
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_ID);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(SharedPrefManager.NOTIFICATION_ID, notification);
    }
}