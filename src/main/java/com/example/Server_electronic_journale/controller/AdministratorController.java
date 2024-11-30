package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.dto.GroupDTO;
import com.example.Server_electronic_journale.dto.GroupResponseDTO;
import com.example.Server_electronic_journale.dto.SubjectDTO;
import com.example.Server_electronic_journale.dto.SubjectResponseDTO;
import com.example.Server_electronic_journale.model.Group;
import com.example.Server_electronic_journale.model.Subject;
import com.example.Server_electronic_journale.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    // Управление группами
    @PostMapping("/groups")
    public ResponseEntity<GroupResponseDTO> addGroup(@RequestBody GroupDTO groupDTO) {
        try {
            Group addedGroup = administratorService.addGroup(groupDTO);
            // Преобразуем в GroupResponseDTO
            GroupResponseDTO dto = new GroupResponseDTO();
            dto.setGroupId(addedGroup.getGroupId());
            dto.setName(addedGroup.getName());
            // Преобразуем связанные предметы
            List<SubjectResponseDTO> subjectDTOs = addedGroup.getSubjects().stream()
                    .map(subject -> {
                        SubjectResponseDTO subjectDTO = new SubjectResponseDTO();
                        subjectDTO.setSubjectId(subject.getSubjectId());
                        subjectDTO.setName(subject.getName());
                        subjectDTO.setCourse(subject.getCourse());
                        return subjectDTO;
                    })
                    .collect(Collectors.toList());
            dto.setSubjects(subjectDTOs);

            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponseDTO>> getAllGroups() {
        List<Group> groups = administratorService.getAllGroups();
        List<GroupResponseDTO> groupDTOs = groups.stream()
                .map(group -> {
                    GroupResponseDTO dto = new GroupResponseDTO();
                    dto.setGroupId(group.getGroupId());
                    dto.setName(group.getName());
                    // Преобразуем связанные предметы
                    List<SubjectResponseDTO> subjectDTOs = group.getSubjects().stream()
                            .map(subject -> {
                                SubjectResponseDTO subjectDTO = new SubjectResponseDTO();
                                subjectDTO.setSubjectId(subject.getSubjectId());
                                subjectDTO.setName(subject.getName());
                                subjectDTO.setCourse(subject.getCourse());
                                return subjectDTO;
                            })
                            .collect(Collectors.toList());
                    dto.setSubjects(subjectDTOs);
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(groupDTOs);
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable int id) {
        try {
            administratorService.deleteGroup(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Управление предметами
    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        List<Subject> subjects = administratorService.getAllSubjects();
        List<SubjectResponseDTO> subjectDTOs = subjects.stream()
                .map(subject -> {
                    SubjectResponseDTO dto = new SubjectResponseDTO();
                    dto.setSubjectId(subject.getSubjectId());
                    dto.setName(subject.getName());
                    dto.setCourse(subject.getCourse());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(subjectDTOs);
    }

    @PostMapping("/subjects")
    public ResponseEntity<?> addSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            Subject subject = administratorService.addSubject(subjectDTO);
            return ResponseEntity.ok(subject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable int id) {
        try {
            administratorService.deleteSubject(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Управление студентами
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> removeStudentFromGroup(@PathVariable int id) {
        try {
            administratorService.removeStudentFromGroup(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}