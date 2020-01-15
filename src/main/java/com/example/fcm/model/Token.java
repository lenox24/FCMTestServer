package com.example.fcm.model;

import org.springframework.data.annotation.Id;

public class Token {
    private String token;
    private String type = "android";
    private String agree;

    public Token() {
        this.token = null;
    }

    public void setToken(String token, String type, String agree) {
        this.token = token;
        this.type = type;
        this.agree = agree;
    }

    public String getToken() {
        return token;
    }
}
