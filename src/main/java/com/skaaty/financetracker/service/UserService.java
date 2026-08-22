package com.skaaty.financetracker.service;

import com.skaaty.financetracker.dto.UserRegistrationRequest;
import com.skaaty.financetracker.dto.UserResponse;
import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse registerUser(UserRegistrationRequest request) {
        // TODO: password hashing her
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getRawPassword()); //add hashing later

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
