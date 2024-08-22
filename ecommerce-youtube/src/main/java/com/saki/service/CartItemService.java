package com.saki.service;

import com.saki.exception.CartItemException;
import com.saki.exception.ProductException;
import com.saki.exception.UserException;
import com.saki.model.Cart;
import com.saki.model.CartItem;
import com.saki.model.Product;

public interface CartItemService {

    
    CartItem createCartItem(CartItem cartItem)throws ProductException;

    
    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    
    CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
