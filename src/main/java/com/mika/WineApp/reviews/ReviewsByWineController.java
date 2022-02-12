package com.mika.WineApp.reviews;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.mappers.ReviewMapper;
import com.mika.WineApp.services.ReviewService;
import com.mika.api.ReviewsByWineApi;
import com.mika.model.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
class ReviewsByWineController implements ReviewsByWineApi {
    private final ReviewMapper mapper;
    private final ReviewService service;

    @Override
    public ResponseEntity<List<ReviewDTO>> findReviewByWineId(Long wineId) {
        return mapResponse(service.findByWineId(wineId));
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> findReviewByWineName(String wineName) {
        return mapResponse(service.findByWineName(wineName));
    }

    private ResponseEntity<List<ReviewDTO>> mapResponse(List<Review> reviews) {
        return ResponseEntity.ok(reviews
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList()));
    }
}
