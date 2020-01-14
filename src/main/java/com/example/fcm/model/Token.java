package com.example.fcm.model;

import org.springframework.data.annotation.Id;

public class Token {
    @Id
    private String token;

    public Token(String token) {
        this.token = token;
    }
}
