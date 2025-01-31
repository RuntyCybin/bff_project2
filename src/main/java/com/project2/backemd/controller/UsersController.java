package com.project2.backemd.controller;

import com.project2.backemd.model.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project2.backemd.dto.UserDto;
import com.project2.backemd.service.UsersService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("::USER: " + authentication.getName());

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserDto.Builder()
                .setName(currentUser.getName())
                .setEmail(currentUser.getEmail())
                .build());
    }

    @GetMapping("/strings")
    @Async
    public DeferredResult<List<String>> getStrings() {

        DeferredResult<List<String>> deferredResult = new DeferredResult<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CompletableFuture.supplyAsync(() -> {

            User currentUser = (User) authentication.getPrincipal();
            if (null == currentUser) {
                throw new SecurityException("Unauthorized access");
            }

            List<String> strings = Arrays.asList("string1", "string2", "string3");
            return strings;
        }).whenComplete((result, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            } else {
                deferredResult.setResult(result);
            }
        });
        return deferredResult;
    }


    @GetMapping("/securedEndpoint")
    public DeferredResult<String> securedEndpoint() {
        DeferredResult<String> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            // Perform security check here
            if (null == currentUser) {
                throw new SecurityException("Unauthorized access");
            }
            // Return result
            return "Hello, secured world!";
        }).whenComplete((result, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            } else {
                deferredResult.setResult(result);
            }
        });

        return deferredResult;
    }


    @GetMapping("/strings1")
    @Async
    public ResponseEntity<List<String>> getStrings1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<String> strings = Arrays.asList("string1", "string2", "string3");
        return ResponseEntity.ok(strings);
    }

    @GetMapping("id-sync/{id}")
    public ResponseEntity<List<UserDto>> getUsersByIdSync(
            @PathVariable Integer id) throws InterruptedException, ExecutionException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getId().equals(id)) {
            CompletableFuture<UserDto> users = service.obtenerUsuarioAsyncPorId(id);
            List<UserDto> response = new ArrayList<>();

            try {
                response.add(users.get());
                return ResponseEntity.ok(response);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("id-async/{id}")
    @Async
    public CompletableFuture<ResponseEntity<List<UserDto>>> getUsersByIdAsync(
            @PathVariable Integer id) throws ExecutionException, InterruptedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getId().equals(id)) {
            return service.obtenerUsuarioAsyncPorId(id)
                    .thenApply(userDto -> {
                        List<UserDto> response = new ArrayList<>();
                        response.add(userDto);
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(e -> {
                        // Manejo de excepciones
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    });
        }

        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
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

}
