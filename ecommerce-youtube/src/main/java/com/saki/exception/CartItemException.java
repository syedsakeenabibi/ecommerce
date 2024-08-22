package com.saki.exception;

public class CartItemException extends Exception {

    // Constructor that accepts a message
    public CartItemException(String message) {
        super(message);
    }

    // No-args constructor
    public CartItemException() {
        super();
    }

    // Optionally, you can override methods for more detailed error information
    @Override
    public String toString() {
        return "CartItemException: " + getMessage();
    }

    // Optionally, you can add more methods or functionality if needed
}
