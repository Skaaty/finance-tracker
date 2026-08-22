package com.skaaty.financetracker.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String rawPassword;
}
