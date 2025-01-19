package com.project2.backemd.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.project2.backemd.dto.request.AuthRequest;
import com.project2.backemd.model.User;
import com.project2.backemd.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UsersRepository usersRepository;

    public User authenticate(AuthRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        return usersRepository.findByEmail(authRequest.getEmail()).orElseThrow(null);
    }

}
