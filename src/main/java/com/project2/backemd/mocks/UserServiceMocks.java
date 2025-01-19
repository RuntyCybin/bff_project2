package com.project2.backemd.mocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project2.backemd.dto.UserDto;

@Service
public class UserServiceMocks {


    @Async
    public CompletableFuture<List<String>> getMultipleUsers() throws InterruptedException {
        Thread.sleep(4000);
        return CompletableFuture.completedFuture(List.of("Jhon", "Nick", "Patrick"));
    }

    @Async
    public CompletableFuture<List<String>> getMultipleRoles() throws InterruptedException {
        Thread.sleep(2000);
        return CompletableFuture.completedFuture(List.of("ADMIN", "USER"));
    }

    @Async
    public CompletableFuture<List<UserDto>> getUsers() {

        List<UserDto> result = this.generateUsers();

        return CompletableFuture.completedFuture(result);
    }
    
    private List<UserDto> generateUsers() {
        return List.of(
                UserDto.builder()
                        .name("Anna")
                        .role(List.of("USER"))
                        .build(),
                UserDto.builder()
                        .name("John")
                        .role(List.of("USER"))
                        .build(),
                UserDto.builder()
                        .name("Nick")
                        .role(List.of("USER"))
                        .build());
    }
}
