package com.mika.WineApp.controllers;

import com.mika.WineApp.dtos.WineReviewDto;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WineReviewController {
    private final WineServiceImpl wineServiceImpl;

    public WineReviewController(WineRepository wineRepository) {
        this.wineServiceImpl = new WineServiceImpl(wineRepository);
    }

    @GetMapping("/winereviews")
    public List<WineReviewDto> findAll() {
        return mapToDto(wineServiceImpl.findAll());
    }

    @GetMapping("/winereviews/{wineName}")
    public List<WineReviewDto> findByName(String wineName) {
        return mapToDto(wineServiceImpl.findByName(wineName));
    }

    private List<WineReviewDto> mapToDto(List<Wine> wines) {
        return wines.stream()
                .map(wine -> new WineReviewDto(wine, wine.getReviews()))
                .collect(Collectors.toList());
    }
}
