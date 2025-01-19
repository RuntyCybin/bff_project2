package com.project2.backemd.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

public class OrderDto {
    private UUID orderId;
    private String userName;
    private String orderDescription;
    private OffsetDateTime orderCreated;

    // Private constructor to enforce the use of the builder
    private OrderDto(Builder builder) {
        this.orderId = builder.orderId;
        this.userName = builder.userName;
        this.orderDescription = builder.orderDescription;
        this.orderCreated = builder.orderCreated;
    }

    // Getters
    public UUID getOrderId() {
        return orderId;
    }

    public String getUserName() {
        return userName;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public OffsetDateTime getOrderCreated() {
        return orderCreated;
    }

    // Builder class
    public static class Builder {
        private UUID orderId;
        private String userName;
        private String orderDescription;
        private OffsetDateTime orderCreated;

        public Builder setOrderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setOrderDescription(String orderDescription) {
            this.orderDescription = orderDescription;
            return this;
        }

        public Builder setOrderCreated(OffsetDateTime orderCreated) {
            this.orderCreated = orderCreated;
            return this;
        }

        public OrderDto build() {
            return new OrderDto(this);
        }
    }
}
