package com.ecommerce.order_service.dto;

public class AdminResult {
    private Long totalUser;
    private Long pendingUser;
    private Long orderedUser;
    private Long attemptDepletedProductUser;

    // No-argument constructor
    public AdminResult() {
    }

    // Constructor with arguments
    public AdminResult(Long totalUser, Long pendingUser, Long orderedUser, Long attemptDepletedProductUser) {
        this.totalUser = totalUser;
        this.pendingUser = pendingUser;
        this.orderedUser = orderedUser;
        this.attemptDepletedProductUser = attemptDepletedProductUser;
    }

    // Getter and Setter methods
    public Long getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Long totalUser) {
        this.totalUser = totalUser;
    }

    public Long getPendingUser() {
        return pendingUser;
    }

    public void setPendingUser(Long pendingUser) {
        this.pendingUser = pendingUser;
    }

    public Long getOrderedUser() {
        return orderedUser;
    }

    public void setOrderedUser(Long orderedUser) {
        this.orderedUser = orderedUser;
    }

    public Long getAttemptDepletedProductUser() {
        return attemptDepletedProductUser;
    }

    public void setAttemptDepletedProductUser(Long attemptDepletedProductUser) {
        this.attemptDepletedProductUser = attemptDepletedProductUser;
    }
}
