package com.saki.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.saki.exception.PaymentInformationException;
import com.saki.model.PaymentInformation;
import com.saki.service.PaymentInformationService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/payment-information")
@Validated
public class PaymentInformationController {

    private static final Logger LOGGER = Logger.getLogger(PaymentInformationController.class.getName());
    private final PaymentInformationService paymentInformationService;

    public PaymentInformationController(PaymentInformationService paymentInformationService) {
        this.paymentInformationService = paymentInformationService;
    }

    /**
     * Creates a new PaymentInformation entry.
     *
     * @param paymentInformation the PaymentInformation object to create
     * @return the created PaymentInformation object
     */
    @PostMapping
    public ResponseEntity<PaymentInformation> createPaymentInformation( @RequestBody PaymentInformation paymentInformation) {
        try {
            PaymentInformation createdPaymentInfo = paymentInformationService.createPaymentInformation(paymentInformation);
            return ResponseEntity.ok(createdPaymentInfo);
        } catch (PaymentInformationException e) {
            LOGGER.severe("Failed to create payment information: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves a PaymentInformation entry by its ID.
     *
     * @param id the ID of the PaymentInformation to retrieve
     * @return the PaymentInformation object with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentInformation> getPaymentInformationById(@PathVariable Long id) {
        try {
            PaymentInformation paymentInformation = paymentInformationService.findPaymentInformationById(id);
            return ResponseEntity.ok(paymentInformation);
        } catch (PaymentInformationException e) {
            LOGGER.warning("Payment information not found for ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates an existing PaymentInformation entry.
     *
     * @param id the ID of the PaymentInformation to update
     * @param paymentInformation the updated PaymentInformation object
     * @return the updated PaymentInformation object
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentInformation> updatePaymentInformation(@PathVariable Long id, @RequestBody PaymentInformation paymentInformation) {
        try {
            PaymentInformation updatedPaymentInfo = paymentInformationService.updatePaymentInformation(id, paymentInformation);
            return ResponseEntity.ok(updatedPaymentInfo);
        } catch (PaymentInformationException e) {
            LOGGER.severe("Failed to update payment information: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a PaymentInformation entry by its ID.
     *
     * @param id the ID of the PaymentInformation to delete
     * @return a response with status 204 if successful, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentInformation(@PathVariable Long id) {
        try {
            paymentInformationService.deletePaymentInformation(id);
            return ResponseEntity.noContent().build();
        } catch (PaymentInformationException e) {
            LOGGER.warning("Failed to delete payment information for ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }
}
