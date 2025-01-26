package com.example.mobilneaplikacije.model;

import com.example.mobilneaplikacije.model.enums.UserRole;

import java.util.List;

public class Admin extends BaseUser{
    private List<RegistrationRequest> registrationRequests;

    public Admin(List<RegistrationRequest> registrationRequests) {
        this.registrationRequests = registrationRequests;
    }

    public Admin(String email, String password, String firstName, String lastName, String address, String phone, UserRole role, List<RegistrationRequest> registrationRequests) {
        super(email, password, firstName, lastName, address, phone, role);
        this.registrationRequests = registrationRequests;
    }

    public List<RegistrationRequest> getRegistrationRequests() {
        return registrationRequests;
    }

    public void setRegistrationRequests(List<RegistrationRequest> registrationRequests) {
        this.registrationRequests = registrationRequests;
    }
}
