package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.model.*;
import com.example.Server_electronic_journale.repository.GradeEntryRepository;
import com.example.Server_electronic_journale.repository.GradebookRepository;
import com.example.Server_electronic_journale.repository.StudentRepository;
import com.example.Server_electronic_journale.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class GradebookService {

    private final GradebookRepository gradebookRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final TeacherService teacherService;
    private final GradeEntryRepository gradeEntryRepository;

    @Autowired
    public GradebookService(GradebookRepository gradebookRepository, SubjectRepository subjectRepository, StudentRepository studentRepository, TeacherService teacherService, GradeEntryRepository gradeEntryRepository) {
        this.gradebookRepository = gradebookRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.teacherService = teacherService;
        this.gradeEntryRepository = gradeEntryRepository;
    }

    @Transactional
    public GradeEntry addGrade(int studentId, int subjectId, int grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Предмет не найден"));

        Teacher teacher = teacherService.getCurrentTeacher();
        if (!teacher.getSubjects().contains(subject)) {
            throw new IllegalArgumentException("Вы не преподаете этот предмет");
        }

        if (!subject.getGroups().contains(student.getGroup())) {
            throw new IllegalArgumentException("Студент не изучает этот предмет");
        }

        Gradebook gradebook = student.getGradebook();
        if (gradebook == null) {
            gradebook = new Gradebook();
            gradebook.setStudent(student);
            student.setGradebook(gradebook);
            gradebookRepository.save(gradebook);
        }

        GradeEntry gradeEntry = new GradeEntry();
        gradeEntry.setGradebook(gradebook);
        gradeEntry.setSubject(subject);
        gradeEntry.setGrade(grade);

        gradeEntryRepository.save(gradeEntry);
        return gradeEntry;
    }
}



