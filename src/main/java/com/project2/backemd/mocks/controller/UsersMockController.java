package com.project2.backemd.mocks.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project2.backemd.dto.UserDto;
import com.project2.backemd.mocks.UserServiceMocks;

@RestController
@RequestMapping("/mocked_api")
public class UsersMockController {

    @Autowired
    private UserServiceMocks service;


    @GetMapping("completedusers")
    public CompletableFuture<List<UserDto>> getUsersAsync()
            throws InterruptedException {

        CompletableFuture<List<UserDto>> users = service.getUsers();
        List<UserDto> response = new ArrayList<>();

        try {
            response.addAll(users.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.allOf(users).thenApply(result -> {
            return response;
        });
    }

    @GetMapping("usuario")
    public ResponseEntity<List<UserDto>> getById() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return ResponseEntity.status(202)
                .headers(headers)
                .body(List.of(UserDto.builder()
                        .name("Jhon")
                        .role(List.of("USER"))
                        .build()));
    }
}
