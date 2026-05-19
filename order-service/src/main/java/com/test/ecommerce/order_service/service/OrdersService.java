package com.test.ecommerce.order_service.service;

import com.test.ecommerce.order_service.dto.OrderRequestDto;
import com.test.ecommerce.order_service.entity.Orders;
import com.test.ecommerce.order_service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Getting all orders");
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long orderId) {
        log.info("Getting order by id {}", orderId);
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order Not Found"));
        return modelMapper.map(orders, OrderRequestDto.class);
    }
}
