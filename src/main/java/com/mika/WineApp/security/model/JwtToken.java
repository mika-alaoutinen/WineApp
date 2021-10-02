package com.mika.WineApp.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {
    private String token;
    private String type = "Bearer";

    public JwtToken(String token) {
        this.token = token;
    }
}
