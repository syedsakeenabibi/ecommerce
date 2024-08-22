package com.saki.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saki.exception.ProductException;
import com.saki.model.Product;
import com.saki.model.Review;
import com.saki.model.User;
import com.saki.repository.ProductRepository;
import com.saki.repository.ReviewRepository;
import com.saki.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewServiceImplementation(ReviewRepository reviewRepository,
                                       ProductService productService,
                                       ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        // Retrieve product using ProductService
        Product product = productService.findProductById(req.getProductId());
        if (product == null) {
            throw new ProductException("Product not found with ID: " + req.getProductId());
        }

        // Create and save review
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
