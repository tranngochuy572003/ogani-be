package com.example.mapper;

import com.example.dto.ReviewDto;
import com.example.entity.Review;

public class ReviewMapper {
  public static ReviewDto toDto(Review review) {
    ReviewDto reviewDto = new ReviewDto();
    reviewDto.setRate(review.getRate());
    return reviewDto;
  }

  public static Review toEntity(ReviewDto reviewDto) {
    Review review = new Review();
    review.setRate(reviewDto.getRate());
    return review;
  }
}
