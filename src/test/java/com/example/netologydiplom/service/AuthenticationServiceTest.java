package com.example.netologydiplom.service;

import com.example.netologydiplom.DataTest;
import com.example.netologydiplom.dto.response.AuthResponse;
import com.example.netologydiplom.repositories.AuthRepository;
import com.example.netologydiplom.security.JwtTokenUtils;
import com.example.netologydiplom.services.AuthService;
import com.example.netologydiplom.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("=== Testing authentication service ===")
public class AuthenticationServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    AuthRepository authRepository;

    @Mock
    UserService userService;

    @Mock
    JwtTokenUtils jwtTokenUtil;
    @Mock
    AuthenticationManager authenticationManager;


    @Test
    void loginTest() {
        when(userService.loadUserByUsername(DataTest.USERNAME)).thenReturn(DataTest.USER_DETAILS);
        when(jwtTokenUtil.generateToken(DataTest.USER_DETAILS)).thenReturn(DataTest.TOKEN);

        AuthResponse expected = DataTest.AUTH_RESPONSE;
        AuthResponse result = authService.login(DataTest.AUTH_REQUEST);
        assertEquals(expected, result);
        Mockito.verify(authRepository, Mockito.times(1)).putTokenAndUsername(DataTest.TOKEN, DataTest.USERNAME);
    }

    @Test
    void logoutTest() {
        when(authRepository.getUsernameByToken(DataTest.BEARER_TOKEN.substring(7))).thenReturn(DataTest.USERNAME);
        authService.logout(DataTest.BEARER_TOKEN);
        Mockito.verify(authRepository, Mockito.times(1)).getUsernameByToken(DataTest.BEARER_TOKEN.substring(7));
        Mockito.verify(authRepository, Mockito.times(1)).removeTokenAndUsernameByToken(DataTest.BEARER_TOKEN.substring(7));
    }
}

