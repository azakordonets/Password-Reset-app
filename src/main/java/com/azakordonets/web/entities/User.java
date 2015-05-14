package com.azakordonets.web.entities;

public class User {
    private final String email;
    private String newPassword;
    private long resetPasswordTokenTs;

    public User(String email) {
        this.email = email;
        this.newPassword = "";
        this.resetPasswordTokenTs = System.currentTimeMillis();
    }

    public String getEmail() {
        return email;
    }

}
