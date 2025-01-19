package com.project2.backemd.dto.response;

import lombok.Builder;
import lombok.Data;

public class LoginResponse {
    private String token;
    private Long expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    // Constructor privado para forzar el uso del builder
    private LoginResponse(Builder builder) {
        this.token = builder.token;
        this.expiresIn = builder.expiresIn;
    }

    // Clase Builder
    public static class Builder {
        private String token;
        private Long expiresIn;

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setExpiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(this);
        }
    }
}
