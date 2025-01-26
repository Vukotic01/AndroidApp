package com.example.mobilneaplikacije.model;

import com.example.mobilneaplikacije.model.enums.UserRole;

import java.util.List;

public class EventOrganizer extends BaseUser {

    private List<Events> organizedEvents;
    // Additional fields specific to EventOrganizer if any


    public EventOrganizer(List<Events> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public EventOrganizer(String email, String password, String firstName, String lastName, String address, String phone, UserRole role, List<Events> organizedEvents) {
        super(email, password, firstName, lastName, address, phone, role);
        this.organizedEvents = organizedEvents;
    }

    public List<Events> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(List<Events> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }
}
