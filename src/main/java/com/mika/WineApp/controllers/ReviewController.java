package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.review.ReviewNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.ReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reviews API", description = "Contains CRUD operations and search functionality.")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewRepository repository,
                            WineRepository wineRepository) {

        this.service = new ReviewServiceImpl(repository, wineRepository);
    }

// --- Find reviews ---
    @Operation(summary = "Get all reviews", description = "Returns all reviews in descending order by date.")
    @GetMapping()
    public List<Review> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Get one review")
    @GetMapping("/{id}")
    public Review findById(@PathVariable Long id) {
        return service
                .findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Operation(summary = "Get reviews of a wine", description = "Get all reviews written of a particular wine, get wine by id.")
    @GetMapping("/wine/id/{wineId}")
    public List<Review> findByWineId(@PathVariable Long wineId) {
        return service.findByWineId(wineId);
    }

    @Operation(summary = "Get reviews of a wine", description = "Get all reviews written of a particular wine, get wine(s) by name.")
    @GetMapping("/wine/name/{wineName}")
    public List<Review> findByWineName(@PathVariable String wineName) {
        return service.findByWineName(wineName);
    }

// --- Add, edit and delete ---
    @Operation(summary = "Add new review")
    @PostMapping("/{wineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@PathVariable Long wineId, @RequestBody Review newReview) {
        return service.add(wineId, newReview);
    }

    @Operation(summary = "Edit review")
    @PutMapping("/{id}")
    public Review edit(@PathVariable Long id, @RequestBody Review editedReview) {
        return service.edit(id, editedReview);
    }

    @Operation(summary = "Delete review")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

// --- Additional functionality ---
    @Operation(summary = "Get review count", description = "Returns total count of reviews in the application as long.")
    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    @Operation(summary = "Search reviews", description = "Search for reviews based on their author, date and rating.")
    @GetMapping("/search")
    public List<Review> search(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String[] dateRange,
            @RequestParam(required = false) Double[] ratingRange) {

        return service.search(author, dateRange, ratingRange);
    }

// --- Quick searches ---
    @Operation(summary = "Get newest reviews", description = "Get newest reviews. Number of reviews can be given as request parameter, default amount is 10.")
    @GetMapping("/search/newest")
    public List<Review> findNewest(@RequestParam(defaultValue = "10") int limit) {
        return service.findNewest(limit);
    }

    @Operation(summary = "Get best reviews", description = "Get best rated reviews. Number of reviews can be given as request parameter, default amount is 10.")
    @GetMapping("/search/best")
    public List<Review> findBest(@RequestParam(defaultValue = "10") int limit) {
        return service.findBestRated(limit);
    }

    @Operation(summary = "Get worst reviews", description = "Get worst rated reviews. Number of reviews can be given as request parameter, default amount is 10.")
    @GetMapping("/search/worst")
    public List<Review> findWorst(@RequestParam(defaultValue = "10") int limit) {
        return service.findWorstRated(limit);
    }
}