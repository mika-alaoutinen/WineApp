package com.mika.WineApp.models.superclasses;

import lombok.Data;

import javax.persistence.*;

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
}