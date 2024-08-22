package com.saki.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotNull
    @Size(max = 50)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(max = 50)
    private String lastName;

    @Column(name = "street_address")
    @NotNull
    @Size(max = 100)
    private String streetAddress;

    @Column(name = "city")
    @NotNull
    @Size(max = 50)
    private String city;

    @Column(name = "state")
    @NotNull
    @Size(max = 50)
    private String state;

    @Column(name = "zip_code")
    @NotNull
    @Size(max = 10)
    @Pattern(regexp = "\\d{5}(-\\d{4})?", message = "Invalid zip code format")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "mobile")
    @Size(max = 15)
    @Pattern(regexp = "\\d{10,15}", message = "Invalid mobile number format")
    private String mobile;

    // Default constructor
    public Address() {}

    // Constructor with fields
    public Address(Long id, String firstName, String lastName, String streetAddress, String city, String state,
                   String zipCode, User user, String mobile) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.user = user;
        this.mobile = mobile;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Address{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", streetAddress='" + streetAddress + '\'' +
               ", city='" + city + '\'' +
               ", state='" + state + '\'' +
               ", zipCode='" + zipCode + '\'' +
               ", mobile='" + mobile + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id != null && id.equals(address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
