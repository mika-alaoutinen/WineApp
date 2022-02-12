package com.mika.WineApp.controllers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@TestConfiguration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Disable Spring Security for unit tests
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/**");
    }
}
