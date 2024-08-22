package com.saki.service;

import com.saki.exception.ProductException;
import com.saki.model.Cart;
import com.saki.model.User;
import com.saki.request.AddItemRequest;

public interface CartService {

	
	
	public Cart createCart(User user);
	
	public String addCartItem(Long userId,AddItemRequest req)throws ProductException;
	
	public Cart findUserCart(Long userId);
	
	
	
	
}
