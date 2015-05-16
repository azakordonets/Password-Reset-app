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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public long getResetPasswordTokenTs() {
        return resetPasswordTokenTs;
    }

    public void setResetPasswordTokenTs(long resetPasswordTokenTs) {
        this.resetPasswordTokenTs = resetPasswordTokenTs;
    }
}
