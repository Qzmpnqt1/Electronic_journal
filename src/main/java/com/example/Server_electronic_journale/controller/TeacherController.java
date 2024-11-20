package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.model.*;
import com.example.Server_electronic_journale.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Эндпоинт для получения предметов учителя
    @GetMapping("/subjects")
    public Set<Subject> getSubjects() {
        return teacherService.getSubjectsForTeacher();
    }

    // Эндпоинт для получения групп по предмету
    @GetMapping("/subjects/{subjectId}/groups")
    public Set<Group> getGroupsForSubject(@PathVariable int subjectId) {
        return teacherService.getGroupsForSubject(subjectId);
    }

    // Эндпоинт для получения студентов в группе
    @GetMapping("/groups/{groupId}/students")
    public Set<Student> getStudentsInGroup(@PathVariable int groupId) {
        return teacherService.getStudentsInGroup(groupId);
    }

    // Эндпоинт для добавления оценки студенту (если его не было)
    @PostMapping("/students/{studentId}/subjects/{subjectId}/grade")
    public GradeEntry addGradeToStudent(
            @PathVariable int studentId,
            @PathVariable int subjectId,
            @RequestParam int grade) {
        return teacherService.addGradeToStudent(studentId, subjectId, grade);
    }

    // Эндпоинт для получения личных данных учителя
    @GetMapping("/personal-data")
    public Teacher getPersonalData() {
        return teacherService.getPersonalData();
    }
}
