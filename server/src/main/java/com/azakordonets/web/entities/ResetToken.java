package com.azakordonets.web.entities;

import fabricator.Fabricator;

public class ResetToken {

    private final String token;
    private final String email;
    private long timeStamp = System.currentTimeMillis();

    ResetToken(String email){
        this.email = email;
        this.token = Fabricator.alphaNumeric().hash(60);
    }

    public String getToken() {
        return this.token;
    }

    public boolean isTokenStillValid() {
        return System.currentTimeMillis() - timeStamp <= 60000;
    }
}
