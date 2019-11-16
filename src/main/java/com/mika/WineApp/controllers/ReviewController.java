package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.hateoas.ReviewModelAssembler;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {
    private final ReviewService service;
    private final ReviewModelAssembler assembler;
    private static final String baseUrl = "/reviews";

    public ReviewController(ReviewRepository repository,
                            WineRepository wineRepository,
                            ReviewModelAssembler assembler) {

        this.service = new ReviewService(repository, wineRepository);
        this.assembler = assembler;
    }

// --- Find based on review ---
    @GetMapping(baseUrl)
    public CollectionModel<EntityModel<Review>> findAll() {
        var reviews = service.findAll();
        return assembler.buildResponse(reviews);
    }

    @GetMapping(baseUrl + "/{id}")
    public EntityModel<Review> findById(@PathVariable Long id) {
        Review review = service.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        return assembler.toModel(review);
    }

    @GetMapping(baseUrl + "/author/{author}")
    public CollectionModel<EntityModel<Review>> findByAuthor(@PathVariable String author) {
        var reviews = service.findByAuthor(author);
        return assembler.buildResponse(reviews);
    }

    @GetMapping(baseUrl + "/date")
    public CollectionModel<EntityModel<Review>> findByDate(
            @RequestParam(name = "start", defaultValue = "2010-01-01") String start,
            @RequestParam(name = "end", defaultValue = "today") String end) {

        var reviews = service.findByDate(start, end);
        return assembler.buildResponse(reviews);
    }

    @GetMapping(baseUrl + "/rating")
    public CollectionModel<EntityModel<Review>> findByRating(
            @RequestParam(name = "minRating", defaultValue = "0") double minRating,
            @RequestParam(name = "maxRating", defaultValue = "5.0") double maxRating) {

        var reviews = service.findByRating(minRating, maxRating);
        return assembler.buildResponse(reviews);
    }

// --- Find based on wine ---
    @GetMapping(baseUrl + "/wineId/{wineId}")
    public CollectionModel<EntityModel<Review>> findByWineId(@PathVariable Long wineId) {
        var reviews = service.findByWineId(wineId);
        return assembler.buildResponse(reviews);
    }

    @GetMapping(baseUrl + "/wineName/{wineName}")
    public CollectionModel<EntityModel<Review>> findByWineName(@PathVariable String wineName) {
        var reviews = service.findByWineName(wineName);
        return assembler.buildResponse(reviews);
    }

// --- Add, edit and delete ---
    @PostMapping(baseUrl + "/{wineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Review> add(@PathVariable Long wineId, @RequestBody Review newReview) {
        Review review = service.add(wineId, newReview);
        return assembler.toModel(review);
    }

    @PutMapping(baseUrl + "/{id}")
    public EntityModel<Review> edit(@RequestBody Review editedReview, @PathVariable Long id) {
        Review review = service.edit(editedReview, id);
        return assembler.toModel(review);
    }

    @DeleteMapping(baseUrl + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}