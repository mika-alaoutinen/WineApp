package com.mika.WineApp.hateoas;

import com.mika.WineApp.controllers.WineController;
import com.mika.WineApp.models.Wine;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Builds a HATEOAS compliant collection of wine entity models.
     * @param wines as a list.
     * @return CollectionModel of Wine entity models.
     */
    public CollectionModel<EntityModel<Wine>> buildResponse(List<Wine> wines) {
        var models = wines.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(models,
                linkTo(methodOn(WineController.class).findAll()).withSelfRel());
    }

    public ResponseEntity<?> addLinks(EntityModel<Wine> wineModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(WineController.class)
                .slash(wineModel.getContent())
                .toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}