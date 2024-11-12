package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.model.Classroom;
import com.example.Server_electronic_journale.model.Group;
import com.example.Server_electronic_journale.model.Subject;
import com.example.Server_electronic_journale.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping("/classroom")
    public Classroom addClassroom(@RequestBody Classroom classroom) {
        return administratorService.addClassroom(classroom);
    }

    @PostMapping("/group")
    public Group addGroup(@RequestBody Group group) {
        return administratorService.addGroup(group);
    }

    @PostMapping("/subject")
    public Subject addSubject(@RequestBody Subject subject) {
        return administratorService.addSubject(subject);
    }

    @DeleteMapping("/classroom/{id}")
    public void deleteClassroom(@PathVariable int id) {
        administratorService.deleteClassroom(id);
    }

    @DeleteMapping("/group/{id}")
    public void deleteGroup(@PathVariable int id) {
        administratorService.deleteGroup(id);
    }

    @DeleteMapping("/subject/{id}")
    public void deleteSubject(@PathVariable int id) {
        administratorService.deleteSubject(id);
    }

    @DeleteMapping("/student/{id}")
    public void removeStudentFromGroup(@PathVariable int id) {
        administratorService.removeStudentFromGroup(id);
    }
}
