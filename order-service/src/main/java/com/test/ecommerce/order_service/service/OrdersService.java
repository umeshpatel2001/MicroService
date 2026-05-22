package com.test.ecommerce.order_service.service;

import com.test.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.test.ecommerce.order_service.dto.OrderRequestDto;
import com.test.ecommerce.order_service.entity.OrderItem;
import com.test.ecommerce.order_service.entity.OrderStatus;
import com.test.ecommerce.order_service.entity.Orders;
import com.test.ecommerce.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Data;
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
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

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

//    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);
        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
        for (OrderItem orderItem : orders.getItems()) {
            orderItem.setOrder(orders);
        }
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);
        Orders savedOrders = ordersRepository.save(orders);
        return modelMapper.map(savedOrders, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Falling back to create order", throwable);
        return new OrderRequestDto();
    }
}
