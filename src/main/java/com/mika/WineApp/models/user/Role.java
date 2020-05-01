package com.mika.WineApp.models.user;

import com.mika.WineApp.models.superclasses.IdentityModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role extends IdentityModel {

    @NaturalId
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public Role(String name) {
        try {
            this.name = RoleName.valueOf(name.toUpperCase());
        } catch (Exception e) {
            System.out.println("What exception is this: " + e.getMessage());
        }
    }
}
