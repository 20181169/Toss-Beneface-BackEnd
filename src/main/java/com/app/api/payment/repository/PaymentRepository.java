package com.app.api.payment.repository;

import com.app.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByPaymentKeyAndCustomer_Email(String paymentKey, String email);
    Slice<Payment> findAllByCustomer_Email(String email, Pageable pageable);
}
