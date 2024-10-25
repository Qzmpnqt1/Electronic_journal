package com.example.Server_electronic_journale.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue
    private int schedule_id;

    // Связь с группой
    @Setter
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // Связь с предметами
    @ManyToMany
    @JoinTable(
            name = "schedule_subject",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    // Связь с аудиторией
    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Setter
    @Column(nullable = false)
    private String dayOfWeek;  // День недели, когда проходят занятия (например, "Понедельник")
}
