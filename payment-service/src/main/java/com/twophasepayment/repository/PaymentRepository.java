package com.twophasepayment.repository;

import com.twophasepayment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>
{
    Payment findByItem(String item);
    Payment findPaymentByOrderNumber(String orderNumber);

    void deletePaymentByOrderNumber(String orderNumber);
}
