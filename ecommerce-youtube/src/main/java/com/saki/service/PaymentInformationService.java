package com.saki.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saki.exception.PaymentInformationException;
import com.saki.model.PaymentInformation;
import com.saki.repository.PaymentInformationRepository;

@Service
public class PaymentInformationService {

    private final PaymentInformationRepository paymentInformationRepository;

    public PaymentInformationService(PaymentInformationRepository paymentInformationRepository) {
        this.paymentInformationRepository = paymentInformationRepository;
    }

    @Transactional
    public PaymentInformation createPaymentInformation(PaymentInformation paymentInformation) throws PaymentInformationException {
        if (paymentInformation == null) {
            throw new IllegalArgumentException("Payment information must not be null");
        }
        // Save and return the payment information
        return paymentInformationRepository.save(paymentInformation);
    }

    public PaymentInformation findPaymentInformationById(Long id) throws PaymentInformationException {
        return paymentInformationRepository.findById(id)
                .orElseThrow(() -> new PaymentInformationException("Payment information not found with id: " + id));
    }

    @Transactional
    public PaymentInformation updatePaymentInformation(Long id, PaymentInformation paymentInformation) throws PaymentInformationException {
        PaymentInformation existingInfo = findPaymentInformationById(id);
        // Update the details
        existingInfo.setCardholderName(paymentInformation.getCardholderName());
        existingInfo.setCardNumber(paymentInformation.getCardNumber());
        existingInfo.setExpirationDate(paymentInformation.getExpirationDate());
        existingInfo.setCvv(paymentInformation.getCvv());
        return paymentInformationRepository.save(existingInfo);
    }

    @Transactional
    public void deletePaymentInformation(Long id) throws PaymentInformationException {
        if (!paymentInformationRepository.existsById(id)) {
            throw new PaymentInformationException("Payment information not found with id: " + id);
        }
        paymentInformationRepository.deleteById(id);
    }
}
