package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewRepository repository) {
        this.service = new ReviewService(repository);
    }

    @GetMapping("/reviews")
    List<Review> findAll() {
        return service.findAll();
    }

    @GetMapping("reviews/{id}")
    Review find(@PathVariable Long id) {
        return service.find(id)
                      .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @PostMapping("/reviews")
    Review add(@RequestBody Review newReview) {
        return service.add(newReview);
    }

    @PutMapping("reviews/{id}")
    Review edit(@RequestBody Review editedReview, @PathVariable Long id) {
        return service.edit(editedReview, id);
    }

    @DeleteMapping("reviews/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
