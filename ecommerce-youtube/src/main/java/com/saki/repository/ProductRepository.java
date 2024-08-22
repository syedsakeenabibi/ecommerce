package com.saki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import com.saki.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
           "JOIN p.categories c " +
           "WHERE c.name = :category")
    List<Product> findByCategoryName(@Param("category") String category);

    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.categories c " +
           "WHERE (:category IS NULL OR c.name = :category) " +
           "AND (:minPrice IS NULL OR p.discountedPrice >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) " +
           "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount) " +
           "AND (:color IS NULL OR p.color = :color) " +  // Added color filter
           "ORDER BY " +
           "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
           "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
    List<Product> filterProducts(@Param("category") @Nullable String category,
                                 @Param("minPrice") @Nullable Integer minPrice,
                                 @Param("maxPrice") @Nullable Integer maxPrice,
                                 @Param("minDiscount") @Nullable Integer minDiscount,
                                 @Param("color") @Nullable String color,  // Added color parameter
                                 @Param("sort") @Nullable String sort);
}
