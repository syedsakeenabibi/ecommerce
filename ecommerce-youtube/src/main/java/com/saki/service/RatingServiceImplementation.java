package com.saki.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saki.exception.ProductException;
import com.saki.model.Product;
import com.saki.model.Rating;
import com.saki.model.User;
import com.saki.repository.RatingRepository;
import com.saki.request.RatingRequest;

@Service
public class RatingServiceImplementation implements RatingService {

    private final RatingRepository ratingRepository;
    private final ProductService productService;

    public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
        Product product = productService.findProductById(req.getProductId());

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreatedAt(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return ratingRepository.getAllProductRating(productId);
    }
}
