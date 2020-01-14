package com.example.fcm.model;

import org.springframework.data.annotation.Id;

public class Token {
    private String token;
    private String type = "android";

    public Token() {
        this.token = null;
    }

    public void setToken(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }
}
