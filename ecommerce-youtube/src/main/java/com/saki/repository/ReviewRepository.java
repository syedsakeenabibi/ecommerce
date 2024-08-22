package com.saki.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saki.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    List<Review> findByProductId(@Param("productId") Long productId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Review r WHERE r.rating = :rating")
    List<Review> findByRating(@Param("rating") int rating);
}
