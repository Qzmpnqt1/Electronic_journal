package com.example.Server_electronic_journale.repository;

import com.example.Server_electronic_journale.model.Gradebook;
import com.example.Server_electronic_journale.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradebookRepository extends JpaRepository<Gradebook, Integer> {

    void deleteByStudent(Student student);

    // Метод для поиска зачетки по ID студента
    Optional<Gradebook> findByStudent_StudentId(int studentId);

    // Новый метод для поиска зачетки по email студента
    Optional<Gradebook> findByStudent_Email(String email);
}
