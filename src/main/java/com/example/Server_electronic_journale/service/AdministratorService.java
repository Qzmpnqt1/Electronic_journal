package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.model.Classroom;
import com.example.Server_electronic_journale.model.Group;
import com.example.Server_electronic_journale.model.Student;
import com.example.Server_electronic_journale.model.Subject;
import com.example.Server_electronic_journale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    private final ClassroomRepository classroomRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final GradebookRepository gradebookRepository;


    @Autowired
    public AdministratorService(ClassroomRepository classroomRepository,
                                GroupRepository groupRepository,
                                SubjectRepository subjectRepository,
                                StudentRepository studentRepository,
                                GradebookRepository gradebookRepository) {
        this.classroomRepository = classroomRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.gradebookRepository = gradebookRepository;
    }

    public Classroom addClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Group addGroup(Group group) {
        return groupRepository.save(group);
    }

    public Subject addSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void deleteGroup(int groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));
        if (group.getStudents().isEmpty()) {
            groupRepository.delete(group);
        }
        else {
            throw new IllegalStateException("Не удается удалить группу со студентами");
        }
    }

    public void deleteClassroom(int classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Аудитория не найдена"));
        classroomRepository.delete(classroom);
    }

    public void removeStudentFromGroup(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));
        gradebookRepository.deleteByStudent(student);
        studentRepository.delete(student);
    }

    public void deleteSubject(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Предмет не найден"));
        subjectRepository.delete(subject);
    }
}
