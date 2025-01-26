package com.example.mobilneaplikacije.model;

import com.example.mobilneaplikacije.model.enums.EventStatus;

import java.util.List;

public class EventType {
    private String name;
    private String description;
    private List<SubCategory> suggestedSubcategories;
    private EventStatus status;

    public EventType() {
    }

    public EventType(String name, String description, List<SubCategory> suggestedSubcategories, EventStatus status) {
        this.name = name;
        this.description = description;
        this.suggestedSubcategories = suggestedSubcategories;
        this.status = status;
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

    public List<SubCategory> getSuggestedSubcategories() {
        return suggestedSubcategories;
    }

    public void setSuggestedSubcategories(List<SubCategory> suggestedSubcategories) {
        this.suggestedSubcategories = suggestedSubcategories;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
