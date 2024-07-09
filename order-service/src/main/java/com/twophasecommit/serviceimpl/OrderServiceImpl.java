package com.twophasecommit.serviceimpl;

import com.twophasecommit.dto.OrderStatus;
import com.twophasecommit.exceptionhandler.OrderException;
import com.twophasecommit.model.Order;
import com.twophasecommit.repository.OrderRepository;
import com.twophasecommit.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderServiceImpl implements OrderService
{

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order saveOrder(Order order)
    {
        logger.info("Saving order: {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Order findByItem(String item)
    {
        logger.info("Finding order by item: {}", item);
        return orderRepository.findByItem(item);
    }

    @Override
    public Order findByOrderNumber(String orderNumber)
    {
        logger.info("Finding order by order number: {}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public void deleteOrder(Long id)
    {
        logger.info("Deleting order with id: {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    public void prepareOrder(Order order)
    {
        logger.info("Preparing order: {}", order);
        order.setPreparationStatus(OrderStatus.PREPARING.name());
        saveOrder(order);
    }

    @Override
    public void commitOrder(String item)
    {
        logger.info("Committing order for item: {}", item);
        Order order = findByItem(item);
        if (order != null && order.getPreparationStatus().equalsIgnoreCase(OrderStatus.PREPARING.name()))
        {
            order.setPreparationStatus(OrderStatus.COMMITTED.name());
            saveOrder(order);
        }
        else
        {
            throw new OrderException("Order cannot be committed");
        }
    }

    @Override
    public void rollbackOrder(String item)
    {
        logger.info("Rolling back order for item: {}", item);
        Order order = findByItem(item);
        if (order != null)
        {
            order.setPreparationStatus(OrderStatus.ROLLBACK.name());
            saveOrder(order);
        }
        else
        {
            throw new OrderException("Error during order rollback");
        }
    }
}
