package com.project2.backemd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project2.backemd.dto.UserDto;
import com.project2.backemd.service.UsersService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("id/{id}")
    public CompletableFuture<List<UserDto>> getUsersByIdAsync(
            @PathVariable Integer id) throws InterruptedException, ExecutionException {

        CompletableFuture<UserDto> users = service.obtenerUsuarioAsyncPorId(id);
        List<UserDto> response = new ArrayList<>();

        try {
            response.add(users.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.allOf(users).thenApply(result -> {
            return response;
        });

    }

    @GetMapping("email/{email}")
    public CompletableFuture<List<UserDto>> getUsersByEmailAsync(
            @PathVariable String email) throws InterruptedException {

        CompletableFuture<UserDto> users = service.obtenerUsuarioAsyncPorEmail(email);
        List<UserDto> response = new ArrayList<>();

        try {
            response.add(users.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.allOf(users).thenApply(result -> {
            return response;
        });

    }

    @PostMapping("create-user-async")
    public CompletableFuture<String> createUserAsync(@RequestBody UserDto dto) {
        return service.guardarUsuarioAsync(dto).thenApply(result -> {
            return "CREATED";
        });
    }

    @PostMapping("create")
    public ResponseEntity<String> createUser(@RequestBody UserDto dto) {
        try {
            service.guardarUsuario(dto);
            return ResponseEntity.ok("CREATED");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("NOT CREATED");
        }
    }

}
