package com.skaaty.financetracker.service;

import com.skaaty.financetracker.dto.request.UserRegistrationRequest;
import com.skaaty.financetracker.dto.response.UserResponse;
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
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setEmail("test@test.com");
        request.setRawPassword("hash");

        User savedUser = new User(1L, "testuser", "test@test.com", "hash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.registerUser(request);

        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@test.com", result.getEmail());
    }
}
