package com.example.mobilneaplikacije.model;

import com.example.mobilneaplikacije.model.enums.UserRole;

import java.util.Map;

public class Worker extends BaseUser {
    private Map<String, String> workingHours;
    private Company company;
    private String workerId; // Add this field

    public Worker() {
    }

    public Worker(String email, String password, String firstName, String lastName, String address, String phone, UserRole role) {
        super(email, password, firstName, lastName, address, phone, role);
    }

    public Worker(Map<String, String> workingHours, Company company, String workerId) {
        this.workingHours = workingHours;
        this.company = company;
        this.workerId = workerId;
    }

    public Worker(String email, String password, String firstName, String lastName, String address, String phone, UserRole role, Map<String, String> workingHours, Company company, String workerId) {
        super(email, password, firstName, lastName, address, phone, role);
        this.workingHours = workingHours;
        this.company = company;
        this.workerId = workerId;
    }

    public Map<String, String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Map<String, String> workingHours) {
        this.workingHours = workingHours;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }
}
