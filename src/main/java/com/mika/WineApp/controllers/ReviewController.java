package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService service;
    private static final String baseUrl = "/reviews";

    public ReviewController(ReviewRepository repository, WineRepository wineRepository) {
        this.service = new ReviewService(repository, wineRepository);
    }

    @GetMapping(baseUrl)
    List<Review> findAll() {
        return service.findAll();
    }

    @GetMapping(baseUrl + "/{id}")
    Review find(@PathVariable Long id) {
        return service.find(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

// Find based on wine
    @GetMapping("/reviews/wineId/{wineId}")
    List<Review> findByWineId(@PathVariable Long wineId) {
        return service.findByWineId(wineId);
    }

    @GetMapping("/reviews/wineName/{wineName}")
    List<Review> findByWineId(@PathVariable String wineName) {
        return service.findByWineName(wineName);
    }

// Add, edit and delete
    @PostMapping(baseUrl)
    public Review add(@RequestBody Review newReview) {
        return service.add(newReview);
    }

    @PostMapping("/reviews/{wineId}")
    Review add(@PathVariable Long wineId, @RequestBody Review newReview) {
        return service.add(wineId, newReview);
    }

    @PutMapping("reviews/{wineId}")
    Review edit(@RequestBody Review editedReview, @PathVariable Long id) {
        return service.edit(editedReview, id);
    }

    @DeleteMapping("reviews/{wineId}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
