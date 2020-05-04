package com.mika.WineApp.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.superclasses.IdentityModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "users")
public class User extends IdentityModel {

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = Set.of(Role.ROLE_USER);

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = List.of();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Set.of(Role.ROLE_USER);
        this.reviews = List.of();
    }
}
