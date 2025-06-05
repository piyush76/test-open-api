package com.userops.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AdminRole {
    ADMIN("Admin"),
    GRANT_ADMIN("Grant Admin");

    private final String value;

    AdminRole(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AdminRole fromValue(String value) {
        for (AdminRole role : AdminRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown admin role: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
