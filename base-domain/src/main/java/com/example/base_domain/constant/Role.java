package com.example.base_domain.constant;

public enum Role {
    USER, ADMIN;

    public String getRolePrefix() {
        return "ROLE_" + this.name();
    }
}
