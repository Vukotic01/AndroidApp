package com.example.mobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event implements Parcelable {
    private Long id;
    private String eventName;
    private String date;       // String representation of LocalDate
    private String startTime;  // String representation of LocalTime
    private String endTime;    // String representation of LocalTime
    private String eventType;  // String representation of EventType


    public Event(Long id, String eventName, String date, String startTime, String endTime, String eventType) {
        this.id = id;
        this.eventName = eventName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
    }

    public Event() {
    }

    protected Event(Parcel in) {
        id = in.readLong();
        eventName = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        eventType = in.readString();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }
/*
    public void setDate(LocalDate date) {
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }*/

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(eventName);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(eventType);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
