package com.example.mobilneaplikacije.model;

import java.util.List;

public class Product {
    private String category;
    private String subCategory;
    private String subcategoryType;
    private String name;
    private String description;
    private double price;
    private double discount;
    private double priceWithDiscount;
    private List<String> images;
    private List<String> eventTypes;
    private boolean visibility; // if EventOrganizer can see it
    private boolean availability; // is it available for shopping

    public Product() {
    }

    public Product(String category, String subCategory, String subcategoryType, String name, String description, double price, double discount, double priceWithDiscount, List<String> images, List<String> eventTypes, boolean visibility, boolean availability) {
        this.category = category;
        this.subCategory = subCategory;
        this.subcategoryType = subcategoryType;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.priceWithDiscount = priceWithDiscount;
        this.images = images;
        this.eventTypes = eventTypes;
        this.visibility = visibility;
        this.availability = availability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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

    public double getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(double priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
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

    public String getSubcategoryType() {
        return subcategoryType;
    }

    public void setSubcategoryType(String subcategoryType) {
        this.subcategoryType = subcategoryType;
    }
}
