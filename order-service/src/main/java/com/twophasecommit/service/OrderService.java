package com.twophasecommit.service;

import com.twophasecommit.model.Order;

public interface OrderService
{
    Order saveOrder(Order order);

    Order findByItem(String item);

    Order findByOrderNumber(String orderNumber);

    void deleteOrder(Long id);

    void prepareOrder(Order order);

    void commitOrder(String item);

    void rollbackOrder(String item);
}
