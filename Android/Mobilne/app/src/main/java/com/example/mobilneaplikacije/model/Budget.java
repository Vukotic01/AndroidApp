package com.example.mobilneaplikacije.model;

import java.util.ArrayList;
import java.util.List;

public class Budget {
    private String eventName;
    private List<Product> products;
    private List<Service> services;
    private double totalCost;

    public Budget() {
        this.products = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Service> getServices() {
        return services;
    }

    public Budget(String eventName, List<Product> products, List<Service> services) {
        this.eventName = eventName;
        this.products = products;
        this.services = services;
        this.totalCost = calculateTotalCost(products, services);
    }

    // Getteri i setteri
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products, List<Service> services) {
        this.products = products;
        this.totalCost = calculateTotalCost(products, services);
    }

    public void setServices(List<Service> services) {
        this.services = services;
        this.totalCost = calculateTotalCost(products, services);
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    private double calculateTotalCost(List<Product> products, List<Service> services) {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }

        for (Service service : services) {
            total += service.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "eventName='" + eventName + '\'' +
                ", products=" + products +
                ", products=" + services +
                ", totalCost=" + totalCost +
                '}';
    }
}
