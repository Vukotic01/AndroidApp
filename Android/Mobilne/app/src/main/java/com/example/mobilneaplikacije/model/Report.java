package com.example.mobilneaplikacije.model;

public class Report {

    public enum RepStatus {
        APPROVED,
        CANCELED,
        HOLD
    }

    private String id;

    private String reason;
    private Rate rate;

    private RepStatus status;

    public Report(String reason, Rate rate, RepStatus status) {
        this.reason = reason;
        this.rate = rate;
        this.status = status;
    }

    public Report() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public RepStatus getStatus() {
        return status;
    }

    public void setStatus(RepStatus status) {
        this.status = status;
    }
}
