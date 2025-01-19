package com.project2.backemd.controller;

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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    public AuthController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
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

}
