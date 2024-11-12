package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.model.GradeEntry;
import com.example.Server_electronic_journale.model.Gradebook;
import com.example.Server_electronic_journale.model.Subject;
import com.example.Server_electronic_journale.repository.GradebookRepository;
import com.example.Server_electronic_journale.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GradebookService {

    private final GradebookRepository gradebookRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public GradebookService(GradebookRepository gradebookRepository, SubjectRepository subjectRepository) {
        this.gradebookRepository = gradebookRepository;
        this.subjectRepository = subjectRepository;
    }

    public GradeEntry addGrade(int studentId, int subjectId, int grade) {
        Gradebook gradebook = gradebookRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Gradebook not found for student ID: " + studentId));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found for ID: " + subjectId));

        GradeEntry gradeEntry = new GradeEntry();
        gradeEntry.setSubject(subject);
        gradeEntry.setGrade(grade);
        gradeEntry.setDateOfGrade(LocalDate.now());

        gradebook.addGrade(gradeEntry);
        gradebookRepository.save(gradebook);

        return gradeEntry;
    }
}

