package com.mika.WineApp.models.user;

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
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Set.of();
        this.reviews = List.of();
    }
}
