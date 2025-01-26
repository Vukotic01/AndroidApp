package com.example.mobilneaplikacije.model;

import com.google.firebase.Timestamp;

public class Availability {
    private Timestamp date;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    public Availability() {}

    public Availability(Timestamp date, Timestamp startTime, Timestamp endTime, String status) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
