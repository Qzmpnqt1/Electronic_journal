package com.example.Server_electronic_journale.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gradebooks")
public class Gradebook {

    @Id
    @GeneratedValue
    private int gradebook_id;

    // Связь со студентом (каждая зачетка принадлежит студенту)
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Связь с предметом
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // Оценка
    @Setter
    @Column(nullable = false)
    private int grade;  // Оценка (2, 3, 4 или 5)

    // Дата выставления оценки
    @Setter
    @Column(nullable = false)
    private LocalDate dateOfGrade;
}
