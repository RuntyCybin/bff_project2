package com.project2.backemd.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
