package com.saki.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Size implements Serializable {

    private String name;
    private int quantity;

    // Default constructor
    public Size() {
    }

    // Parameterized constructor (optional)
    public Size(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Override equals and hashCode for proper functioning in collections and comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return quantity == size.quantity && Objects.equals(name, size.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity);
    }
}
