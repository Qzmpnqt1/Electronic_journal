package com.example.Server_electronic_journale.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade_entries")
public class GradeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private int entryId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "gradebook_id", nullable = false)
    private Gradebook gradebook;

    @Setter
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Setter
    @Column(nullable = false)
    private int grade;

    @Setter
    @Column(nullable = false)
    private LocalDate dateOfGrade;
}
