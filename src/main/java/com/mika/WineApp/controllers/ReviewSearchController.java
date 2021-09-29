package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.ReviewMapper;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.services.ReviewService;
import com.mika.api.ReviewsSearchApi;
import com.mika.model.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewSearchController implements ReviewsSearchApi {
    private final ReviewMapper mapper;
    private final ReviewService service;

    @Override
    public ResponseEntity<List<ReviewDTO>> reviewSearch(String author, List<String> dateRange, List<Double> ratingRange) {
        var reviews = service.search(author, dateRange, ratingRange);
        return mapResponse(reviews);
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> searchBestReview(Integer limit) {
        var reviews = service.findBestRated(limit);
        return mapResponse(reviews);
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> searchNewestReview(Integer limit) {
        var reviews = service.findNewest(limit);
        return mapResponse(reviews);
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> searchWorstReview(Integer limit) {
        var reviews = service.findWorstRated(limit);
        return mapResponse(reviews);
    }

    private ResponseEntity<List<ReviewDTO>> mapResponse(List<Review> reviews) {
        return ResponseEntity.ok(
                reviews.
                        stream()
                        .map(mapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
