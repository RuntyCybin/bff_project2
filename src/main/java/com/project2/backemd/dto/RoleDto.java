package com.project2.backemd.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private UUID roleId;
    private String roleName;
}
