package com.mika.WineApp.hateoas;

import com.mika.WineApp.controllers.WineController;
import com.mika.WineApp.models.Wine;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WineModelAssembler implements RepresentationModelAssembler<Wine, EntityModel<Wine>> {

    /**
     * Builds a HATEOAS compliant Wine entity model (i.e. resource).
     * @param wine
     * @return EntityModel with links to self and all wines.
     */
    @Override
    public EntityModel<Wine> toModel(Wine wine) {
        return new EntityModel<>(wine,
                linkTo(methodOn(WineController.class).findById(wine.getId())).withSelfRel(),
                linkTo(methodOn(WineController.class).findAll()).withRel("wines"));
    }
}