package com.mika.WineApp.security.model;

import lombok.Data;

@Data
public class JwtToken {
    private String token;
    private String type = "Bearer";

    public JwtToken(String token) {
        this.token = token;
    }
}
