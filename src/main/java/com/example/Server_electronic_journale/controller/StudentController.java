package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.model.Gradebook;
import com.example.Server_electronic_journale.model.Student;
import com.example.Server_electronic_journale.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/personal-data")
    public Student getPersonalData() {
        return studentService.getCurrentStudent();
    }

    @GetMapping("/gradebook")
    public Gradebook getGradebook() {
        Student student = studentService.getCurrentStudent();
        return student.getGradebook();
    }
}
