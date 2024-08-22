package com.saki.service;

import java.util.List;

import com.saki.exception.ProductException;
import com.saki.model.Review;
import com.saki.model.User;
import com.saki.request.ReviewRequest;

public interface ReviewService {

    // Corrected method name
    Review createReview(ReviewRequest req, User user) throws ProductException;

    List<Review> getAllReview(Long productId);
}
