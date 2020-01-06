package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.ReviewServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService service;
    private static final String baseUrl = "/reviews";

    public ReviewController(ReviewRepository repository,
                            WineRepository wineRepository) {

        this.service = new ReviewServiceImpl(repository, wineRepository);
    }

// --- Find reviews ---
    @GetMapping(baseUrl)
    public List<Review> findAll() {
        return service.findAll();
    }

    @GetMapping(baseUrl + "/{id}")
    public Review findById(@PathVariable Long id) {
        return service
                .findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @GetMapping(baseUrl + "/wine/id/{wineId}")
    public List<Review> findByWineId(@PathVariable Long wineId) {
        return service.findByWineId(wineId);
    }

    @GetMapping(baseUrl + "/wine/name/{wineName}")
    public List<Review> findByWineName(@PathVariable String wineName) {
        return service.findByWineName(wineName);
    }

// --- Add, edit and delete ---
    @PostMapping(baseUrl + "/{wineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@PathVariable Long wineId, @RequestBody Review newReview) {
        return service.add(wineId, newReview);
    }

    @PutMapping(baseUrl + "/{id}")
    public Review edit(@PathVariable Long id, @RequestBody Review editedReview) {
        return service.edit(id, editedReview);
    }

    @DeleteMapping(baseUrl + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

// --- Additional functionality ---
    @GetMapping(baseUrl + "/count")
    public long count() {
        return service.count();
    }

    @GetMapping(baseUrl + "/search")
    public List<Review> search(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String[] dateRange,
            @RequestParam(required = false) Double[] ratingRange) {

        return service.search(author, dateRange, ratingRange);
    }

// --- Quick searches ---
    @GetMapping(baseUrl + "/search/newest")
    public List<Review> findNewest(@RequestParam(defaultValue = "10") int limit) {
        return service.findNewest(limit);
    }

    @GetMapping(baseUrl + "/search/best")
    public List<Review> findBest(@RequestParam(defaultValue = "10") int limit) {
        return service.findBestRated(limit);
    }

    @GetMapping(baseUrl + "/search/worst")
    public List<Review> findWorst(@RequestParam(defaultValue = "10") int limit) {
        return service.findWorstRated(limit);
    }
}