package com.saki.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saki.exception.OrderException;
import com.saki.exception.UserException;
import com.saki.model.Address;
import com.saki.model.Order;
import com.saki.model.User;
import com.saki.service.OrderService;
import com.saki.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * Creates a new order for the authenticated user.
     *
     * @param shippingAddress the address where the order will be shipped
     * @param jwt the JWT token for authentication
     * @return the created Order
     * @throws UserException if the user cannot be authenticated
     */
    @PostMapping("/")
    public ResponseEntity<Order> createOrder( @RequestBody Address shippingAddress, 
                                             @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, shippingAddress);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * Retrieves the order history for the authenticated user.
     *
     * @param jwt the JWT token for authentication
     * @return the list of Orders for the user
     * @throws UserException if the user cannot be authenticated
     */
    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @param jwt the JWT token for authentication
     * @return the Order with the specified ID
     * @throws UserException if the user cannot be authenticated
     * @throws OrderException if the order is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable("id") Long orderId, 
                                               @RequestHeader("Authorization") String jwt) throws UserException, OrderException {
        userService.findUserProfileByJwt(jwt); // Validate user, but not used directly in this method
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
}
