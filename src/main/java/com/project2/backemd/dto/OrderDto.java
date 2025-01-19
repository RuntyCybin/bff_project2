package com.project2.backemd.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
    private UUID orderId;
    private String userName;
    private String orderDescription;
    private OffsetDateTime orderCreated;
}
