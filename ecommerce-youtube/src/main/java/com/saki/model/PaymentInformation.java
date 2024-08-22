package com.saki.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "payment_information")
public class PaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cardholder_name", nullable = false)
    private String cardholderName;

    @Column(name = "card_number", nullable = false, length = 19) // Card number length for masking
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate; // Consider using LocalDate for better date handling

    @Column(name = "cvv", nullable = false, length = 4) // CVV is usually 3 or 4 digits
    private String cvv;

    // Default constructor
    public PaymentInformation() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "PaymentInformation{" +
               "id=" + id +
               ", cardholderName='" + cardholderName + '\'' +
               ", cardNumber='" + cardNumber + '\'' +
               ", expirationDate='" + expirationDate + '\'' +
               ", cvv='" + cvv + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentInformation that = (PaymentInformation) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
