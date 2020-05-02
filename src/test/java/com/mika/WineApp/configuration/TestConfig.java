package com.mika.WineApp.configuration;

import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        var users = createTestUsers();
        var userDetails = buildUserDetails(users);

        return new InMemoryUserDetailsManager(userDetails);
    }

    private List<User> createTestUsers() {
        return List.of(
                new User("user", "user_password", Set.of(Role.ROLE_USER)),
                new User("admin", "admin_password", Set.of(Role.ROLE_ADMIN))
        );
    }

    private List<UserDetails> buildUserDetails(List<User> users) {
        return users.stream()
                .map(UserPrincipal::build)
                .collect(Collectors.toList());
    }
}
