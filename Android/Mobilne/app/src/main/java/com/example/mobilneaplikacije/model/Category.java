package com.example.mobilneaplikacije.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private String description;
    private List<SubCategory> subcategories;

    public Category() {
        this.subcategories = new ArrayList<>(); // Initialize as an empty list
    }



    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.subcategories = new ArrayList<>(); // Initialize as an empty list
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

    public List<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }
}
