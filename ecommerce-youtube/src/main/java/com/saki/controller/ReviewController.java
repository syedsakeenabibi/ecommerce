package com.saki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saki.exception.ProductException;
import com.saki.exception.UserException;
import com.saki.model.Review;
import com.saki.model.User;
import com.saki.request.ReviewRequest;
import com.saki.service.ReviewService;
import com.saki.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req,
                                               @RequestHeader("Authorization") String jwt)
            throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(req, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReview(@PathVariable Long productId)
            throws ProductException {

        List<Review> reviews = reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
