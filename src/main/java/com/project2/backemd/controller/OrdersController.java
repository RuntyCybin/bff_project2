package com.project2.backemd.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project2.backemd.dto.OrderDto;
import com.project2.backemd.service.OrdersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("order/{id}")
    public CompletableFuture<List<OrderDto>> getOrderById(
            @PathVariable Integer id) throws InterruptedException {

        CompletableFuture<OrderDto> orders = ordersService.obteneOrderByIdAsync(id);
        List<OrderDto> response = new ArrayList<>();

        try {
            response.add(orders.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.allOf(orders).thenApply(result -> {
            return response;
        });
    }

    @GetMapping("all")
    public CompletableFuture<Page<OrderDto>> listarOrders (@PageableDefault(size = 10) Pageable pageable) {
        return ordersService.obtenerOrdersAsync(pageable);
    }

    @GetMapping("dates")
    public CompletableFuture<Page<OrderDto>> listarPorFecha (
        @PageableDefault(size = 10) Pageable pageable, 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startdate, 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime enddate) {

        return ordersService.obtenerOrderByFechaAsync(startdate, enddate, pageable);
    }

}
