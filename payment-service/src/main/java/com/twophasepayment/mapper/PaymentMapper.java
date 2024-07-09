package com.twophasepayment.mapper;

import com.twophasepayment.dto.TransactionData;
import com.twophasepayment.model.Payment;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface PaymentMapper
{
    Payment transactionDataToPayment(TransactionData transactionData);

    TransactionData paymentToTransactionData(Payment payment);
}
