package com.saki.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    @Column(name = "total_price")
    private double totalPrice = 0.0;

    @Column(name = "total_item")
    private int totalItem = 0;

    @Column(name = "total_discounted_price")
    private double totalDiscountedPrice = 0.0;

    @Column(name = "discount")
    private double discount = 0.0;

    // Default constructor
    public Cart() {}

    // Constructor with parameters
    public Cart(User user, double totalPrice, int totalItem, double totalDiscountedPrice, double discount) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.totalItem = totalItem;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.discount = discount;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public void setTotalDiscountedPrice(double totalDiscountedPrice) {
        this.totalDiscountedPrice = totalDiscountedPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Convenience methods
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
        updateTotals();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
        updateTotals();
    }

    // Update total price, discounted price, and total items
    private void updateTotals() {
        this.totalPrice = cartItems.stream()
                                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                                    .sum();
        this.totalDiscountedPrice = cartItems.stream()
                                              .mapToDouble(item -> item.getDiscountedPrice() * item.getQuantity())
                                              .sum();
        this.totalItem = cartItems.stream()
                                   .mapToInt(CartItem::getQuantity)
                                   .sum();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                ", totalItem=" + totalItem +
                ", totalDiscountedPrice=" + totalDiscountedPrice +
                ", discount=" + discount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
