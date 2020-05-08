package com.mika.WineApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mika.WineApp.models.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * A super class for all persisted entities (at the moment Wine and Review).
 * Contains the ID attribute.
 */
@Data
@MappedSuperclass
public abstract class EntityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // User who added the wine or review
}