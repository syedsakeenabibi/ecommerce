package com.saki.service;

import java.util.List;

import com.saki.exception.OrderException;
import com.saki.model.Address;
import com.saki.model.Order;
import com.saki.model.User;

public interface OrderService {
	
	
public Order createOrder(User user,Address shippingAdress );


public Order findOrderById(Long orderId)throws OrderException;

	public List<Order>userOrderHistory(Long userId);
	 public Order placedOrder(Long orderId)throws OrderException;
	 public Order confirmedOrder(Long orderId)throws OrderException;
	 
	 public Order shippedOrder(Long orderId)throws OrderException;
	
	 public Order deliveredOrder(Long orderId)throws OrderException;
	
	 public Order cancledOrder(Long orderId)throws OrderException;
	
	 public List<Order>getAllOrders();
	 
	 public void deleteOrder(Long orderId)throws OrderException;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
