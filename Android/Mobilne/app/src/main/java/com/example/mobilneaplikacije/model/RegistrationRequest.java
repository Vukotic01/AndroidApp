package com.example.mobilneaplikacije.model;

import com.example.mobilneaplikacije.model.enums.RequestStatus;

import java.util.Map;

public class RegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String companyName;
    private String companyEmail;
    private String companyAddress;
    private String companyPhone;
    private String companyDescription;
    private Map<String, String> workingHours; // Map of working hours for each day of the week as Strings
    private RequestStatus status; // pending, approved, rejected

    public RegistrationRequest() {
    }



    public RegistrationRequest(String email, String password, String firstName, String lastName, String address,
                               String phone, String companyName, String companyEmail, String companyAddress,
                               String companyPhone, String companyDescription, Map<String, String> workingHours) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.companyDescription = companyDescription;
        this.workingHours = workingHours;
        this.status = RequestStatus.PENDING; // Default status
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public Map<String, String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Map<String, String> workingHours) {
        this.workingHours = workingHours;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }




}
