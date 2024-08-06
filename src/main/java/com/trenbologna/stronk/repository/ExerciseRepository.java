package com.trenbologna.stronk.repository;

import com.trenbologna.stronk.domain.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
    //ToDo: create interface functions for the crud operations
    Optional<ExerciseEntity> findExercisesByName(String name);
    Optional<ExerciseEntity> findExercisesByBodyPart(String bodyPart);
    Optional<ExerciseEntity> findExercisesByCategory(String category);
    void deleteById(Long id);
}
