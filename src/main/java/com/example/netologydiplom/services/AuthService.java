package com.example.netologydiplom.services;

import com.example.netologydiplom.dto.request.AuthRequest;
import com.example.netologydiplom.dto.response.AuthResponse;
import com.example.netologydiplom.dto.request.RegisterRequest;
import com.example.netologydiplom.exceptions.UnauthorizedException;
import com.example.netologydiplom.repositories.AuthRepository;
import com.example.netologydiplom.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthRepository authRepository;


    public AuthResponse login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.login());
        String token = jwtTokenUtils.generateToken(userDetails);
        log.info("User '{}' is authorized", userDetails.getUsername());
        authRepository.putTokenAndUsername(token, userDetails.getUsername());
        return new AuthResponse(token);
    }


    public ResponseEntity<?> registerNewUser(RegisterRequest registerRequest) {
        if (userService.createNewUser(registerRequest)) {
            UserDetails userDetails = userService.loadUserByUsername(registerRequest.login());
            String token = jwtTokenUtils.generateToken(userDetails);
            log.info("New User '{}' successful registration", userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            throw new UnauthorizedException("User with the same username already exists");
        }

    }

    public void logout(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        final String username = authRepository.getUsernameByToken(token);
        log.info("User '{}' logout", username);
        authRepository.removeTokenAndUsernameByToken(token);
    }
}
