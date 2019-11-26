package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService service;
    private static final String baseUrl = "/reviews";

    public ReviewController(ReviewRepository repository,
                            WineRepository wineRepository) {

        this.service = new ReviewService(repository, wineRepository);
    }

// --- Find based on review ---
    @GetMapping(baseUrl)
    public List<Review> findAll() {
        return service.findAll();
    }

    @GetMapping(baseUrl + "/author/{author}")
    public List<Review> findByAuthor(@PathVariable String author) {
        return service.findByAuthor(author);
    }

    @GetMapping(baseUrl + "/date")
    public List<Review> findByDate(
            @RequestParam(name = "start", defaultValue = "2010-01-01") String start,
            @RequestParam(name = "end", defaultValue = "today") String end) {

        return service.findByDate(start, end);
    }

    @GetMapping(baseUrl + "/rating")
    public List<Review> findByRating(
            @RequestParam(name = "minRating", defaultValue = "0") double minRating,
            @RequestParam(name = "maxRating", defaultValue = "5.0") double maxRating) {

        return service.findByRating(minRating, maxRating);
    }

    @GetMapping(baseUrl + "/newest")
    public List<Review> findNewest(@RequestParam(name = "limit", defaultValue = "25") int limit) {
        return service.findNewest(limit);
    }

    @GetMapping(baseUrl + "/{id}")
    public Review findById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

// --- Find based on wine ---
    @GetMapping(baseUrl + "/wineId/{wineId}")
    public List<Review> findByWineId(@PathVariable Long wineId) {
        return service.findByWineId(wineId);
    }

    @GetMapping(baseUrl + "/wineName/{wineName}")
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
    public Review edit(@RequestBody Review editedReview, @PathVariable Long id) {
        return service.edit(editedReview, id);
    }

    @DeleteMapping(baseUrl + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}