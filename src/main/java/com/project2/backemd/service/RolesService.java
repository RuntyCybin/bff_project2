package com.project2.backemd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.project2.backemd.model.Role;
import com.project2.backemd.model.User;
import com.project2.backemd.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolesService {

    private UsersRepository repo;

    @Async
    public CompletableFuture<List<String>> obtenerRolesPorIdUsuarioAsync(Integer userId) {
        Optional<User> userOpt = repo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Set<Role> userRoles = user.getRoles();

            List<String> roleDtoList = new ArrayList<>();
            for (Role elem : userRoles) {
                roleDtoList.add(elem.getDescription());
            }

            return CompletableFuture.completedFuture(roleDtoList);
        } else {
            return CompletableFuture
                    .failedFuture(new IllegalArgumentException("User with ID " + userId + " not found"));
        }
    }

}
