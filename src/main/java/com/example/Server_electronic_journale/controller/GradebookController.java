package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.dto.GradeEntryRequest;
import com.example.Server_electronic_journale.model.GradeEntry;
import com.example.Server_electronic_journale.service.GradebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/gradebook")
public class GradebookController {

    private final GradebookService gradebookService;

    @Autowired
    public GradebookController(GradebookService gradebookService) {
        this.gradebookService = gradebookService;
    }

    @PostMapping("/add-grade")
    public GradeEntry addGrade(@RequestBody GradeEntryRequest gradeEntryRequest) {
        return gradebookService.addGrade(
                gradeEntryRequest.getStudentId(),
                gradeEntryRequest.getSubjectId(),
                gradeEntryRequest.getGrade()
        );
    }
}




