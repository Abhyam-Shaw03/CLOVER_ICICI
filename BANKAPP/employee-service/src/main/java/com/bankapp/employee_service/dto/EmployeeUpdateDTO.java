package com.bankapp.employee_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class EmployeeUpdateDTO {
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Pattern(regexp = "\\d{10}", message = "Alternate phone must be 10 digits")
    private String altPhoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    private String branchId;
    private String department;

    @PositiveOrZero(message = "Salary must be positive")
    private Double salary;

    private String address;
    private String city;
    private String state;

    @Pattern(regexp = "^\\d{6}$", message = "ZIP Code must be 6 digits")
    private String zipCode;

    private Boolean isActive;

    public EmployeeUpdateDTO() {
    }

    public EmployeeUpdateDTO(String phoneNumber, String altPhoneNumber, String email, String branchId, String department, Double salary, String address, String city, String state, String zipCode, Boolean isActive) {
        this.phoneNumber = phoneNumber;
        this.altPhoneNumber = altPhoneNumber;
        this.email = email;
        this.branchId = branchId;
        this.department = department;
        this.salary = salary;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.isActive = isActive;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAltPhoneNumber() {
        return altPhoneNumber;
    }

    public void setAltPhoneNumber(String altPhoneNumber) {
        this.altPhoneNumber = altPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "EmployeeUpdateDTO{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", altPhoneNumber='" + altPhoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", branchId='" + branchId + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
