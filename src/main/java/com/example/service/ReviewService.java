package com.example.service;

import com.example.entity.Review;

import java.util.List;

public interface ReviewService {
  void addReview(Review review) ;
  List<Review> getAllReviews();
  List<Review> getAllReviewsByRate(Long rate) ;
}
