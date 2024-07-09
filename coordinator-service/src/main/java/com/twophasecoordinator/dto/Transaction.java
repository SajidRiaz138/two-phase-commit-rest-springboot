package com.twophasecoordinator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction
{
    private String orderNumber;
    private String item;
    private String price;
    private String paymentMode;
}
