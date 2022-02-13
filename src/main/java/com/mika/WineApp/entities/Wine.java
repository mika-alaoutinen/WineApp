package com.mika.WineApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "wines")
public class Wine implements EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private WineType type;

    @NotBlank
    private String country;

    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private double price;

    @Positive
    @Column(precision = 10, scale = 2)
    private double volume;

    @ElementCollection
    @CollectionTable(name = "wine_descriptions", joinColumns = @JoinColumn(name = "id"))
    private List<@NotBlank String> description = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "wine_food_pairings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "food_pairings")
    private List<@NotBlank String> foodPairings = new ArrayList<>();

    private String url;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wine")
    @ToString.Exclude
    private List<@NotNull Review> reviews = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Wine wine = (Wine) o;
        return id != null && Objects.equals(id, wine.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}