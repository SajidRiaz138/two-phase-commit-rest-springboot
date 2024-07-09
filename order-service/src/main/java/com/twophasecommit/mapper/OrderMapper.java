package com.twophasecommit.mapper;

import com.twophasecommit.dto.OrderDto;
import com.twophasecommit.model.Order;
import org.mapstruct.Mapper;


@Mapper
public interface OrderMapper
{
    OrderDto orderToOrderDto(Order order);

    Order orderDtoToOrder(OrderDto orderDto);
}
