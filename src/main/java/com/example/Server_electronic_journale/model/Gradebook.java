package com.example.Server_electronic_journale.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gradebooks")
public class Gradebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gradebook_id")
    private int gradebookId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Список записей оценок для разных предметов
    @OneToMany(mappedBy = "gradebook", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GradeEntry> grades = new HashSet<>();

    public void addGrade(GradeEntry gradeEntry) {
        grades.add(gradeEntry);
        gradeEntry.setGradebook(this);
    }

    public void removeGrade(GradeEntry gradeEntry) {
        grades.remove(gradeEntry);
        gradeEntry.setGradebook(null);
    }
}
