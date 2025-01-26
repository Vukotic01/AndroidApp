package com.example.mobilneaplikacije.model;

import com.example.mobilneaplikacije.model.enums.SubCategoryType;

public class SubCategory {
    private Category category;
    private String name;
    private String description;
    private String subcategoryType;

    public SubCategory() {
    }

    public SubCategory(Category category, String name, String description, String subcategoryType) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.subcategoryType = subcategoryType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getSubcategoryType() {
        return subcategoryType;
    }

    public void setSubcategoryType(String subcategoryType) {
        this.subcategoryType = subcategoryType;
    }
}
