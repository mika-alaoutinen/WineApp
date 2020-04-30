package com.mika.WineApp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * The web security config is used to disable Spring CSRF.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("#{'${frontend.urls}'.split(',')}")
    private List<String> allowedUrls;

//    private final UserService service;
//
//    public WebSecurityConfig(UserAccountRepository repository) {
//        this.service = new UserServiceImpl(repository);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//            .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//            .formLogin()
//                .permitAll()
//                .and()
//            .logout()
//                .permitAll()
//                .logoutSuccessUrl("/")
//                .and()
            .csrf()
                .disable();
    }

//    @Autowired
//    public void configure(AuthenticationManagerBuilder authentication) throws Exception {
//        authentication
//                .userDetailsService(service)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }

    /**
     * Configure CORS to allow connections from the frontend client. If this configuration
     * is missing, communication between client and server is blocked and a CORS error is shown.
     * @return FilterRegistrationBean.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedUrls);
        config.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        var bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}