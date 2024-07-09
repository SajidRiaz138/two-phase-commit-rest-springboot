package com.twophasecommit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderDto
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
