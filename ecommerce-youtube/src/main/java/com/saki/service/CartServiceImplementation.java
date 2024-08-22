package com.saki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saki.exception.ProductException;
import com.saki.model.Cart;
import com.saki.model.CartItem;
import com.saki.model.Product;
import com.saki.model.User;
import com.saki.repository.CartRepository;
import com.saki.repository.UserRepository; // Import UserRepository
import com.saki.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService {

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;
    private UserRepository userRepository; // Add UserRepository

    @Autowired
    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, 
                                      ProductService productService, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.userRepository = userRepository; // Initialize UserRepository
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(req.getProductId());
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new ProductException("User not found")); // Use UserRepository

        CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUser(user); // This line should be removed if you are using entity relationships properly

            int price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        return "Item Added To Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscount(totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}
