package com.mika.WineApp.controllers;

import com.mika.WineApp.dtos.WineReviewDto;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.WineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class WineReviewController {
    private final ReviewService reviewService;
    private final WineService wineService;

    public WineReviewController(ReviewRepository reviewRepository, WineRepository wineRepository) {
        this.reviewService = new ReviewService(reviewRepository);
        this.wineService = new WineService(wineRepository);
    }

    @GetMapping("/winereviews")
    public List<WineReviewDto> findAll() {
        var reviews = reviewService.findAll();

        Map<Long, List<Wine>> groupByWineId = reviews.stream()
                .map(Review::getWine)
                .collect(Collectors.groupingBy(Wine::getId));

        System.out.println(groupByWineId.entrySet());

        return new ArrayList<>();
    }

    @GetMapping("/winereviews/{wineName}")
    public WineReviewDto findByName(String name) {
        var reviews = reviewService.findAllByWineName(name);
        Wine wine = reviews.get(0).getWine();
        return new WineReviewDto(wine, reviews);
    }

}
