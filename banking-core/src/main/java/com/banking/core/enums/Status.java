package com.banking.core.enums;

/**
 * General purpose status enumeration
 */
public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended"),
    CLOSED("Closed"),
    BLOCKED("Blocked");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
