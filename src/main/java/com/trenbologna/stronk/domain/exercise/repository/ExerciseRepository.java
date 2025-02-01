package com.trenbologna.stronk.domain.exercise.repository;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
    List<ExerciseEntity> findExercisesByUser_Email(String email);
    Optional<ExerciseEntity> findByIdAndUser_Email(Long id, String email);
    Optional<ExerciseEntity> findExercisesByName(String name);
    Optional<ExerciseEntity> findExercisesByBodyPart(String bodyPart);
    Optional<ExerciseEntity> findExercisesByCategory(String category);
    void deleteById(Long id);
    void deleteByIdAndUser_Email(Long id, String email);
}
