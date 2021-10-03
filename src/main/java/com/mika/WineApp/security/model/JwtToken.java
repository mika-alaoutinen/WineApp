package com.mika.WineApp.security.model;

import lombok.Getter;

@Getter
public class JwtToken {
    private final String token;
    private final String type = "Bearer";

    public JwtToken(String token) {
        this.token = token;
    }
}
