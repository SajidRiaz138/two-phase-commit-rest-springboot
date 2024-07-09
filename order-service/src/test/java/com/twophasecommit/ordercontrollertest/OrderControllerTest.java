package com.twophasecommit.ordercontrollertest;

import com.twophasecommit.controller.OrderController;
import com.twophasecommit.dto.OrderDto;
import com.twophasecommit.mapper.OrderMapper;
import com.twophasecommit.model.Order;
import com.twophasecommit.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest
{

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPrepareOrderSuccess()
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderNumber("12345");
        orderDto.setItem("Laptop");

        Order order = new Order();
        order.setOrderNumber(orderDto.getOrderNumber());
        order.setItem(orderDto.getItem());
        order.setPreparationStatus("PREPARING");

        when(orderMapper.orderDtoToOrder(orderDto)).thenReturn(order);
        doNothing().when(orderService).prepareOrder(any(Order.class));

        ResponseEntity<String> response = orderController.prepareOrder(orderDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order prepared successfully", response.getBody());
        verify(orderService, times(1)).prepareOrder(any(Order.class));
    }

    @Test
    public void testPrepareOrderFailure()
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderNumber("12345");
        orderDto.setItem("Laptop");

        when(orderMapper.orderDtoToOrder(orderDto)).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<String> response = orderController.prepareOrder(orderDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error during order preparation", response.getBody());
        verify(orderService, times(0)).prepareOrder(any(Order.class));
    }

    @Test
    public void testCommitOrderSuccess()
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setItem("Laptop");

        doNothing().when(orderService).commitOrder(orderDto.getItem());

        ResponseEntity<String> response = orderController.commitOrder(orderDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order committed successfully", response.getBody());
        verify(orderService, times(1)).commitOrder(orderDto.getItem());
    }

    @Test
    public void testCommitOrderFailure()
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setItem("Laptop");

        doThrow(new RuntimeException("Order cannot be committed")).when(orderService).commitOrder(orderDto.getItem());

        ResponseEntity<String> response = orderController.commitOrder(orderDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Order cannot be committed", response.getBody());
        verify(orderService, times(1)).commitOrder(orderDto.getItem());
    }

    @Test
    public void testRollbackOrderSuccess()
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setItem("Laptop");

        doNothing().when(orderService).rollbackOrder(orderDto.getItem());

        ResponseEntity<String> response = orderController.rollbackOrder(orderDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order rolled back successfully", response.getBody());
        verify(orderService, times(1)).rollbackOrder(orderDto.getItem());
    }

    @Test
    public void testRollbackOrderFailure()
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setItem("Laptop");

        doThrow(new RuntimeException("Error during order rollback")).when(orderService).rollbackOrder(orderDto.getItem());

        ResponseEntity<String> response = orderController.rollbackOrder(orderDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error during order rollback", response.getBody());
        verify(orderService, times(1)).rollbackOrder(orderDto.getItem());
    }

    @Test
    public void testGetOrderSuccess()
    {
        String orderNumber = "12345";

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setItem("Laptop");

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderNumber(orderNumber);
        orderDto.setItem("Laptop");

        when(orderService.findByOrderNumber(orderNumber)).thenReturn(order);
        when(orderMapper.orderToOrderDto(order)).thenReturn(orderDto);

        ResponseEntity<OrderDto> response = orderController.getOrder(orderNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto, response.getBody());
        verify(orderService, times(1)).findByOrderNumber(orderNumber);
        verify(orderMapper, times(1)).orderToOrderDto(order);
    }

    @Test
    public void testGetOrderNotFound()
    {
        String orderNumber = "12345";

        when(orderService.findByOrderNumber(orderNumber)).thenReturn(null);

        ResponseEntity<OrderDto> response = orderController.getOrder(orderNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderService, times(1)).findByOrderNumber(orderNumber);
        verify(orderMapper, times(0)).orderToOrderDto(any(Order.class));
    }

    @Test
    public void testDeleteOrderSuccess()
    {
        Long id = 1L;

        doNothing().when(orderService).deleteOrder(id);

        ResponseEntity<String> response = orderController.deleteOrder(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order deleted successfully", response.getBody());
        verify(orderService, times(1)).deleteOrder(id);
    }

    @Test
    public void testDeleteOrderFailure()
    {
        Long id = 1L;

        doThrow(new RuntimeException("Test Exception")).when(orderService).deleteOrder(id);

        ResponseEntity<String> response = orderController.deleteOrder(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting order", response.getBody());
        verify(orderService, times(1)).deleteOrder(id);
    }
}