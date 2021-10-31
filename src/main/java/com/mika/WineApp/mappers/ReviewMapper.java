package com.mika.WineApp.mappers;

import com.mika.WineApp.models.review.Review;
import com.mika.model.NewReviewDTO;
import com.mika.model.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = WineMapper.class)
public interface ReviewMapper {

    @Mapping(ignore = true, target = "user")
    Review toModel(ReviewDTO dto);

    ReviewDTO toDTO(Review review);

    Review toModel(NewReviewDTO newDto);
}
