package com.mika.WineApp.hateoas;

import com.mika.WineApp.controllers.ReviewController;
import com.mika.WineApp.models.Review;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReviewModelAssembler implements RepresentationModelAssembler<Review, EntityModel<Review>> {

    @Override
    public EntityModel<Review> toModel(Review review) {
        return new EntityModel<>(review,
                linkTo(methodOn(ReviewController.class).findById(review.getId())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).findAll()).withRel("reviews"));
    }

    /**
     * Builds a HATEOAS compliant collection of wine entity models.
     * @param reviews as a list.
     * @return CollectionModel of Wine entity models.
     */
    public CollectionModel<EntityModel<Review>> buildResponse(List<Review> reviews) {
        var models = reviews.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(models,
                linkTo(methodOn(ReviewController.class).findAll()).withSelfRel());
    }
}