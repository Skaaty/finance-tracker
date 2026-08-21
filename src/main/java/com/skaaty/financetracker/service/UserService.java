package com.skaaty.financetracker.service;

import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User registerUser(User user) {
        // TODO: password hashing here
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
