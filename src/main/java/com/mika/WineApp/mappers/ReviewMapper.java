package com.mika.WineApp.mappers;

import com.mika.WineApp.reviews.model.Review;
import com.mika.model.NewReviewDTO;
import com.mika.model.ReviewDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = WineMapper.class)
public interface ReviewMapper {

    ReviewDTO toDTO(Review review);

    Review toModel(NewReviewDTO newDto);
}
