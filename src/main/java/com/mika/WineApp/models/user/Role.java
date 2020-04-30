package com.mika.WineApp.models.user;

import com.mika.WineApp.models.superclasses.IdentityModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role extends IdentityModel {

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
