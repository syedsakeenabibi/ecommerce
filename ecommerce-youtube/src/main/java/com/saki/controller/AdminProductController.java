package com.saki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saki.exception.ProductException;
import com.saki.model.Product;
import com.saki.request.CreateProductRequest;
import com.saki.service.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req) {
        Product product = productService.createProduct(req);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
        String result = productService.deleteProduct(productId);
        ApiResponse res = new ApiResponse();
        res.setMessage(result);
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> findAllProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) List<String> colors,
        @RequestParam(required = false) List<String> sizes,
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        @RequestParam(required = false) Integer minDiscount,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) String stock,
        @RequestParam(defaultValue = "0") Integer pageNumber,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<Product> products = productService.getAllProduct(category, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody CreateProductRequest req, @PathVariable Long productId) 
            throws ProductException {
        String result = productService.updateProduct(productId, req);
        ApiResponse res = new ApiResponse();
        res.setMessage(result);
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProducts(@RequestBody CreateProductRequest[] req) {
        for (CreateProductRequest product : req) {
            productService.createProduct(product);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Products created successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    // Assuming ApiResponse is a custom class for responses
    static class ApiResponse {
        private String message;
        private boolean status;

        // Getters and setters

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
