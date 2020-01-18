package com.example.niyanta.askforleave.Models;

public class EmployeeModel {
    private String name;
    private String from_date;
    private String to_date;
    private String reason;
    private String request_id;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public EmployeeModel(){

        this.name = name;
        this.from_date = from_date;
        this.to_date = to_date;
        this.reason = reason;
        this.request_id = request_id;

    }
}
