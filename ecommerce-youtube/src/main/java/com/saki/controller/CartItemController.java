package com.saki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saki.exception.CartItemException;
import com.saki.exception.ProductException;
import com.saki.exception.UserException;
import com.saki.model.CartItem;
import com.saki.model.Product;
import com.saki.model.User;
import com.saki.request.UpdateCartItemRequest;
import com.saki.service.CartItemService;
import com.saki.service.ProductService;
import com.saki.service.UserService;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveCartItem(@RequestBody CartItem cartItem,
                                                    @RequestHeader("Authorization") String authHeader) {
        try {
            String jwt = extractJwtToken(authHeader);
            User user = userService.findUserProfileByJwt(jwt);
            if (user == null) {
                throw new UserException("User not found");
            }
            // Check if user has a cart
            if (user.getCart() == null) {
                throw new CartItemException("User does not have a cart");
            }
            cartItem.setCart(user.getCart());

            // Ensure the Product exists
            Product product = productService.findProductById(cartItem.getProduct().getId());
            cartItem.setProduct(product);

            cartItemService.createCartItem(cartItem);

            return new ResponseEntity<>(new ApiResponse("Item saved to cart", true), HttpStatus.CREATED);
        } catch (UserException | ProductException | CartItemException | IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    
    
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long id,
                                                      @RequestBody UpdateCartItemRequest req,
                                                      @RequestHeader("Authorization") String authHeader) {
        try {
            String jwt = extractJwtToken(authHeader);
            User user = userService.findUserProfileByJwt(jwt);

            // Fetch the existing CartItem to update
            CartItem existingCartItem = cartItemService.findCartItemById(id);
            if (existingCartItem == null) {
                throw new CartItemException("CartItem not found");
            }
            existingCartItem.setProduct(new Product(req.getProductId())); // Ensure Product constructor or setter exists
            existingCartItem.setQuantity(req.getQuantity());
            existingCartItem.setSize(req.getSize());

            cartItemService.updateCartItem(user.getId(), id, existingCartItem);

            return new ResponseEntity<>(new ApiResponse("Item updated in cart", true), HttpStatus.OK);
        } catch (UserException | CartItemException | ProductException | IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String authHeader) {
        try {
            String jwt = extractJwtToken(authHeader);
            User user = userService.findUserProfileByJwt(jwt);
            cartItemService.removeCartItem(user.getId(), id);

            return new ResponseEntity<>(new ApiResponse("Item removed from cart", true), HttpStatus.OK);
        } catch (UserException | CartItemException | IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    private String extractJwtToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

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
