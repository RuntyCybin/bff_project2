package com.project2.backemd.service;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project2.backemd.model.User;
import com.project2.backemd.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final static String USER_EMAIL_NOT_FOUND = "User not found with EMAIL: ";

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_EMAIL_NOT_FOUND + email));
        String roles = user.getRoles().stream().map(rol -> rol.getDescription()).collect(Collectors.joining(", "));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }

}
