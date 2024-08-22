package com.saki.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saki.exception.OrderException;
import com.saki.model.Address;
import com.saki.model.Cart;
import com.saki.model.CartItem;
import com.saki.model.Order;
import com.saki.model.OrderItem;
import com.saki.model.User;
import com.saki.repository.AddressRepository;
import com.saki.repository.OrderItemRepository;
import com.saki.repository.OrderRepository;
import com.saki.repository.UserRepository;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService, 
                                      AddressRepository addressRepository, UserRepository userRepository, 
                                      OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {

        // Save shipping address and update user
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddress().add(address);
        userRepository.save(user);

        // Retrieve user's cart
        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            // No need to set userId here if not required
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            orderItem.setOrder(null); // Set order to null initially

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        }

        // Create and save the order
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(createdOrder);

        // Update each order item to reference the saved order
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderException("Order not exists with id " + orderId));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.getUsersOrders(userId);
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return orderRepository.save(order);
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId);
    }
}
