package com.saki.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saki.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.product.id = :productId")
    List<Rating> findRatingsByProductId(@Param("productId") Long productId);

    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId")
    List<Rating> findRatingsByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Rating r WHERE r.ratingValue BETWEEN :minRating AND :maxRating")
    List<Rating> findRatingsInRange(@Param("minRating") int minRating, @Param("maxRating") int maxRating);

	List<Rating> getAllProductRating(Long productId);
}
