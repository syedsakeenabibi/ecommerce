package com.saki.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.saki.exception.ProductException;
import com.saki.model.Product;
import com.saki.request.CreateProductRequest;

public interface ProductService {

    Product createProduct(CreateProductRequest req);

    String deleteProduct(Long productId) throws ProductException;

    String updateProduct(Long productId, CreateProductRequest req) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> findProductByCategory(String category);

    Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
                                Integer maxPrice, Integer minDiscount, String sort, String stock,
                                Integer pageNumber, Integer pageSize);
}
