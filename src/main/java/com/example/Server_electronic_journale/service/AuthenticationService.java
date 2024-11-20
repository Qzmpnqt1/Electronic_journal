package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.dto.*;
import com.example.Server_electronic_journale.model.*;
import com.example.Server_electronic_journale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public SignUpResponse registerStudent(StudentSignUpRequest request) {
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже используется");
        }

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));

        Student student = Student.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .dateOfBirth(request.getDateOfBirth())
                .group(group)
                .build();

        studentRepository.save(student);

        String jwt = jwtService.generateToken(student);

        return new SignUpResponse(jwt, student.getUsername(), "ROLE_STUDENT");
    }

    public SignUpResponse registerTeacher(TeacherSignUpRequest request) {
        if (teacherRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже используется");
        }

        Set<Subject> subjects = request.getSubjectIds().stream()
                .map(id -> subjectRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Предмет не найден: ID " + id)))
                .collect(Collectors.toSet());

        Teacher teacher = Teacher.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .subjects(subjects)
                .build();

        teacherRepository.save(teacher);

        String jwt = jwtService.generateToken(teacher);

        return new SignUpResponse(jwt, teacher.getUsername(), "ROLE_TEACHER");
    }

    public AuthResponse signIn(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = loadUserByEmail(request.getEmail());

        String jwt = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Роль пользователя не найдена"))
                .getAuthority();

        return new AuthResponse(jwt, userDetails.getUsername(), role);
    }

    private UserDetails loadUserByEmail(String email) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            return studentOpt.get();
        }

        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(email);
        if (teacherOpt.isPresent()) {
            return teacherOpt.get();
        }

        // Добавлена проверка администратора
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            return adminOpt.get();
        }

        throw new IllegalArgumentException("Пользователь не найден");
    }

}
