package com.example.Server_electronic_journale.model;

import com.example.Server_electronic_journale.service.AdministratorService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "administrator")
public class Administrator {

    @Id
    @GeneratedValue
    private int admin_id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Autowired
    private AdministratorService administratorService;

    // Метод для добавления новой группы
    public Group addGroup(Group group) {
        return administratorService.addGroup(group);
    }

    // Метод для добавления новой аудитории
    public Classroom addClassroom(Classroom classroom) {
        return administratorService.addClassroom(classroom);
    }

    // Метод для добавления нового предмета
    public Subject addSubject(Subject subject) {
        return administratorService.addSubject(subject);
    }
}
