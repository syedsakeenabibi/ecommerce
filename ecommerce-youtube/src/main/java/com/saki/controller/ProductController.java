package com.saki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saki.exception.ProductException;
import com.saki.model.Product;
import com.saki.service.ProductService;


@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false) List<String> size,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {

        Page<Product> res = productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort,
                stock, pageNumber, pageSize);
        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {

        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }
}
