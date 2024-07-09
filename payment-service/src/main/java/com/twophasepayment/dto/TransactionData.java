package com.twophasepayment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransactionData
{
    @NotEmpty
    private String orderNumber;

    @NotEmpty
    private String item;

    @NotEmpty
    private String price;

    @NotEmpty
    private String paymentMode;
}
