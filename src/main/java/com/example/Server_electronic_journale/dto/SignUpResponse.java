package com.example.Server_electronic_journale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
    private String token;
    private String email;
    private String role;
}
