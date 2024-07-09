package com.twophasepayment.service;

import com.twophasepayment.dto.TransactionData;
import com.twophasepayment.model.Payment;

public interface PaymentService
{
    Payment preparePayment(TransactionData transactionData);

    Payment commitPayment(TransactionData transactionData);

    Payment rollbackPayment(TransactionData transactionData);

    TransactionData findPaymentByOrderNumber(String orderNumber);
}
