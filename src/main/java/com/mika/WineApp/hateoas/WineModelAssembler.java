package com.mika.WineApp.hateoas;

import com.mika.WineApp.controllers.WineController;
import com.mika.WineApp.models.Wine;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WineModelAssembler implements RepresentationModelAssembler<Wine, EntityModel<Wine>> {

    /**
     * Builds a HATEOAS compliant Wine entity model (i.e. resource).
     * @param wine model.
     * @return EntityModel with links to self and all wines.
     */
    @Override
    public EntityModel<Wine> toModel(Wine wine) {
        return new EntityModel<>(wine,
                linkTo(methodOn(WineController.class).findById(wine.getId())).withSelfRel(),
                linkTo(methodOn(WineController.class).findAll()).withRel("wines"));
    }

    /**
     * Builds a HTTP response.
     * @param wines as a list.
     * @return CollectionModel of Wine entity models.
     */
    public CollectionModel<EntityModel<Wine>> buildResponse(List<Wine> wines) {
        var models = wines.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return toCollectionModel(models);
    }

    /**
     * Builds a HATEOAS compliant collection of wine entity models.
     * @param wineModels list of Wine EntityModels.
     * @return CollectionModel of Wine EntityModels
     */
    private CollectionModel<EntityModel<Wine>> toCollectionModel(List<EntityModel<Wine>> wineModels) {
        return new CollectionModel<>(wineModels,
                linkTo(methodOn(WineController.class).findAll()).withSelfRel());
    }
}