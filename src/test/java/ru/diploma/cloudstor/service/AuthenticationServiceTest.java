package ru.diploma.cloudstor.service;

import com.example.netologydiplom.dto.request.AuthRequest;
import com.example.netologydiplom.dto.response.AuthResponse;
import com.example.netologydiplom.entyties.Role;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.model.EnumRoles;
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
import org.springframework.security.core.userdetails.UserDetails;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.diploma.cloudstor.DataTest.*;

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
        when(userService.loadUserByUsername(USERNAME)).thenReturn(USER_DETAILS);
        when(jwtTokenUtil.generateToken(USER_DETAILS)).thenReturn(TOKEN);

        AuthResponse expected = AUTH_RESPONSE;
        AuthResponse result = authService.login(AUTH_REQUEST);
        assertEquals(expected, result);
        Mockito.verify(authRepository, Mockito.times(1)).putTokenAndUsername(TOKEN, USERNAME);
    }

    @Test
    void logoutTest() {
        when(authRepository.getUsernameByToken(BEARER_TOKEN.substring(7))).thenReturn(USERNAME);
        authService.logout(BEARER_TOKEN);
        Mockito.verify(authRepository, Mockito.times(1)).getUsernameByToken(BEARER_TOKEN.substring(7));
        Mockito.verify(authRepository, Mockito.times(1)).removeTokenAndUsernameByToken(BEARER_TOKEN.substring(7));
    }
}

