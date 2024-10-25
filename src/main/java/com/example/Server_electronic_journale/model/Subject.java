package com.example.Server_electronic_journale.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue
    private int subject_id;

    @Setter
    @Column(nullable = false)
    private String name;

    // Связь Many-to-Many с Teacher
    @ManyToMany(mappedBy = "subjects")
    private Set<Teacher> teachers;

    // Связь с зачетками
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Gradebook> gradebooks = new HashSet<>();  // Множество записей зачеток для предмета
}
