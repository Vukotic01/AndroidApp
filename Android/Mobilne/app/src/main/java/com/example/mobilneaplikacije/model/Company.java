package com.example.mobilneaplikacije.model;

import java.util.List;
import java.util.Map;

public class Company {
    private String email;
    private String name;
    private String address;
    private String phone;
    private String description;
    private String ownerId;
    private List<Worker> workers;
    private Map<String, String> workingHours; // Map of working hours for each day of the week
    private List<Category> categories; // List of service and product categories
    private List<EventType> eventTypes; // List of event types

    public Company() {
    }

    public Company(String email, String name, String address, String phone, String description, String ownerId, List<Worker> workers, Map<String, String> workingHours, List<Category> categories, List<EventType> eventTypes) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.ownerId = ownerId;
        this.workers = workers;
        this.workingHours = workingHours;
        this.categories = categories;
        this.eventTypes = eventTypes;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public Map<String, String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Map<String, String> workingHours) {
        this.workingHours = workingHours;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
