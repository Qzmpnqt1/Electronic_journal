package com.example.Server_electronic_journale.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentSignUpRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate dateOfBirth;
    private int groupId; // ID группы, к которой относится студент
}
