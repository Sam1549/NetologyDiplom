package com.example.netologydiplom.services;

import com.example.netologydiplom.dto.request.AuthRequest;
import com.example.netologydiplom.dto.response.AuthResponse;
import com.example.netologydiplom.dto.request.RegisterRequest;
import com.example.netologydiplom.exceptions.UnauthorizedException;
import com.example.netologydiplom.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    public ResponseEntity<?> createAuthToken(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getLogin());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }


    public ResponseEntity<?> registerNewUser(RegisterRequest registerRequest) {
        if (userService.createNewUser(registerRequest)) {
            UserDetails userDetails = userService.loadUserByUsername(registerRequest.getLogin());
            String token = jwtTokenUtils.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
//            return new ResponseEntity<>(new ExceptionResponse("Такой пользователь уже существует",HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
            throw new UnauthorizedException("Такой пользователь уже существует");
        }

    }
}
