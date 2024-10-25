package com.example.Server_electronic_journale.service;

import com.example.Server_electronic_journale.model.Classroom;
import com.example.Server_electronic_journale.model.Group;
import com.example.Server_electronic_journale.model.Subject;
import com.example.Server_electronic_journale.repository.ClassroomRepository;
import com.example.Server_electronic_journale.repository.GroupRepository;
import com.example.Server_electronic_journale.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    private final ClassroomRepository classroomRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public AdministratorService(ClassroomRepository classroomRepository,
                                GroupRepository groupRepository,
                                SubjectRepository subjectRepository) {
        this.classroomRepository = classroomRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
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
}
