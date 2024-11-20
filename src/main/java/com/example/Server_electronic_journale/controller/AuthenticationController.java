package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.dto.AuthRequest;
import com.example.Server_electronic_journale.dto.AuthResponse;
import com.example.Server_electronic_journale.dto.StudentSignUpRequest;
import com.example.Server_electronic_journale.dto.TeacherSignUpRequest;
import com.example.Server_electronic_journale.dto.SignUpResponse;
import com.example.Server_electronic_journale.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<String> getAuthInfo() {
        return ResponseEntity.ok("Authentication endpoint is active");
    }

    // Вход пользователя
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authenticationService.signIn(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Регистрация студента
    @PostMapping("/register/student")
    public ResponseEntity<SignUpResponse> registerStudent(@RequestBody StudentSignUpRequest request) {
        try {
            SignUpResponse response = authenticationService.registerStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Регистрация учителя
    @PostMapping("/register/teacher")
    public ResponseEntity<SignUpResponse> registerTeacher(@RequestBody TeacherSignUpRequest request) {
        try {
            SignUpResponse response = authenticationService.registerTeacher(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
