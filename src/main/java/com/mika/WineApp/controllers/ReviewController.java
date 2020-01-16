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
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewRepository repository,
                            WineRepository wineRepository) {

        this.service = new ReviewServiceImpl(repository, wineRepository);
    }

// --- Find reviews ---
    @GetMapping("/")
    public List<Review> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Review findById(@PathVariable Long id) {
        return service
                .findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @GetMapping("/wine/id/{wineId}")
    public List<Review> findByWineId(@PathVariable Long wineId) {
        return service.findByWineId(wineId);
    }

    @GetMapping("/wine/name/{wineName}")
    public List<Review> findByWineName(@PathVariable String wineName) {
        return service.findByWineName(wineName);
    }

// --- Add, edit and delete ---
    @PostMapping("/{wineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@PathVariable Long wineId, @RequestBody Review newReview) {
        return service.add(wineId, newReview);
    }

    @PutMapping("/{id}")
    public Review edit(@PathVariable Long id, @RequestBody Review editedReview) {
        return service.edit(id, editedReview);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

// --- Additional functionality ---
    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    @GetMapping("/search")
    public List<Review> search(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String[] dateRange,
            @RequestParam(required = false) Double[] ratingRange) {

        return service.search(author, dateRange, ratingRange);
    }

// --- Quick searches ---
    @GetMapping("/search/newest")
    public List<Review> findNewest(@RequestParam(defaultValue = "10") int limit) {
        return service.findNewest(limit);
    }

    @GetMapping("/search/best")
    public List<Review> findBest(@RequestParam(defaultValue = "10") int limit) {
        return service.findBestRated(limit);
    }

    @GetMapping("/search/worst")
    public List<Review> findWorst(@RequestParam(defaultValue = "10") int limit) {
        return service.findWorstRated(limit);
    }
}