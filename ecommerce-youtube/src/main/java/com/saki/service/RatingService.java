package com.saki.service;

import java.util.List;
import com.saki.exception.ProductException;
import com.saki.model.Rating;
import com.saki.model.User;
import com.saki.request.RatingRequest;

public interface RatingService {
    Rating createRating(RatingRequest req, User user) throws ProductException;
    List<Rating> getProductsRating(Long productId);
}
