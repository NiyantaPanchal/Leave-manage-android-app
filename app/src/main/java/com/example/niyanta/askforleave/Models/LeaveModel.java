package com.example.niyanta.askforleave.Models;

public class LeaveModel {
    private String user_id;
    private String from_date;
    private String to_date;
    private String leavestatus;
    private String reason;

    public String getUser_id(String user_id) {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getLeavestatus() {
        return leavestatus;
    }

    public void setLeavestatus(String leavestatus) {
        this.leavestatus = leavestatus;
    }

    public String getreason() {
        return reason;
    }
    public void setreason(String reason) {
        this.reason = reason;
    }

    public LeaveModel() {
        this.user_id = user_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.leavestatus = leavestatus;
        this.reason=reason;

    }
}

