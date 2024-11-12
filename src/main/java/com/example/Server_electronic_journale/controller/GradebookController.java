package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.model.GradeEntry;
import com.example.Server_electronic_journale.service.GradebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gradebook")
public class GradebookController {

    private final GradebookService gradebookService;

    @Autowired
    public GradebookController(GradebookService gradebookService) {
        this.gradebookService = gradebookService;
    }

    @PostMapping("/add-grade")
    public GradeEntry addGrade(@RequestParam int studentId, @RequestParam int subjectId, @RequestParam int grade) {
        return gradebookService.addGrade(studentId, subjectId, grade);
    }
}

