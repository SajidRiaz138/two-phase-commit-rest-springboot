package com.twophasecommit.repository;

import com.twophasecommit.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    Order findByItem(String item);
    Order findByOrderNumber(String orderNumber);
}
