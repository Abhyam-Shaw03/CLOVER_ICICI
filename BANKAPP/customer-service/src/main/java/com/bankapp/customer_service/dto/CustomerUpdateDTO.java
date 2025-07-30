package com.bankapp.customer_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerUpdateDTO {
    private String address;

    private String city;

    private String state;

    @Pattern(regexp = "^\\d{6}$", message = "ZIP Code must be 6 digits")
    private String zipCode;

    private String altPhoneNumber;

    private String occupation;

    @PositiveOrZero(message = "Income must be positive or zero")
    private double annualIncome;

    private boolean isActive;

    public CustomerUpdateDTO() {
    }

    public CustomerUpdateDTO(String address, String city, String state, String zipCode, String altPhoneNumber, String occupation, double annualIncome, boolean isActive) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.altPhoneNumber = altPhoneNumber;
        this.occupation = occupation;
        this.annualIncome = annualIncome;
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAltPhoneNumber() {
        return altPhoneNumber;
    }

    public void setAltPhoneNumber(String altPhoneNumber) {
        this.altPhoneNumber = altPhoneNumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "CustomerUpdateDTO{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", altPhoneNumber='" + altPhoneNumber + '\'' +
                ", occupation='" + occupation + '\'' +
                ", annualIncome=" + annualIncome +
                ", isActive=" + isActive +
                '}';
    }
}
