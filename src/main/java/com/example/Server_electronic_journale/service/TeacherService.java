package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.model.*;
import com.example.Server_electronic_journale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GradebookRepository gradebookRepository;
    private final GradeEntryRepository gradeEntryRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          SubjectRepository subjectRepository,
                          GroupRepository groupRepository,
                          StudentRepository studentRepository,
                          GradebookRepository gradebookRepository,
                          GradeEntryRepository gradeEntryRepository) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.gradebookRepository = gradebookRepository;
        this.gradeEntryRepository = gradeEntryRepository;
    }

    // Получение текущего учителя
    public Teacher getCurrentTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }
        String email = authentication.getName();
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Учитель не найден"));
    }


    // Получение предметов, которые ведет учитель
    public Set<Subject> getSubjectsForTeacher() {
        Teacher teacher = getCurrentTeacher();
        return teacher.getSubjects();
    }

    // Получение групп для определенного предмета
    public Set<Group> getGroupsForSubject(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Предмет не найден"));
        return subject.getGroups();
    }

    // Получение студентов в группе
    public Set<Student> getStudentsInGroup(int groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));
        return group.getStudents();
    }

    // Добавление оценки студенту по предмету
    @Transactional
    public GradeEntry addGradeToStudent(int studentId, int subjectId, int grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Предмет не найден"));

        // Проверяем, что учитель ведет этот предмет
        Teacher teacher = getCurrentTeacher();
        if (!teacher.getSubjects().contains(subject)) {
            throw new IllegalArgumentException("Вы не преподаете этот предмет");
        }

        // Проверяем, что студент изучает этот предмет в своей группе
        if (!subject.getGroups().contains(student.getGroup())) {
            throw new IllegalArgumentException("Студент не изучает этот предмет");
        }

        // Получаем зачетку студента
        Gradebook gradebook = student.getGradebook();
        if (gradebook == null) {
            // Создаем зачетку, если ее нет
            gradebook = new Gradebook();
            gradebook.setStudent(student);
            student.setGradebook(gradebook);
            gradebookRepository.save(gradebook);
        }

        // Создаем запись оценки
        GradeEntry gradeEntry = new GradeEntry();
        gradeEntry.setGradebook(gradebook);
        gradeEntry.setSubject(subject);
        gradeEntry.setGrade(grade);
        gradeEntry.setDateOfGrade(LocalDate.now());

        // Добавляем запись в зачетку
        gradebook.addGrade(gradeEntry);

        gradeEntryRepository.save(gradeEntry);
        gradebookRepository.save(gradebook);

        return gradeEntry;
    }

    // Получение личных данных учителя
    public Teacher getPersonalData() {
        return getCurrentTeacher();
    }
}
