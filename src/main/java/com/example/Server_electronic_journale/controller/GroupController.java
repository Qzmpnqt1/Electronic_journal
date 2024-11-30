package com.example.Server_electronic_journale.controller;

import com.example.Server_electronic_journale.model.Group;
import com.example.Server_electronic_journale.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    public Group addGroup(String name) {
        // Проверка на уникальность имени группы
        if (groupRepository.existsByName(name)) {
            throw new IllegalArgumentException("Группа с таким названием уже существует");
        }
        Group group = new Group();
        group.setName(name);
        return groupRepository.save(group);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}