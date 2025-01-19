package com.project2.backemd.service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project2.backemd.dto.UserDto;
import com.project2.backemd.mappers.UserMapper;
import com.project2.backemd.model.Role;
import com.project2.backemd.model.User;
import com.project2.backemd.repository.OrdersRepository;
import com.project2.backemd.repository.RolesRepository;
import com.project2.backemd.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
public class UsersService {

    private final static String USER_NOT_FOUND = "User not found with ID: ";
    private final static String USER_EMAIL_NOT_FOUND = "User not found with EMAIL: ";

    private final UsersRepository repo;

    private final UserMapper mapper;

    private final OrdersRepository ordersService;

    private final RolesRepository rolesService;

    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repo, UserMapper mapper, OrdersRepository ordersService, RolesRepository rolesService, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.mapper = mapper;
        this.ordersService = ordersService;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean authenticate(String username, String password) {
        User user = repo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    @Transactional
    @Async
    public CompletableFuture<UserDto> obtenerUsuarioAsyncPorId(Integer id)
            throws InterruptedException, ExecutionException {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND + id));

        return CompletableFuture.completedFuture(new UserDto.Builder()
                .setName(user.getName())
                .setRole(user.getRoles().stream()
                        .map(rol -> rol.getDescription())
                        .toList())
                .setEmail(user.getEmail())
                .build());
    }

    @Transactional
    @Async
    public CompletableFuture<UserDto> obtenerUsuarioAsyncPorEmail(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_EMAIL_NOT_FOUND + email));
        return CompletableFuture.completedFuture(new UserDto.Builder()
                .setName(user.getName())
                .setRole(user.getRoles().stream()
                        .map(rol -> rol.getDescription())
                        .toList())
                .setEmail(user.getEmail())
                .build());
    }

    @Transactional
    public void guardarUsuario(UserDto dto) {
        try {
            User user = new User();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setName(dto.getName());
            Set<Role> roles = dto.getRole().stream()
                    .map(role -> {
                        return rolesService.findByDescription(role)
                                .orElseThrow(() -> new RuntimeException(
                                        "Role not found"));
                    })
                    .collect(Collectors.toSet());
            user.setRoles(roles);

            repo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user");
        }
    }

    @Transactional
    @Async
    public CompletableFuture<Void> guardarUsuarioAsync(UserDto dto) {

        return CompletableFuture.runAsync(() -> {
            try {
                User user = new User();
                user.setEmail(dto.getEmail());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setName(dto.getName());
                Set<Role> roles = dto.getRole().stream()
                        .map(role -> {
                            return rolesService.findByDescription(role)
                                    .orElseThrow(() -> new RuntimeException(
                                            "Role not found"));
                        })
                        .collect(Collectors.toSet());
                user.setRoles(roles);

                repo.save(user);
            } catch (Exception e) {
                throw new RuntimeException("Error saving user");
            }
        });
    }
}
