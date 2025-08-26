package com.backend.ecommerce.event;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Event sent when a user logs in
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginEvent extends UserEvent {
    
    private String loginMethod;
    private String userAgent;
    private String ipAddress;
    private boolean successful;
    
    // Default constructor
    public UserLoginEvent() {
        super("USER_LOGIN", null, null);
    }
    
    // Constructor with login details
    public UserLoginEvent(String userId, String userEmail, String loginMethod, String userAgent, String ipAddress, boolean successful) {
        super("USER_LOGIN", userId, userEmail);
        this.loginMethod = loginMethod;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.successful = successful;
    }
    
    // Getters and Setters
    public String getLoginMethod() {
        return loginMethod;
    }
    
    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public boolean isSuccessful() {
        return successful;
    }
    
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
    
    @Override
    public String toString() {
        return "UserLoginEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", eventType='" + getEventType() + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", userEmail='" + getUserEmail() + '\'' +
                ", loginMethod='" + loginMethod + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", successful=" + successful +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
