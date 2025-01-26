package com.example.mobilneaplikacije.model;

import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Map;

public class Service {
    private String category;
    private String subcategory;
    private String name;
    private String description;
    private String specifics;
    private double price;
    private double discount;
    private List<String> images;
    private List<String> eventTypes;
    private boolean visibility;
    private boolean availability;
    private List<String> employees;
    private String duration;
    private String minEngagement;
    private String maxEngagement;
    private int reservationLeadTime;
    private int cancellationLeadTime;
    private String confirmationMethod;
    private String status;

    public Service() {
    }

    public Service(String category, String subcategory, String name, String description, String specifics, double price, double discount, List<String> images, List<String> eventTypes, boolean visibility, boolean availability, List<String> employees, String duration, String minEngagement, String maxEngagement, int reservationLeadTime, int cancellationLeadTime, String confirmationMethod, String status) {
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.specifics = specifics;
        this.price = price;
        this.discount = discount;
        this.images = images;
        this.eventTypes = eventTypes;
        this.visibility = visibility;
        this.availability = availability;
        this.employees = employees;
        this.duration = duration;
        this.minEngagement = minEngagement;
        this.maxEngagement = maxEngagement;
        this.reservationLeadTime = reservationLeadTime;
        this.cancellationLeadTime = cancellationLeadTime;
        this.confirmationMethod = confirmationMethod;
        this.status = status;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public List<String> getEmployees() {
        return employees;
    }

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMinEngagement() {
        return minEngagement;
    }

    public void setMinEngagement(String minEngagement) {
        this.minEngagement = minEngagement;
    }

    public String getMaxEngagement() {
        return maxEngagement;
    }

    public void setMaxEngagement(String maxEngagement) {
        this.maxEngagement = maxEngagement;
    }

    public int getReservationLeadTime() {
        return reservationLeadTime;
    }

    public void setReservationLeadTime(int reservationLeadTime) {
        this.reservationLeadTime = reservationLeadTime;
    }

    public int getCancellationLeadTime() {
        return cancellationLeadTime;
    }

    public void setCancellationLeadTime(int cancellationLeadTime) {
        this.cancellationLeadTime = cancellationLeadTime;
    }

    public String getConfirmationMethod() {
        return confirmationMethod;
    }

    public void setConfirmationMethod(String confirmationMethod) {
        this.confirmationMethod = confirmationMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
