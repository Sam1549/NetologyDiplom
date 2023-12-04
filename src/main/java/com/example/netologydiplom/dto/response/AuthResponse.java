package com.example.netologydiplom.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public record AuthResponse(String authToken) {
    @JsonProperty("auth-token")
    public String authToken() {
        return authToken;
    }
}
