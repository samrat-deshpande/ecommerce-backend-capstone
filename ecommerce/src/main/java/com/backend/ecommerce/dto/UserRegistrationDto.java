package com.backend.ecommerce.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for user registration
 * Provides structured data validation and transfer
 */
public class UserRegistrationDto implements Serializable {
    
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    // Default constructor
    public UserRegistrationDto() {}
    
    // Constructor with all fields
    public UserRegistrationDto(String email, String password, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
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
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Validate the DTO data
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return email != null && !email.trim().isEmpty() &&
               password != null && password.length() >= 6 &&
               firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty();
    }
    
    /**
     * Get validation error message
     * @return Error message if invalid, null if valid
     */
    public String getValidationError() {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            return "First name is required";
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return "Last name is required";
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
