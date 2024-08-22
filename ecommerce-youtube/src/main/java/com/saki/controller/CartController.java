package com.saki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saki.exception.ProductException;
import com.saki.exception.UserException;
import com.saki.model.Cart;
import com.saki.request.AddItemRequest;
import com.saki.service.CartService;
import com.saki.service.UserService;
import com.saki.model.User;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String authHeader) {
        try {
            String jwt = extractJwtToken(authHeader);
            User user = userService.findUserProfileByJwt(jwt);
            Cart cart = cartService.findUserCart(user.getId());
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            String jwt = extractJwtToken(authHeader);
            User user = userService.findUserProfileByJwt(jwt);
            cartService.addCartItem(user.getId(), req);

            ApiResponse res = new ApiResponse("Item added to cart", true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (UserException | ProductException e) {
            ApiResponse res = new ApiResponse(e.getMessage(), false);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    private String extractJwtToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    // Assuming ApiResponse is a custom class for responses
    static class ApiResponse {
        private String message;
        private boolean status;

        public ApiResponse() {}

        public ApiResponse(String message, boolean status) {
            this.message = message;
            this.status = status;
        }

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
