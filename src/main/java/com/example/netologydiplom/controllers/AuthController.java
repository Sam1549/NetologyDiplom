package com.example.netologydiplom.controllers;

import com.example.netologydiplom.dto.request.AuthRequest;
import com.example.netologydiplom.dto.request.RegisterRequest;
import com.example.netologydiplom.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PutMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return authService.registerNewUser(registerRequest);
    }


}
