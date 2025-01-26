package com.example.mobilneaplikacije.model;

import java.io.Serializable;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Events implements Serializable {

    public enum State {
        REALIZED,
        CANCELED,
        HOLD
    }

    private String id;

    private String eventType;
    private String name;
    private String description;
    private int maxParticipants;
    private String location;
    private String date;
    private boolean isPrivate;
    private Company company;



    private Timestamp timeDone;
    private State state;
   // @PropertyName("isRated")
    private boolean ocenjen;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(Timestamp timeDone) {
        this.timeDone = timeDone;
    }

    public Events() {
        // Prazan konstruktor neophodan za Firebase
        this.ocenjen=false;
    }

    public Events(String eventType, String name, String description, int maxParticipants, String location, String date, boolean isPrivate) {
        this.eventType = eventType;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.date = date;
        this.isPrivate = isPrivate;
        this.state = State.HOLD;
        this.company = new Company();
        this.ocenjen=false;
    }

    public boolean getOcenjen() {
        return ocenjen;
    }

    public void setOcenjen(boolean ocenjen) {
        this.ocenjen = ocenjen;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", isPrivate=" + isPrivate +
                ", company=" + company +
                ", state=" + state +
                '}';
    }
}