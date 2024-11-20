package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.model.Group;
import com.example.Server_electronic_journale.model.Student;
import com.example.Server_electronic_journale.model.Subject;
import com.example.Server_electronic_journale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final GradebookRepository gradebookRepository;


    @Autowired
    public AdministratorService(GroupRepository groupRepository,
                                SubjectRepository subjectRepository,
                                StudentRepository studentRepository,
                                GradebookRepository gradebookRepository) {
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.gradebookRepository = gradebookRepository;
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
