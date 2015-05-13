package com.azakordonets.web.entities;

import fabricator.Fabricator;

class ResetToken {

    private final String token;
    private final long timeStamp = System.currentTimeMillis();

    public ResetToken(){
        this.token = Fabricator.alphaNumeric().hash(60);
    }

    public String getToken() {
        return this.token;
    }

    public boolean isTokenStillValid() {
        return System.currentTimeMillis() - timeStamp <= 60000;
    }
}
