package com.project2.backemd.controller;

import com.project2.backemd.dto.UserDto;
import com.project2.backemd.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project2.backemd.dto.request.AuthRequest;
import com.project2.backemd.dto.response.LoginResponse;
import com.project2.backemd.model.User;
import com.project2.backemd.service.AuthenticationService;
import com.project2.backemd.service.JwtService;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    private final UsersService service;

    public AuthController(AuthenticationService authenticationService, JwtService jwtService, UsersService service) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createToken(@RequestBody AuthRequest authRequest)
            throws Exception {

        User autenticatedUser = this.authenticationService.authenticate(authRequest);

        if (null != autenticatedUser) {
            String jwtToken = jwtService.generateToken(autenticatedUser);
            LoginResponse loginResponse = new LoginResponse.Builder()
                    .setToken(jwtToken)
                    .setExpiresIn(jwtService.getExpirationTime())
                    .build();
            return ResponseEntity.ok(loginResponse);
        }

        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("register")
    public CompletableFuture<String> createUserAsync(@RequestBody UserDto dto) {
        return service.guardarUsuarioAsync(dto).thenApply(result -> {
            return "CREATED";
        });
    }

}
