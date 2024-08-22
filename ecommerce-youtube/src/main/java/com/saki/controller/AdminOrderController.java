package com.saki.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saki.exception.OrderException;
import com.saki.service.OrderService;

import com.saki.model.Order; // Corrected import for Order

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/")
	public ResponseEntity<List<Order>> getAllOrdersHandler() {
		List<Order> orders = orderService.getAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{orderId}/confirmed")
	public ResponseEntity<Order> confirmedOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException {
		Order order = orderService.confirmedOrder(orderId);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PutMapping("/{orderId}/ship")
	public ResponseEntity<Order> shippedOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException {
		Order order = orderService.shippedOrder(orderId);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException {
		Order order = orderService.cancledOrder(orderId);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/{orderId}/delete")
	public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException {
		orderService.deleteOrder(orderId);
		ApiResponse res = new ApiResponse();
		res.setMessage("Order deleted successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}

// Assuming ApiResponse is a custom class for responses
class ApiResponse {
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
