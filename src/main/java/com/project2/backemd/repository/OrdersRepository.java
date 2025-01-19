package com.project2.backemd.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project2.backemd.model.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer> {
    List<Order> findByOrderDateBetween(OffsetDateTime start, OffsetDateTime end);
}
