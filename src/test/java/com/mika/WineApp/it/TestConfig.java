package com.mika.WineApp.it;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.stream.Collectors;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        var users = TestData.initTestUsers();
        var userDetails = buildUserDetails(users);

        return new InMemoryUserDetailsManager(userDetails);
    }

    private List<UserDetails> buildUserDetails(List<User> users) {
        return users
                .stream()
                .map(UserPrincipal::build)
                .collect(Collectors.toList());
    }
}
