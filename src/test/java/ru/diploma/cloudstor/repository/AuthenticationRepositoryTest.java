package ru.diploma.cloudstor.repository;

import com.example.netologydiplom.repositories.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static ru.diploma.cloudstor.DataTest.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("=== Testing authentication repository ===")
public class AuthenticationRepositoryTest {
    @Autowired
    AuthRepository authRepository;
    private final Map<String, String> testTokens = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        authRepository = new AuthRepository();
        authRepository.putTokenAndUsername(TOKEN, USERNAME);
        testTokens.clear();
        testTokens.put(TOKEN, USERNAME);
    }

    @Test
    void putTokenAndUsernameTest() {
        authRepository.putTokenAndUsername(TOKEN, USERNAME);
        String actual = authRepository.getUsernameByToken(TOKEN);
        assertEquals(USERNAME, actual);
    }

    @Test
    void getUsernameByToken() {
        String tokenActual = authRepository.getUsernameByToken(TOKEN);
        assertEquals(testTokens.get(TOKEN), tokenActual);
    }

    @Test
    void removeTokenAndUsernameByToken() {
        String beforeRemove = authRepository.getUsernameByToken(TOKEN);
        assertNotNull(beforeRemove);
        authRepository.removeTokenAndUsernameByToken(TOKEN);
        String afterRemove = authRepository.getUsernameByToken(TOKEN);
        assertNull(afterRemove);
    }
}
