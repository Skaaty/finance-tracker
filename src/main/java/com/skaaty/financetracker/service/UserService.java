package com.skaaty.financetracker.service;

import com.skaaty.financetracker.dto.request.UserRegistrationRequest;
import com.skaaty.financetracker.dto.response.UserResponse;
import com.skaaty.financetracker.model.Portfolio;
import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getRawPassword())); //add hashing later

        Portfolio emptyPortfolio = Portfolio.builder()
                .user(user)
                .cashBalance(BigDecimal.ZERO)
                .build();

        user.setPortfolio(emptyPortfolio);

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
