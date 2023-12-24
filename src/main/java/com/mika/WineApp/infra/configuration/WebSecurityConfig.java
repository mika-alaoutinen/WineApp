package com.mika.WineApp.infra.configuration;

import com.mika.WineApp.infra.security.JwtProvider;
import com.mika.WineApp.infra.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfig {

    private final AuthenticationEntryPoint unauthorizedHandler;
    private final JwtProvider jwtProvider;
    private final UserDetailsService service;

    @Value("${frontend.urls}")
    private List<String> allowedUrls;

    @Value("${spring.security.enabled}")
    private boolean securityEnabled;

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
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter tokenFilter) throws Exception {
        return securityEnabled
                ? enabledSecurityFilterChain(http, tokenFilter, unauthorizedHandler)
                : disabledSecurityFilterChain(http);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configure CORS to allow connections from the frontend client. If this configuration
     * is missing, communication between client and server is blocked and a CORS error is shown.
     *
     * @return FilterRegistrationBean.
     */
    @Bean
    CorsConfigurationSource corsConfig() {
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedUrls);
        config.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Use BCrypt as the password encoder.
     *
     * @return PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JwtTokenFilter extends OncePerRequestFilter. It is used to set user authentication token.
     *
     * @return token filter.
     */
    @Bean
    JwtTokenFilter tokenFilter() {
        return new JwtTokenFilter(jwtProvider, service);
    }

    //@formatter:off
    private static SecurityFilterChain enabledSecurityFilterChain(
            HttpSecurity http,
            JwtTokenFilter tokenFilter,
            AuthenticationEntryPoint unauthorizedHandler
    ) throws Exception {
        return http
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(config -> config.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    //@formatter:off
    private static SecurityFilterChain disabledSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }
}