package com.example.service.impl;

import com.example.entity.Review;
import com.example.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
  @Override
  public void addReview(Review review) {

  }

  @Override
  public List<Review> getAllReviews() {
    return null;
  }

  @Override
  public List<Review> getAllReviewsByRate(Long rate) {
    return null;
  }
}
