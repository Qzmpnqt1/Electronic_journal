package com.example.Server_electronic_journale.repository;

import com.example.Server_electronic_journale.model.GradeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeEntryRepository extends JpaRepository<GradeEntry, Integer> {
}
