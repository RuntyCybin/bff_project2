package com.project2.backemd.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

public class UserDto {
    private String name;
    private String password;
    private List<String> role;
    private String email;

    // Constructor privado para forzar el uso del builder
    private UserDto(Builder builder) {
        this.name = builder.name;
        this.password = builder.password;
        this.role = builder.role;
        this.email = builder.email;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    // Clase Builder
    public static class Builder {
        private String name;
        private String password;
        private List<String> role;
        private String email;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(List<String> role) {
            this.role = role;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }
}