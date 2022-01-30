package com.mika.WineApp.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = mapAuthorities(user);
    }

    private static List<SimpleGrantedAuthority> mapAuthorities(User user) {
        return user
                .getRoles()
                .stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
