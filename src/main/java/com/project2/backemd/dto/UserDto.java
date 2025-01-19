package com.project2.backemd.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String name;
    private String password;
    private List<String> role;
    private String email;
}