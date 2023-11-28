package com.example.netologydiplom.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
