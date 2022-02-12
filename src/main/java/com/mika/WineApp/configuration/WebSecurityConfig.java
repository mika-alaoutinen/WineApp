package com.mika.WineApp.configuration;

import com.mika.WineApp.security.JwtTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

interface WebSecurityConfig {

    /**
     * Overrides the default implementation in WebSecurityConfigurerAdapter.
     * Used to disable CSRF. Additionally, if Spring Security is enabled in application.properties,
     * configures authorization rules.
     * <p>
     * Authorization rules:
     * - allow post requests to /auth for login and registration
     * - allow all GET requests without logging in
     * - block POST, PUT and DELETE requests from users who are not logged in
     * - Only admins can change user's roles.
     *
     * @param http HttpSecurity
     * @throws Exception e
     */
    void configure(HttpSecurity http) throws Exception;

    /**
     * Configures the Spring AuthenticationManager with custom UserDetailsService implementation
     * and sets the password encoder to BCrypt.
     *
     * @param authBuilder AuthenticationManagerBuilder
     * @throws Exception e
     */
    void configure(AuthenticationManagerBuilder authBuilder) throws Exception;

    /**
     * Creates the AuthenticationManagerBean used for user authentication.
     *
     * @return authenticationManagerBean
     * @throws Exception e
     */
    AuthenticationManager authenticationManagerBean() throws Exception;

    /**
     * Configure CORS to allow connections from the frontend client. If this configuration
     * is missing, communication between client and server is blocked and a CORS error is shown.
     *
     * @return FilterRegistrationBean.
     */
    FilterRegistrationBean<CorsFilter> corsFilter();

    /**
     * JwtTokenFilter extends OncePerRequestFilter. It is used to set user authentication token.
     *
     * @return token filter.
     */
    JwtTokenFilter tokenFilter();

    /**
     * Use BCrypt as the password encoder.
     *
     * @return PasswordEncoder
     */
    PasswordEncoder passwordEncoder();
}
