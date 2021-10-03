package com.mika.WineApp.mappers;

import com.mika.WineApp.models.review.Review;
import com.mika.model.ReviewDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = WineMapper.class)
public interface ReviewMapper {

    Review toModel(ReviewDTO dto);

    ReviewDTO toDTO(Review review);
}
