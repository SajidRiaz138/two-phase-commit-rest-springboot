package com.twophasecommit.controller;

import com.twophasecommit.dto.OrderDto;
import com.twophasecommit.mapper.OrderMapper;
import com.twophasecommit.model.Order;
import com.twophasecommit.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/orders")
public class OrderController
{

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper)
    {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping ("/prepare_order")
    public ResponseEntity<String> prepareOrder(@Validated @RequestBody OrderDto orderDto)
    {
        logger.info("Received prepare order request: {}", orderDto);
        try
        {
            Order order = orderMapper.orderDtoToOrder(orderDto);
            orderService.prepareOrder(order);
            return ResponseEntity.ok("Order prepared successfully");
        }
        catch (Exception e)
        {
            logger.error("Error during order preparation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during order preparation");
        }
    }

    @PostMapping ("/commit_order")
    public ResponseEntity<String> commitOrder(@Validated @RequestBody OrderDto transactionData)
    {
        logger.info("Received commit order request: {}", transactionData);
        try
        {
            orderService.commitOrder(transactionData.getItem());
            return ResponseEntity.ok("Order committed successfully");
        }
        catch (Exception e)
        {
            logger.error("Error during order commit", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order cannot be committed");
        }
    }

    @PostMapping ("/rollback_order")
    public ResponseEntity<String> rollbackOrder(@Validated @RequestBody OrderDto transactionData)
    {
        logger.info("Received rollback order request: {}", transactionData);
        try
        {
            orderService.rollbackOrder(transactionData.getItem());
            return ResponseEntity.ok("Order rolled back successfully");
        }
        catch (Exception e)
        {
            logger.error("Error during order rollback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during order rollback");
        }
    }

    @GetMapping ("/{orderNumber}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderNumber)
    {
        logger.info("Received get order request for order number: {}", orderNumber);
        Order order = orderService.findByOrderNumber(orderNumber);
        if (order != null)
        {
            return ResponseEntity.ok(orderMapper.orderToOrderDto(order));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id)
    {
        logger.info("Received delete order request for id: {}", id);
        try
        {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Order deleted successfully");
        }
        catch (Exception e)
        {
            logger.error("Error deleting order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting order");
        }
    }
}
