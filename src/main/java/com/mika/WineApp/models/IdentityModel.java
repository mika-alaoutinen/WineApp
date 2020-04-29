package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class IdentityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
}
