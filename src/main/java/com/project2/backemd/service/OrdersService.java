package com.project2.backemd.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.project2.backemd.dto.OrderDto;
import com.project2.backemd.model.Order;
import com.project2.backemd.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final static String ORDER_BY_ID_NOT_FOUND = "Order not found with ID: ";
    private final static String ORDER__BY_DATE_NOT_FOUND = "Orders not found in this range of dates";
    private final static String ORDERS_NOT_FOUND = "No orders found";

    private final OrdersRepository ordersRepository;

    @Async
    public CompletableFuture<OrderDto> obteneOrderByIdAsync(Integer idOrder) {
        Order order = ordersRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException(ORDER_BY_ID_NOT_FOUND + idOrder));

        return CompletableFuture.completedFuture(OrderDto.builder()
                .orderId(UUID.randomUUID())
                .userName(order.getUser().getName())
                .orderDescription(order.getDescription())
                .orderCreated(order.getOrderDate())
                .build());
    }

    @Async
    public CompletableFuture<Page<OrderDto>> obtenerOrderByFechaAsync(LocalDateTime fechaIni,
            LocalDateTime fechaFin, Pageable pageable) {

        List<Order> orders = ordersRepository.findByOrderDateBetween(fechaIni.atOffset(ZoneOffset.UTC), fechaFin.atOffset(ZoneOffset.UTC));

        if (orders.isEmpty()) {
            throw new RuntimeException(ORDER__BY_DATE_NOT_FOUND);
        }

        return this.convertOrdersToOrderDto(orders, pageable);
    }

    @Async
    public CompletableFuture<Page<OrderDto>> obtenerOrdersAsync(Pageable pageable) {

        List<Order> orders = ordersRepository.findAll();

        if (orders.isEmpty()) {
            throw new RuntimeException(ORDERS_NOT_FOUND);
        }

        return this.convertOrdersToOrderDto(orders, pageable);
    }

    private CompletableFuture<Page<OrderDto>> convertOrdersToOrderDto(List<Order> orders, Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            // Mapeo de Order a OrderDto
            List<OrderDto> orderDtos = orders.stream()
                    .map(order -> OrderDto.builder()
                            .orderId(UUID.randomUUID())
                            .userName(order.getUser().getName())
                            .orderDescription(order.getDescription())
                            .orderCreated(order.getOrderDate())
                            .build())
                    .toList();

            // Creación de Page<OrderDto> usando PageImpl
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), orderDtos.size());
            List<OrderDto> pagedList = orderDtos.subList(start, end);

            return new PageImpl<>(pagedList, pageable, orderDtos.size());
        });
    }

}
