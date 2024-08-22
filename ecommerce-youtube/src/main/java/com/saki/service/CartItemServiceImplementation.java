package com.saki.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saki.exception.CartItemException;
import com.saki.exception.ProductException;
import com.saki.exception.UserException;
import com.saki.model.Cart;
import com.saki.model.CartItem;
import com.saki.model.Product;
import com.saki.model.User;
import com.saki.repository.CartItemRepository;
import com.saki.repository.CartRepository;
import com.saki.repository.ProductRepository;

@Service
public class CartItemServiceImplementation implements CartItemService {

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImplementation.class);

    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartItemServiceImplementation(CartItemRepository cartItemRepository, 
                                         UserService userService, 
                                         CartRepository cartRepository, 
                                         ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CartItem createCartItem(CartItem cartItem) throws ProductException {
        if (cartItem == null || cartItem.getProduct() == null || cartItem.getProduct().getId() == null) {
            throw new IllegalArgumentException("Cart item or product information is missing");
        }

        Long productId = cartItem.getProduct().getId();
        logger.debug("Looking for product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

        cartItem.setPrice(product.getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(product.getDiscountedPrice() * cartItem.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        try {
            logger.debug("Finding user with ID: {}", userId);
            User user = userService.findUserById(userId);
            logger.debug("Found user: {}", user);
            return cartItemRepository.isCartItemExist(cart, product, size, user);
        } catch (UserException e) {
            logger.error("Error finding user with ID: {}", userId, e);
            throw new RuntimeException("Error finding user", e);
        }
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemId);
        User user = userService.findUserById(userId);

        if (cartItem.getUser().getId().equals(user.getId())) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UserException("You cannot remove another user's item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException("Cart item not found with id: " + cartItemId));
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem existingItem = findCartItemById(id);
        User user = userService.findUserById(userId);

        if (!existingItem.getUser().getId().equals(user.getId())) {
            throw new UserException("You cannot update another user's item");
        }

        existingItem.setQuantity(cartItem.getQuantity());
        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new CartItemException("Product not found"));
        existingItem.setPrice(product.getPrice() * cartItem.getQuantity());
        existingItem.setDiscountedPrice(product.getDiscountedPrice() * cartItem.getQuantity());

        return cartItemRepository.save(existingItem);
    }
}
