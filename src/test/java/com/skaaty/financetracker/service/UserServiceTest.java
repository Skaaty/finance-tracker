package com.skaaty.financetracker.service;

import com.skaaty.financetracker.repository.UserRepository;
import com.skaaty.financetracker.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        User newUser = new User(null, "testuser", "test@test.com", "hash");
        User savedUser = new User(1L, "testuser", "test@test.com", "hash");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(newUser);

        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }
}
