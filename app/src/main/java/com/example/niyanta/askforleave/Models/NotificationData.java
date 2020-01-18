package com.example.niyanta.askforleave.Models;

public class NotificationData {
    String id;
    String title;
    String message;
    String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title =title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NotificationData() {
        this.id = id;
        this.title = title;
        this.message = message;
        this.time = time;

    }


}

