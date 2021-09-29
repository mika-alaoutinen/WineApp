package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.ReviewMapper;
import com.mika.WineApp.services.ReviewService;
import com.mika.api.ReviewsCrudApi;
import com.mika.model.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewCrudController implements ReviewsCrudApi {
    private final ReviewMapper mapper;
    private final ReviewService service;

    @Override
    public ResponseEntity<ReviewDTO> addReview(Long wineId, ReviewDTO reviewDTO) {
        var newReview = service.add(wineId, mapper.toModel(reviewDTO));
        return ResponseEntity.ok(mapper.toDTO(newReview));
    }

    @Override
    public ResponseEntity<Void> deleteReview(Long id) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<ReviewDTO> findReview(Long id) {
        var review = service.findById(id);
        return ResponseEntity.ok(mapper.toDTO(review));
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getReviews() {
        var reviews = service
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviews);
    }

    @Override
    public ResponseEntity<Boolean> isReviewEditable(Long id) {
        return ResponseEntity.ok(service.isAllowedToEdit(id));
    }

    @Override
    public ResponseEntity<ReviewDTO> updateReview(Long id, ReviewDTO reviewDTO) {
        var edited = service.edit(id, mapper.toModel(reviewDTO));
        return ResponseEntity.ok(mapper.toDTO(edited));
    }
}