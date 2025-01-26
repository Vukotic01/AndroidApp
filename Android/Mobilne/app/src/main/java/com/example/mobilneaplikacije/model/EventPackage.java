package com.example.mobilneaplikacije.model;

import java.util.List;

public class EventPackage {

    private String id;
    private String name;
    private String description;
    private double discount;
    private boolean visibility;
    private boolean availability;
    private String category;
    private List<Product> products;
    private List<Service> services;
    private String eventType;
    private double price;
    private List<String> images;
    private int reservationDeadlineMonths;
    private int cancellationDeadlineDays;
    private boolean manualAcceptance;

    public EventPackage() {
    }

    public EventPackage(String id, String name, String description, double discount, boolean visibility,
                   boolean availability, String category, List<Product> products,
                   List<Service> services, String eventType, double price, List<String> images,
                   int reservationDeadlineMonths, int cancellationDeadlineDays, boolean manualAcceptance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.visibility = visibility;
        this.availability = availability;
        this.category = category;
        this.products = products;
        this.services = services;
        this.eventType = eventType;
        this.price = price;
        this.images = images;
        this.reservationDeadlineMonths = reservationDeadlineMonths;
        this.cancellationDeadlineDays = cancellationDeadlineDays;
        this.manualAcceptance = manualAcceptance;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getReservationDeadlineMonths() {
        return reservationDeadlineMonths;
    }

    public void setReservationDeadlineMonths(int reservationDeadlineMonths) {
        this.reservationDeadlineMonths = reservationDeadlineMonths;
    }

    public int getCancellationDeadlineDays() {
        return cancellationDeadlineDays;
    }

    public void setCancellationDeadlineDays(int cancellationDeadlineDays) {
        this.cancellationDeadlineDays = cancellationDeadlineDays;
    }

    public boolean isManualAcceptance() {
        return manualAcceptance;
    }

    public void setManualAcceptance(boolean manualAcceptance) {
        this.manualAcceptance = manualAcceptance;
    }
}
