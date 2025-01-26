package com.example.mobilneaplikacije.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Rate implements Serializable {


    private String id;


    private Integer rating;

    private String comment;

    private Company company;

    private Events events;

    private EventOrganizer eventOrganizer;

    private boolean obrisano;

    private Timestamp timeAdded;


    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rate() {
        this.obrisano = false;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public boolean getObrisano() {
        return obrisano;
    }

    public void setObrisano(boolean obrisano) {
        this.obrisano = obrisano;
    }

    public Rate(Integer rating, String comment, Company company, EventOrganizer eventOrganizer) {
        this.rating = rating;
        this.comment = comment;
        this.company = company;
        this.eventOrganizer = eventOrganizer;
        this.obrisano=false;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public EventOrganizer getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(EventOrganizer eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }
}
