package com.example.netologydiplom.repository;

import com.example.netologydiplom.DataTest;
import com.example.netologydiplom.repositories.AuthRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("=== Testing authentication repository ===")
public class AuthenticationRepositoryTest {
    @Autowired
    AuthRepository authRepository;
    private final Map<String, String> testTokens = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        authRepository = new AuthRepository();
        authRepository.putTokenAndUsername(DataTest.TOKEN, DataTest.USERNAME);
        testTokens.clear();
        testTokens.put(DataTest.TOKEN, DataTest.USERNAME);
    }

    @Test
    void putTokenAndUsernameTest() {
        authRepository.putTokenAndUsername(DataTest.TOKEN, DataTest.USERNAME);
        String actual = authRepository.getUsernameByToken(DataTest.TOKEN);
        Assertions.assertEquals(DataTest.USERNAME, actual);
    }

    @Test
    void getUsernameByToken() {
        String tokenActual = authRepository.getUsernameByToken(DataTest.TOKEN);
        assertEquals(testTokens.get(DataTest.TOKEN), tokenActual);
    }

    @Test
    void removeTokenAndUsernameByToken() {
        String beforeRemove = authRepository.getUsernameByToken(DataTest.TOKEN);
        assertNotNull(beforeRemove);
        authRepository.removeTokenAndUsernameByToken(DataTest.TOKEN);
        String afterRemove = authRepository.getUsernameByToken(DataTest.TOKEN);
        assertNull(afterRemove);
    }
}
