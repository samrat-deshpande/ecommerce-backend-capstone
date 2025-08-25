package com.backend.ecommerce.event;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Event sent when a new user registers
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegistrationEvent extends UserEvent {
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    
    // Default constructor
    public UserRegistrationEvent() {
        super("USER_REGISTERED", null, null);
    }
    
    // Constructor with user details
    public UserRegistrationEvent(String userId, String userEmail, String firstName, String lastName, String phoneNumber, String role) {
        super("USER_REGISTERED", userId, userEmail);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
    
    // Getters and Setters
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
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "UserRegistrationEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", eventType='" + getEventType() + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", userEmail='" + getUserEmail() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
