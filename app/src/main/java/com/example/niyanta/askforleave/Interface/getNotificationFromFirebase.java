package com.example.niyanta.askforleave.Interface;

import com.example.niyanta.askforleave.Models.NotificationData;

import java.util.ArrayList;

public interface getNotificationFromFirebase {

    void OnSuccess(ArrayList<NotificationData> notificationData);

    void OnFailure(String Message);
}
