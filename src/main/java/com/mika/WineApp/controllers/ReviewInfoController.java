package com.mika.WineApp.controllers;

import com.mika.WineApp.services.ReviewService;
import com.mika.api.ReviewsInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewInfoController implements ReviewsInfoApi {
    private final ReviewService service;

    @Override
    public ResponseEntity<Integer> reviewCount() {
        return ResponseEntity.ok(service.count());
    }
}
