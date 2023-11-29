package ru.diploma.cloudstor.service;

import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.repositories.UserRepository;
import com.example.netologydiplom.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.diploma.cloudstor.DataTest.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("=== Testing User service ===")
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        User userExpected = (User) USER_DETAILS;
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(userExpected));
        assertEquals(userExpected, userService.loadUserByUsername(USERNAME));
    }

    @Test
    void loadUserByUsernameUnauthorized() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(USER_UNAUTHORIZED));
    }
}